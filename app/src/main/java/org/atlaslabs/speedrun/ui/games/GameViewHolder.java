package org.atlaslabs.speedrun.ui.games;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.atlaslabs.speedrun.databinding.ViewListGameBinding;

class GameViewHolder extends RecyclerView.ViewHolder {
    public ViewListGameBinding binding;

    GameViewHolder(View view) {
        super(view);
        binding = ViewListGameBinding.bind(view);
    }
}