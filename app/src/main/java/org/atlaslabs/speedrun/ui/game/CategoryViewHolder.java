package org.atlaslabs.speedrun.ui.game;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.atlaslabs.speedrun.databinding.ViewListCategoryBinding;

class CategoryViewHolder extends RecyclerView.ViewHolder {
    public ViewListCategoryBinding binding;

    CategoryViewHolder(View view) {
        super(view);
        binding = ViewListCategoryBinding.bind(view);
    }
}
