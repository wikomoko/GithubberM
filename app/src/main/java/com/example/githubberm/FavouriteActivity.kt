package com.example.githubberm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubberm.adapters.FavouriteAdapter
import com.example.githubberm.databinding.ActivityFavouriteBinding
import com.example.githubberm.room.Person
import com.example.githubberm.room.PersonDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteActivity : AppCompatActivity() {
    val db by lazy { PersonDB(this) }
    private lateinit var binding:ActivityFavouriteBinding
    private lateinit var adapter: FavouriteAdapter
    private val listItems = ArrayList<Person>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.liked_page)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerViewFavorite.setHasFixedSize(true)

        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(this)
        listItems.clear()

    }

    override fun onResume() {
        listItems.clear()
        CoroutineScope(Dispatchers.IO).launch {
            val x = db.personDao().getPersons()
            Log.d("ceker","dbrespons : $x")
            listItems.addAll(x)
        }
        binding.recyclerViewFavorite.adapter = FavouriteAdapter(listItems)
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}