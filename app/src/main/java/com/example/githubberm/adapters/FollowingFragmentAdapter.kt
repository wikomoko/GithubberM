package com.example.githubberm.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubberm.R
import com.example.githubberm.databinding.CardListItemsBinding
import com.example.githubberm.dataclass.FollowingFragmentUserDatas
import com.squareup.picasso.Picasso

class FollowingFragmentAdapter :
    RecyclerView.Adapter<FollowingFragmentAdapter.FollowingFragmentViewHolder>() {

    private var containerOfDatas = ArrayList<FollowingFragmentUserDatas>()
    private var customCardColors = arrayListOf("#FFC542", "#FF565E", "#3ED598")

    fun setDatas(items: ArrayList<FollowingFragmentUserDatas>) {
        containerOfDatas.clear()
        containerOfDatas.addAll(items)
    }

    inner class FollowingFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardListItemsBinding.bind(itemView)
        val cardColor = binding.userCardPad

        fun bind(followingFragmentUserDatas: FollowingFragmentUserDatas) {
            with(itemView) {
                Picasso.get().load(followingFragmentUserDatas.userAvatar)
                    .into(binding.userCardListImage)
                binding.userCardListUsername.text = followingFragmentUserDatas.userLogin
                binding.userCardListName.text = followingFragmentUserDatas.userName
                binding.userCardListCompany.text = followingFragmentUserDatas.userCompany
                binding.userCardListFollowers.text = followingFragmentUserDatas.userFollowers
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingFragmentViewHolder {
        val boxView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_list_items, parent, false)
        return FollowingFragmentViewHolder(boxView)
    }

    override fun onBindViewHolder(holder: FollowingFragmentViewHolder, position: Int) {
        if (position % 4 == 0) {
            holder.cardColor.setCardBackgroundColor(Color.parseColor(customCardColors[0]))
        } else if (position % 4 == 1) {
            holder.cardColor.setCardBackgroundColor(Color.parseColor(customCardColors[1]))
        } else if (position % 4 == 2) {
            holder.cardColor.setCardBackgroundColor(Color.parseColor(customCardColors[2]))
        }
        holder.bind(containerOfDatas[position])
    }

    override fun getItemCount(): Int = containerOfDatas.size
}