package com.sqlitedemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_row.view.*

class ItemAdapter(val context: Context, val items: ArrayList<EmpModelClass>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.items_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.tvName.text = item.name
        holder.tvEmail.text = item.email

        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGray))
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        }

        holder.ivEdit.setOnClickListener { view ->

            if (context is MainActivity) {
                context.updateRecordDialog(item)
            }
        }

        holder.ivDelete.setOnClickListener { view ->

            if (context is MainActivity) {
                context.deleteRecordAlertDialog(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llMain = view.llMain
        val tvName = view.tvName
        val tvEmail = view.tvEmail
        val ivEdit = view.ivEdit
        val ivDelete = view.ivDelete
    }
}