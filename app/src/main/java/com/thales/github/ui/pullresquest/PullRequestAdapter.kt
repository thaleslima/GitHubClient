package com.thales.github.ui.pullresquest

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
import com.thales.github.model.pullrequest.PullRequest
import com.thales.github.utilities.Utility

import java.util.ArrayList

internal class PullRequestAdapter(private val listener: PullRequestAdapter.RepositoryAdapterListener) : RecyclerView.Adapter<PullRequestAdapter.ViewHolder>() {
    private val dataSet = ArrayList<PullRequest>()

    internal interface RepositoryAdapterListener {
        fun onClickListItem(pullRequest: PullRequest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_pull_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populate(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun addData(dataSet: List<PullRequest>) {
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

    fun replaceData(dataSet: List<PullRequest>) {
        setList(dataSet)
        notifyDataSetChanged()
    }

    private fun setList(dataSet: List<PullRequest>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
    }

    fun hasItems(): Boolean {
        return dataSet.size > 0
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(BUNDLE_LIST_ITEM)) {
            val dataSet = savedInstanceState
                    .getParcelableArrayList<PullRequest>(BUNDLE_LIST_ITEM)

            replaceData(dataSet)
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(BUNDLE_LIST_ITEM, dataSet as ArrayList<out Parcelable>)
    }

    internal inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val titleView: TextView = view.findViewById(R.id.title) as TextView
        private val dateView: TextView = view.findViewById(R.id.date) as TextView
        private val bodyView: TextView = view.findViewById(R.id.body) as TextView
        private val avatarView: ImageView = view.findViewById(R.id.avatar) as ImageView
        private val login: TextView = view.findViewById(R.id.login) as TextView

        private var pullRequest: PullRequest? = null

        init {
            view.setOnClickListener {
                if (pullRequest != null) {
                    listener.onClickListItem(pullRequest!!)
                }
            }
        }

        fun populate(data: PullRequest)  = with(itemView){
            pullRequest = data
            titleView.text = data.title
            dateView.text = Utility.getFormattedDayMonthYear(data.date!!)
            bodyView.text = data.body
            login.text = data.loginUser

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
