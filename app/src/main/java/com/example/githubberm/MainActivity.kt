package com.example.githubberm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.githubberm.adapters.UserMainDataAdapter
import com.example.githubberm.databinding.ActivityMainBinding
import com.example.githubberm.dataclass.GithubUserDatas
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val listItems = ArrayList<GithubUserDatas>()
    private lateinit var adapter: UserMainDataAdapter
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("theme_mode", Context.MODE_PRIVATE)
        val readingShared = sharedPref.getBoolean("theme_stat", false)

        if (readingShared == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        connectToGithubApi(null)
        binding.recyclerViewForCard.setHasFixedSize(true)
        adapter = UserMainDataAdapter()

        binding.searchComponent.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                listItems.clear()
                connectToGithubApi(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        binding.btnRefresh.setOnClickListener {
            connectToGithubApi(null)
            binding.searchComponent.setQuery(null, false)
        }

        binding.btnLanguage.setOnClickListener {
            val mIntent = Intent(this, SettingActivity::class.java)
            startActivity(mIntent)
        }

        binding.btnLovePage.setOnClickListener {
            val intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
        }


    }


    private fun connectToGithubApi(who: String?) {
        binding.loadingBar.visibility = View.VISIBLE
        binding.suggestText.visibility = View.GONE
        binding.btnRefresh.visibility = View.GONE
        val githubApiKey = "token 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"
        val url: String
        if (who != null) {
            url = "https://api.github.com/search/users?q=$who"
        } else {
            url = "https://api.github.com/users"
        }

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
                    if (who != null) {
                        val responseObject = JSONObject(result)
                        val items = responseObject.getJSONArray("items")
                        val nothing = responseObject.getInt("total_count")
                        if (nothing == 0) {
                            listItems.clear()
                            adapter.setDatas(listItems)
                            showWarningMessage(resources.getString(R.string.not_found))
                            binding.suggestText.visibility = View.VISIBLE
                            binding.btnRefresh.visibility = View.VISIBLE
                        }
                        for (i in 0 until items.length()) {

                            val objetcFromArray = items.getJSONObject(i)
                            val loginName = objetcFromArray.getString("login")
                            findingUserDetails(loginName)
                        }

                    } else {
                        val jsonArray = JSONArray(result)

                        for (i in 0 until jsonArray.length()) {

                            val jsonObject = jsonArray.getJSONObject(i)
                            val loginName: String = jsonObject.getString("login")

                            findingUserDetails(loginName)
                        }
                    }
                    Log.d("checking", result)

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_SHORT).show()
                }
                binding.loadingBar.visibility = View.GONE
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
                binding.loadingBar.visibility = View.GONE

                showWarningMessage(errorMessage)
            }

        })

    }

    private fun findingUserDetails(findGithubUser: String) {
        binding.loadingBar.visibility = View.VISIBLE

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
                        GithubUserDatas(
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
                    binding.loadingBar.visibility = View.GONE

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
                binding.loadingBar.visibility = View.GONE
                showWarningMessage(errorMessage)
            }

        })

    }

    private fun turnOnRecycler() {
        binding.recyclerViewForCard.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewForCard.adapter = adapter
    }

    private fun showWarningMessage(messasge: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("$messasge\n")
            .setConfirmText(resources.getString(R.string.understand))
            .show()
    }

}