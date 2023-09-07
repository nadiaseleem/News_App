package com.example.news.ui.home.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.news.R
import com.example.news.api.ApiManager
import com.example.news.api.articlesModel.Article
import com.example.news.api.articlesModel.ArticlesResponse
import com.example.news.api.sourcesModel.Source
import com.example.news.api.sourcesModel.SourcesResponse
import com.example.news.databinding.FragmentArticlesBinding
import com.example.news.ui.home.MainActivity
import com.example.news.ui.home.articleDetails.ArticleDetailsFragment
import com.example.news.util.Constants
import com.example.news.util.showAlertDialog
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ArticlesFragment : Fragment() {

    private lateinit var binding: FragmentArticlesBinding
    private var articles = listOf<Article>()
    private val adapter = ArticlesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentArticlesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = arguments?.getString(Constants.CATEGORY).toString()
        getSources(category)
        initRecyclerView()
    }

    private fun initRecyclerView() {

        binding.articlesRv.adapter = adapter
        adapter.onArticleClick = { article: Article ->
            val bundle = Bundle()
            bundle
                .putParcelable(Constants.ARTICLE, article)
            val fragment = ArticleDetailsFragment()
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack("").commit()
        }
    }

    private fun getArticles(source: String) {
        binding.progressBar.isVisible = true

        ApiManager
            .getApis().getArticles(source = source)
            .enqueue(object : Callback<ArticlesResponse> {
                override fun onResponse(
                    call: Call<ArticlesResponse>,
                    response: Response<ArticlesResponse>
                ) {
                    binding.progressBar.isVisible = false
                    if (response.isSuccessful) {
                        articles = response.body()?.articles as List<Article>
                        adapter.updateArticles(articles)
                        if (articles.isEmpty())
                            binding.llNotFound.visibility = View.VISIBLE
                        else
                            binding.llNotFound.visibility = View.GONE

                    } else {
                        val jsonString = response.errorBody()?.string()
                        val response = Gson().fromJson(jsonString, ArticlesResponse::class.java)

                        handleError(response.message) {
                            getArticles(source)
                        }
                    }

                }

                override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                    binding.progressBar.isVisible = false
                    handleError(t.localizedMessage) {
                        getArticles(source)
                    }

                }

            })
    }

    fun interface OnTryAgainClickListener {
        fun onTryAgainClick()
    }

    private fun handleError(message: String? = null, onClickListener: OnTryAgainClickListener) {
        showAlertDialog(message
            ?: "something went wrong",
            posActionName = "try again",
            posAction = { dialog, which ->
                dialog.dismiss()
                onClickListener.onTryAgainClick()
            },
            negActionName = "cancel",
            negAction = { dialog, which ->
                dialog.dismiss()
            })
    }

    private fun getSources(category: String) {
        binding.progressBar.isVisible = true
        ApiManager.getApis().getSources(category = category)
            .enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    binding.progressBar.isVisible = false
                    if (response.isSuccessful) {
                        bindTabs(response.body()?.sources)

                    } else {
                        val jsonString = response.errorBody()?.string()
                        val response = Gson().fromJson(jsonString, SourcesResponse::class.java)

                        handleError(response.message) {
                            getSources(category)
                        }
                    }

                }

                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                    binding.progressBar.isVisible = false
                    handleError(t.localizedMessage) {
                        getSources(category)
                    }
                }

            })
    }

    private fun bindTabs(sources: List<Source?>?) {
        if (sources == null)
            return
        sources.forEach { source ->
            val tab = binding.tabLayout.newTab()
            tab.text = source?.name
            tab.tag = source?.id
            binding.tabLayout.addTab(tab)
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                getArticles(tab?.tag.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                getArticles(tab?.tag.toString())
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                getArticles(tab?.tag.toString())
            }
        })
        binding.tabLayout.getTabAt(0)?.select()


    }

    override fun onResume() {
        super.onResume()
        setCustomToolbarTitle(arguments?.getString(Constants.CATEGORY).toString())
        enableBackArrowButton()

    }


    override fun onPause() {
        super.onPause()
        disableBackArrowButton()
    }


    private fun enableBackArrowButton() {
        val activity = requireActivity() as MainActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)

        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun disableBackArrowButton() {
        val activity = requireActivity() as MainActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.setDisplayShowHomeEnabled(false)
    }


    private fun setCustomToolbarTitle(title: String) {
        val activity = requireActivity()

        if (activity is AppCompatActivity) {
            val toolbarTitle = activity.findViewById<TextView>(R.id.toolbarTitle)
            toolbarTitle?.text = title

        }
    }




}