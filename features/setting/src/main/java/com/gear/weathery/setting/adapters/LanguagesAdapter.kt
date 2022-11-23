package com.gear.weathery.setting.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gear.weathery.setting.databinding.ItemLanguageBinding
import com.gear.weathery.setting.util.Language

class LanguagesAdapter(val onLangSelected:(Language)->Unit) :ListAdapter<Language,LanguagesAdapter.LangViewHolder>(LangDiff){
    var selectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LangViewHolder {
        return LangViewHolder(ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: LangViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LangViewHolder(private val binding: ItemLanguageBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(lang: Language) {
            binding.apply {
                tvLocale.text = lang.locale
                tvLanguagevalue.text = lang.lang
                rBtnSelectLang.isChecked= adapterPosition == selectedPosition

                val checkListener = View.OnClickListener {
                    selectedPosition = adapterPosition
                    notifyDataSetChanged()
                    onLangSelected(lang)
                    notifyItemChanged(selectedPosition)
                }
                rBtnSelectLang.setOnClickListener(checkListener)
                root.setOnClickListener(checkListener)
            }

            }
        }
    object LangDiff:DiffUtil.ItemCallback<Language>() {
        override fun areItemsTheSame(oldItem: Language, newItem: Language): Boolean {
            return oldItem.lang == newItem.lang
        }

        override fun areContentsTheSame(oldItem: Language, newItem: Language): Boolean {
            return oldItem == newItem
        }

    }
}
