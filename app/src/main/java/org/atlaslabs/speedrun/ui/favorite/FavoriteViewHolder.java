package org.atlaslabs.speedrun.ui.favorite;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.atlaslabs.speedrun.databinding.ViewListFavoriteBinding;

class FavoriteViewHolder extends RecyclerView.ViewHolder {
    public ViewListFavoriteBinding binding;

    FavoriteViewHolder(View view) {
        super(view);
        binding = ViewListFavoriteBinding.bind(view);
    }
}