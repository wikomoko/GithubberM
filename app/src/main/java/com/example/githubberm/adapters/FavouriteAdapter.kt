package com.example.githubberm.adapters

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.githubberm.InfoDetailsActivity
import com.example.githubberm.R
import com.example.githubberm.databinding.CardListItemsBinding
import com.example.githubberm.dataclass.GithubUserDatas
import com.example.githubberm.room.Person
import com.squareup.picasso.Picasso

class FavouriteAdapter(val listItems: ArrayList<Person>) :RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {
    private var customCardColors = arrayListOf<String>("#FFC542", "#FF565E", "#3ED598")


    inner class FavouriteViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardListItemsBinding.bind(itemView)
        val cardColor = binding.userCardPad

        fun bind(githubUserDatas: Person) {
            with(itemView) {
                Picasso.get().load(githubUserDatas.userAvatar).into(binding.userCardListImage)
                binding.userCardListUsername.text = githubUserDatas.userLogin
                binding.userCardListName.text = githubUserDatas.userName
                binding.userCardListCompany.text = githubUserDatas.userCompany
                binding.userCardListFollowers.text = githubUserDatas.userFollowers

                setOnClickListener {
                    val person = GithubUserDatas(
                        githubUserDatas.userLogin,
                        githubUserDatas.userName,
                        githubUserDatas.userAvatar,
                        githubUserDatas.userCompany,
                        githubUserDatas.userLocation,
                        githubUserDatas.userRepository,
                        githubUserDatas.userFollowers,
                        githubUserDatas.userFollowing
                    )
                    val moveWithObjectIntent = Intent(this.context, InfoDetailsActivity::class.java)
                    moveWithObjectIntent.putExtra(InfoDetailsActivity.data_reciever, person)
                    itemView.context.startActivity(moveWithObjectIntent)
                    Toast.makeText(this.context, githubUserDatas.userLogin, Toast.LENGTH_SHORT)
                        .show()
                }

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val boxView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_list_items, parent, false)
        return FavouriteViewHolder(boxView)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        if (position % 4 == 0) {
            holder.cardColor.setCardBackgroundColor(Color.parseColor(customCardColors[0]))
        } else if (position % 4 == 1) {
            holder.cardColor.setCardBackgroundColor(Color.parseColor(customCardColors[1]))
        } else if (position % 4 == 2) {
            holder.cardColor.setCardBackgroundColor(Color.parseColor(customCardColors[2]))
        }


        holder.bind(listItems[position])
    }

    override fun getItemCount(): Int = listItems.size
}