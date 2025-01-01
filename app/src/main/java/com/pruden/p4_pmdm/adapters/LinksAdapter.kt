package com.pruden.p4_pmdm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.CLIPBOARD_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.classes.Link

import com.pruden.p4_pmdm.databinding.ItemLinkBinding

class LinksAdapter (val listaLinks : MutableList<Link>) : RecyclerView.Adapter<LinksAdapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemLinkBinding.bind(view)
    }

    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_link, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int = listaLinks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val link = listaLinks[position]

        with(holder) {
            binding.link.text = " ${link.link}"
            binding.eco.text = link.eco

            binding.link.setOnLongClickListener {
                val clipboard = contexto.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("Link", link.link)
                clipboard.setPrimaryClip(clip)
                true
            }
        }
    }

}