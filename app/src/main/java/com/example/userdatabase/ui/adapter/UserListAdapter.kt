package com.example.userdatabase.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.userdatabase.databinding.ListItemUserBinding
import com.example.userdatabase.model.User

class UserListAdapter(
    private val clickListener: (User) -> Unit
) : ListAdapter<User, UserListAdapter.UserViewHolder>(DiffCallback) {

    class UserViewHolder(private var binding: ListItemUserBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(user: User) {
                binding.user = user
                binding.executePendingBindings()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(
            ListItemUserBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(user)
        }
        holder.bind(user)
    }

    companion object DiffCallback: DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }

}