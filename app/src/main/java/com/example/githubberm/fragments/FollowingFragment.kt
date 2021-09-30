package com.example.githubberm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.githubberm.InfoDetailsActivity
import com.example.githubberm.R
import com.example.githubberm.adapters.FollowingFragmentAdapter
import com.example.githubberm.databinding.FragmentFollowingBinding
import com.example.githubberm.dataclass.FollowingFragmentUserDatas
import com.example.githubberm.dataclass.GithubUserDatas
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject


class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private val listItems = ArrayList<FollowingFragmentUserDatas>()
    private lateinit var adapter: FollowingFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dataFromInfoDetailsActivity =
            activity?.intent?.getParcelableExtra<GithubUserDatas>(InfoDetailsActivity.data_reciever) as GithubUserDatas

        connectToGithubApi(dataFromInfoDetailsActivity.userLogin)

        binding.recyclerViewForCardFollowingFragment.setHasFixedSize(true)
        adapter = FollowingFragmentAdapter()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun connectToGithubApi(who: String?) {
        binding.loadingBarFragmentSecond.visibility = View.VISIBLE

        val githubApiKey = "token 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"
        val url = "https://api.github.com/users/$who/following"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", githubApiKey)
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONArray(result)

                    for (i in 0 until responseObject.length()) {

                        val objetcFromArray = responseObject.getJSONObject(i)
                        val loginName = objetcFromArray.getString("login")
                        findingUserDetails(loginName)
                    }

                } catch (e: Exception) {
                    showWarningMessage(e.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : ${resources.getString(R.string.bad_request)}"
                    403 -> "$statusCode : ${resources.getString(R.string.forbidden)}"
                    404 -> "$statusCode : ${resources.getString(R.string.not_found)}"
                    else -> "$statusCode : ${error.message}"
                }
                binding.loadingBarFragmentSecond.visibility = View.GONE

                showWarningMessage(errorMessage)
            }

        })

    }

    private fun findingUserDetails(findGithubUser: String) {
        binding.loadingBarFragmentSecond.visibility = View.VISIBLE

        val githubApiKey = "token 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"
        val url = "https://api.github.com/users/$findGithubUser"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", githubApiKey)
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    Log.d("conn", "success")
                    Log.i("gett", String(responseBody))

                    val result = String(responseBody)
                    val responseObject = JSONObject(result)

                    val userLoginData = responseObject.getString("login")
                    val userNameData = responseObject.getString("name")
                    val userAvatarData = responseObject.getString("avatar_url")
                    val userCompanyData = responseObject.getString("company")
                    val userLocationData = responseObject.getString("location")
                    val userRepositoryData = responseObject.getString("public_repos")
                    val userFollowersData = responseObject.getString("followers")
                    val userFollowingData = responseObject.getString("following")


                    listItems.add(
                        FollowingFragmentUserDatas(
                            userLoginData.toString(),
                            userNameData.toString(),
                            userAvatarData.toString(),
                            userCompanyData.toString(),
                            userLocationData.toString(),
                            userRepositoryData.toString(),
                            userFollowersData.toString(),
                            userFollowingData.toString()

                        )
                    )
                    adapter.setDatas(listItems)
                    turnOnRecycler()
                    binding.loadingBarFragmentSecond.visibility = View.GONE

                } catch (e: Exception) {
                    showWarningMessage(e.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : ${resources.getString(R.string.bad_request)}"
                    403 -> "$statusCode : ${resources.getString(R.string.forbidden)}"
                    404 -> "$statusCode : ${resources.getString(R.string.not_found)}"
                    else -> "$statusCode : ${error.message}"
                }
                binding.loadingBarFragmentSecond.visibility = View.GONE
                showWarningMessage(errorMessage)
            }

        })
    }


    private fun turnOnRecycler() {
        binding.recyclerViewForCardFollowingFragment.layoutManager =
            LinearLayoutManager(this.context)
        binding.recyclerViewForCardFollowingFragment.adapter = adapter
    }

    private fun showWarningMessage(messasge: String) {
        SweetAlertDialog(this.context, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("$messasge\n")
            .setConfirmText(resources.getString(R.string.understand))
            .show()
    }
}