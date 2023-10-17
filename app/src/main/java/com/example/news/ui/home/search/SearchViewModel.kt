package com.example.news.ui.home.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.api.articlesModel.Article
import com.example.news.data.api.articlesModel.ArticlesResponse
import com.example.news.repository.articles.ArticlesRepository
import com.example.news.util.ViewError
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val articlesRepository: ArticlesRepository) :
    ViewModel() {
    val articlesLiveData = MutableLiveData<List<Article?>?>()
    val errorLiveData = MutableLiveData<ViewError>()
    fun searchArticles(searchQuery: String) {
        viewModelScope.launch {
            try {
                val articles = articlesRepository.getArticlesThatHas(searchKeyWord = searchQuery)
                articlesLiveData.postValue(articles)
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val response = Gson().fromJson(jsonString, ArticlesResponse::class.java)

                errorLiveData.postValue(ViewError(
                    response.message
                ) {
                    searchArticles(searchQuery)
                })
            } catch (e: Exception) {
                errorLiveData.postValue(ViewError(
                    e.localizedMessage
                ) {
                    searchArticles(searchQuery)
                })
            }


        }

    }

}