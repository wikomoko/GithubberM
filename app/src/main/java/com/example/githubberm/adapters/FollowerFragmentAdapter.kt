package com.example.githubberm.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubberm.R
import com.example.githubberm.databinding.CardListItemsBinding
import com.example.githubberm.dataclass.FollowerFragmentUserDatas
import com.squareup.picasso.Picasso

class FollowerFragmentAdapter :
    RecyclerView.Adapter<FollowerFragmentAdapter.FollowerFragmentViewHolder>() {

    private var containerOfDatas = ArrayList<FollowerFragmentUserDatas>()
    private var customCardColors = arrayListOf("#FFC542", "#FF565E", "#3ED598")

    fun setDatas(items: ArrayList<FollowerFragmentUserDatas>) {
        containerOfDatas.clear()
        containerOfDatas.addAll(items)
    }

    inner class FollowerFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardListItemsBinding.bind(itemView)
        val cardColor = binding.userCardPad

        fun bind(followerFragmentUserDatas: FollowerFragmentUserDatas) {
            with(itemView) {
                Picasso.get().load(followerFragmentUserDatas.userAvatar)
                    .into(binding.userCardListImage)
                binding.userCardListUsername.text = followerFragmentUserDatas.userLogin
                binding.userCardListName.text = followerFragmentUserDatas.userName
                binding.userCardListCompany.text = followerFragmentUserDatas.userCompany
                binding.userCardListFollowers.text = followerFragmentUserDatas.userFollowers
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerFragmentViewHolder {
        val boxView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_list_items, parent, false)
        return FollowerFragmentViewHolder(boxView)
    }

    override fun onBindViewHolder(holder: FollowerFragmentViewHolder, position: Int) {
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