package com.thales.github.ui.pullresquest;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thales.github.R;
import com.thales.github.model.pullrequest.PullRequest;
import com.thales.github.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

class PullRequestAdapter extends RecyclerView.Adapter<PullRequestAdapter.ViewHolder> {
    private static final String BUNDLE_LIST_ITEM = "list-item";
    private final List<PullRequest> dataSet = new ArrayList<>();

    @NonNull
    private final RepositoryAdapterListener listener;

    interface RepositoryAdapterListener {
        void onClickListItem(PullRequest pullRequest);
    }

    PullRequestAdapter(@NonNull RepositoryAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pull_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.populate(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void addData(List<PullRequest> dataSet) {
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    void replaceData(List<PullRequest> dataSet) {
        setList(dataSet);
        notifyDataSetChanged();
    }

    private void setList(List<PullRequest> dataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
    }

    boolean hasItems() {
        return dataSet.size() > 0;
    }

    void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(BUNDLE_LIST_ITEM)) {
            ArrayList<PullRequest> dataSet = savedInstanceState
                    .getParcelableArrayList(BUNDLE_LIST_ITEM);

            replaceData(dataSet);
        }
    }

    void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_LIST_ITEM, (ArrayList<? extends Parcelable>) dataSet);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView dateView;
        private final TextView bodyView;
        private final ImageView avatarView;
        private final TextView login;
        private final View view;

        private PullRequest pullRequest;

        ViewHolder(View view) {
            super(view);
            this.view = view;

            titleView = (TextView) view.findViewById(R.id.title);
            dateView = (TextView) view.findViewById(R.id.date);
            bodyView = (TextView) view.findViewById(R.id.body);
            login = (TextView) view.findViewById(R.id.login);
            avatarView = (ImageView) view.findViewById(R.id.avatar);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pullRequest != null) {
                        listener.onClickListItem(pullRequest);
                    }
                }
            });
        }

        void populate(PullRequest data) {
            pullRequest = data;
            titleView.setText(data.title);
            dateView.setText(Utility.getFormattedDayMonthYear(data.date));
            bodyView.setText(data.body);
            login.setText(data.getLoginUser());

            Glide.with(view.getContext())
                    .load(data.getAvatarUrlUser())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(avatarView);
        }
    }
}
