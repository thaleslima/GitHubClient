package com.thales.github.ui.repository;

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
import com.thales.github.model.repository.Repository;

import java.util.ArrayList;
import java.util.List;

class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {
    private static final String BUNDLE_LIST_ITEM = "list-item";
    private final List<Repository> dataSet = new ArrayList<>();

    @NonNull
    private final RepositoryAdapterListener listener;

    interface RepositoryAdapterListener {
        void onClickListItem(Repository repository);
    }

    RepositoryAdapter(@NonNull RepositoryAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_repository, parent, false);
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

    boolean hasItems() {
        return dataSet.size() > 0;
    }

    void addData(List<Repository> dataSet) {
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    private void replaceData(List<Repository> dataSet) {
        setList(dataSet);
        notifyDataSetChanged();
    }

    private void setList(List<Repository> dataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(dataSet);
    }

    void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(BUNDLE_LIST_ITEM)) {
            ArrayList<Repository> dataSet = savedInstanceState
                    .getParcelableArrayList(BUNDLE_LIST_ITEM);

            replaceData(dataSet);
        }
    }

    void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_LIST_ITEM, (ArrayList<? extends Parcelable>) dataSet);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView descriptionView;
        private final TextView forksCountView;
        private final TextView starsCountView;
        private final TextView loginView;
        private final ImageView avatarView;
        private final View view;

        private Repository repository;


        ViewHolder(View view) {
            super(view);
            this.view = view;
            nameView = (TextView) view.findViewById(R.id.name);
            descriptionView = (TextView) view.findViewById(R.id.description);
            forksCountView = (TextView) view.findViewById(R.id.forks_count);
            starsCountView = (TextView) view.findViewById(R.id.stars_count);
            loginView = (TextView) view.findViewById(R.id.login);
            avatarView = (ImageView) view.findViewById(R.id.avatar);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (repository != null) {
                        listener.onClickListItem(repository);
                    }
                }
            });
        }

        void populate(Repository data) {
            repository = data;
            nameView.setText(data.name);
            descriptionView.setText(data.description);
            forksCountView.setText(String.valueOf(data.forksCount));
            starsCountView.setText(String.valueOf(data.starsCount));
            loginView.setText(data.getLoginUser());

            Glide.with(view.getContext())
                    .load(data.getAvatarUrlUser())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(avatarView);
        }
    }
}
