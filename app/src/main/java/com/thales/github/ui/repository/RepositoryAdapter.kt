package com.thales.github.ui.repository

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thales.github.R
import com.thales.github.model.repository.Repository
import com.thales.github.utilities.inflate

import java.util.ArrayList

internal class RepositoryAdapter(private val listener: RepositoryAdapter.RepositoryAdapterListener) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {
    private val dataSet = ArrayList<Repository>()

    internal interface RepositoryAdapterListener {
        fun onClickListItem(repository: Repository)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.list_item_repository)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populate(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun hasItems(): Boolean {
        return dataSet.size > 0
    }

    fun addData(dataSet: List<Repository>) {
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

    private fun replaceData(dataSet: List<Repository>) {
        setList(dataSet)
        notifyDataSetChanged()
    }

    private fun setList(dataSet: List<Repository>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(BUNDLE_LIST_ITEM)) {
            val dataSet = savedInstanceState
                    .getParcelableArrayList<Repository>(BUNDLE_LIST_ITEM)

            replaceData(dataSet)
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(BUNDLE_LIST_ITEM, dataSet as ArrayList<out Parcelable>)
    }

    internal inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val nameView: TextView = view.findViewById(R.id.name) as TextView
        private val descriptionView: TextView = view.findViewById(R.id.description) as TextView
        private val forksCountView: TextView = view.findViewById(R.id.forks_count) as TextView
        private val starsCountView: TextView = view.findViewById(R.id.stars_count) as TextView
        private val loginView: TextView = view.findViewById(R.id.login) as TextView
        private val avatarView: ImageView = view.findViewById(R.id.avatar) as ImageView

        private var repository: Repository? = null


        init {
            view.setOnClickListener {
                if (repository != null) {
                    listener.onClickListItem(repository!!)
                }
            }
        }

        fun populate(data: Repository) {
            repository = data
            nameView.text = data.name
            descriptionView.text = data.description
            forksCountView.text = data.forksCount.toString()
            starsCountView.text = data.starsCount.toString()
            loginView.text = data.loginUser

            Glide.with(view.context)
                    .load(data.avatarUrlUser)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(avatarView)
        }
    }

    companion object {
        private val BUNDLE_LIST_ITEM = "list-item"
    }
}
