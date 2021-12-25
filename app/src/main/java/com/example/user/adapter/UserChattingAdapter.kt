package com.example.user.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.user.R
import com.example.user.databinding.ItemUserChattingBinding
import com.example.user.entity.UserChatAddEntity


class UserChattingAdapter : RecyclerView.Adapter<UserChattingAdapter.ViewHolder>() {

    private var messageList = mutableListOf<UserChatAddEntity>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserChattingBinding.bind(itemView)
        fun bind(curentMessage: UserChatAddEntity) {
            if(curentMessage.user.isNotEmpty())
            {
                binding.sendMessage.text=curentMessage.user
                binding.comeMessageLayout.visibility=View.GONE
            }else
            {
                binding.comeMessage.text=curentMessage.admin
                binding.sendMessageLayout.visibility=View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_chatting, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount(): Int = messageList.size

    fun sendData(list: MutableList<UserChatAddEntity>) {
        messageList=list
        notifyDataSetChanged()
        notifyItemInserted(messageList.size)
    }

}