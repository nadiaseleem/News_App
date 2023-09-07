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
import com.example.news.databinding.FragmentArticlesBinding
import com.example.news.ui.api.ApiManager
import com.example.news.ui.api.model.Source
import com.example.news.ui.api.model.SourcesResponse
import com.example.news.ui.home.MainActivity
import com.example.news.ui.util.Constants
import com.example.news.ui.util.showAlertDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ArticlesFragment : Fragment() {

    private lateinit var binding: FragmentArticlesBinding

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

    }

    private fun getSources(category: String) {
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

                        showAlertDialog(response.message
                            ?: "something went wrong",
                            posActionName = "try again",
                            posAction = { dialog, which ->
                                dialog.dismiss()
                                getSources(category)
                            },
                            negActionName = "cancel",
                            negAction = { dialog, which ->
                                dialog.dismiss()
                            })
                    }

                }

                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                    binding.progressBar.isVisible = false
                    showAlertDialog(t.localizedMessage
                        ?: "something went wrong",
                        posActionName = "try again",
                        posAction = { dialog, which ->
                            dialog.dismiss()
                            getSources(category)
                        },
                        negActionName = "cancel",
                        negAction = { dialog, which ->
                            dialog.dismiss()
                        })
                }

            })
    }

    private fun bindTabs(sources: List<Source?>?) {
        if (sources == null)
            return
        sources.forEach { source ->
            val tab = binding.tabLayout.newTab()
            tab.text = source?.name
            binding.tabLayout.addTab(tab)
        }


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


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.search -> {
//                // Handle the search click here
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }


}