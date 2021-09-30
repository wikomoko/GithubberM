package com.example.githubberm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.githubberm.adapters.TabLayoutPagerAdapter
import com.example.githubberm.databinding.ActivityInfoDetailsBinding
import com.example.githubberm.dataclass.GithubUserDatas
import com.example.githubberm.room.Person
import com.example.githubberm.room.PersonDB
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class InfoDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoDetailsBinding

    lateinit var sharedPref:SharedPreferences

    val db by lazy { PersonDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val member = intent.getParcelableExtra<GithubUserDatas>(data_reciever) as GithubUserDatas

        Picasso.get().load(member.userAvatar).into(binding.userDetailImage)
        binding.userUserName.text = member.userLogin
        binding.userDetailName.text = member.userName
        binding.userDetailRepository.text = member.userRepository
        binding.userDetailLocation.text = member.userLocation
        binding.userDetailCompany.text = member.userCompany
        binding.userDetailFollower.text = member.userFollowers
        binding.userDetailFollowing.text = member.userFollowing

        supportActionBar?.title = member.userLogin

        val sectionsPagerAdapter = TabLayoutPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.loadingbarDetails.visibility = View.GONE

        sharedPref = getSharedPreferences(member.userLogin,Context.MODE_PRIVATE)
        val readingShared = sharedPref.getBoolean("likedStatus",false)

        if(readingShared == true) {
            binding.haleBtn.visibility = View.VISIBLE
            binding.loveBtn.visibility = View.GONE
        }else{
            binding.haleBtn.visibility = View.GONE
            binding.loveBtn.visibility = View.VISIBLE
        }

        binding.loveBtn.setOnClickListener {
            binding.haleBtn.visibility = View.VISIBLE
            binding.loveBtn.visibility = View.GONE
            Toast.makeText(this, resources.getString(R.string.isLike), Toast.LENGTH_SHORT).show()
            val setStatusLiked = sharedPref.edit().putBoolean("likedStatus",true)
            setStatusLiked.apply()
            CoroutineScope(Dispatchers.IO).launch {
                db.personDao().addPerson(
                   Person(
                       member.userLogin,
                       member.userName,
                       member.userAvatar,
                       member.userCompany,
                       member.userLocation,
                       member.userRepository,
                       member.userFollowers,
                       member.userFollowing
                   )
                )
            }
        }

        binding.haleBtn.setOnClickListener {
            binding.haleBtn.visibility = View.GONE
            binding.loveBtn.visibility = View.VISIBLE
            Toast.makeText(this, resources.getString(R.string.disliked), Toast.LENGTH_SHORT).show()
            val setStatusLiked = sharedPref.edit().putBoolean("likedStatus",false)
            setStatusLiked.apply()
            CoroutineScope(Dispatchers.IO).launch {
                db.personDao().removePerson(member.userLogin)
            }

        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menus, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.set_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            R.id.btn_share -> {
                val memberS =
                    intent.getParcelableExtra<GithubUserDatas>(data_reciever) as GithubUserDatas
                val sharingDatas: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT, "${resources.getString(R.string.this_is_data_of)}" +
                                "\n\n ${resources.getString(R.string.nameShare)} ${memberS.userName}" +
                                "\n\n ${resources.getString(R.string.userSare)} ${memberS.userLogin}" +
                                "\n\n ${resources.getString(R.string.workedShare)} ${memberS.userCompany}" +
                                "\n\n ${resources.getString(R.string.hasFollowers)} ${memberS.userFollowers}" +
                                "\n\n ${resources.getString(R.string.hasRepo)} ${memberS.userRepository} ${resources.getString(R.string.repository)}"
                    )
                    type = "text/plain"
                }

                val shareDatas = Intent.createChooser(sharingDatas, null)
                startActivity(shareDatas)

                return true
            }
            else -> return true
        }
    }

    companion object {
        const val data_reciever = "reciever"

        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

}