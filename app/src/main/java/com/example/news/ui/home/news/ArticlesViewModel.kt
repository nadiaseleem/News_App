package com.example.news.ui.home.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.api.articlesModel.Article
import com.example.news.data.api.articlesModel.ArticlesResponse
import com.example.news.data.api.sourcesModel.Source
import com.example.news.data.api.sourcesModel.SourcesResponse
import com.example.news.repository.articles.ArticlesRepository
import com.example.news.repository.sources.SourcesRepository
import com.example.news.util.ViewError
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val sourcesRepository: SourcesRepository,
    private val articlesRepository: ArticlesRepository
) : ViewModel() {
    val shouldLoad = MutableLiveData<Boolean>(true)
    val articlesList = MutableLiveData<List<Article?>?>()
    val sourcesList = MutableLiveData<List<Source?>?>()
    val errorLiveData = MutableLiveData<ViewError>()
    val shouldDisplayNoArticlesFound = MutableLiveData<Boolean>(false)
    fun getArticles(source: String) {
        shouldLoad.postValue(true)
        viewModelScope.launch {
            try {
                val articles = articlesRepository.getArticles(source = source)
                articlesList.postValue(articles)

                if (articles?.isEmpty() == true)
                    shouldDisplayNoArticlesFound.postValue(true)
                else
                    shouldDisplayNoArticlesFound.postValue(false)
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val response = Gson().fromJson(jsonString, ArticlesResponse::class.java)
                errorLiveData.postValue(ViewError(
                    response.message
                ) {
                    getArticles(source)
                })
            } catch (e: Exception) {
                errorLiveData.postValue(ViewError(e.localizedMessage) {
                    getArticles(source)
                })
            } finally {
                shouldLoad.postValue(false)
            }

        }

    }


    fun getSources(category: String) {
        shouldLoad.postValue(true)
        viewModelScope.launch {
            try {
                val sources = sourcesRepository.getSources(category = category)
                sourcesList.postValue(sources)
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val response = Gson().fromJson(jsonString, SourcesResponse::class.java)
                errorLiveData.postValue(ViewError(response.message) {
                    getSources(category)

                })
            } catch (e: Exception) {
                errorLiveData.postValue(ViewError(e.localizedMessage) {
                    getSources(category)
                })
            } finally {
                shouldLoad.postValue(false)

            }

        }

    }

}