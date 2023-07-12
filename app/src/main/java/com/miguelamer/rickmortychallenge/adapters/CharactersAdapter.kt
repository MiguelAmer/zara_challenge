package com.miguelamer.rickmortychallenge.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miguelamer.rickmortychallenge.CharacterResult
import com.miguelamer.rickmortychallenge.databinding.CharacterItemLayoutBinding

class CharactersAdapter: RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    inner class CharacterViewHolder(private val itemBinding: CharacterItemLayoutBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(character: CharacterResult) {
            with (itemBinding) {
                characterName.text = character.name
                characterGender.text = character.gender
                characterSpecies.text = character.species
                Glide.with(itemBinding.root).load(character.image).into(characterImage)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CharacterResult>() {
        override fun areItemsTheSame(oldItem: CharacterResult, newItem: CharacterResult): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CharacterResult, newItem: CharacterResult): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val itemBinding = CharacterItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = differ.currentList[position]

        holder.bind(character)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(character) }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((CharacterResult) -> Unit)? = null

    fun setOnClickListener(listener: (CharacterResult) -> Unit) {
        onItemClickListener = listener
    }
}