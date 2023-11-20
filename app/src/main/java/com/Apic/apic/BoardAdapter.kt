package com.Apic.apic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
/*
class BoardViewHolder(val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root)

class BoardAdapter (val context: Context, val itemList: MutableList<ItemBoardModel>): RecyclerView.Adapter<MyBoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBoardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyBoardViewHolder(ItemBoardBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyBoardViewHolder, position: Int) {
        val data = itemList.get(position) //itemModel에 있는 각각의 값

        holder.binding.run {
            itemEmailView.text=data.email
            itemDateView.text=data.date
            itemContentView.text=data.content
        }

        //스토리지 이미지 다운로드........................
        val imageRef = MyApplication.storage.reference.child("images/{${data.docId}}.jpg")
        imageRef.downloadUrl.addOnCompleteListener{task ->
            if (task.isSuccessful){
                //전달이 성공적으로 되었을 때
                // 다운로드 이미지를 ImageView에 보여줌
                GlideApp.with(context)
                    .load(task.result) //task가 가지고 있는 결과 로드
                    .into(holder.binding.itemImageView)
            }

        }

    }
}
*/