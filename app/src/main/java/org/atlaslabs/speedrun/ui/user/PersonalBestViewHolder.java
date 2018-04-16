package org.atlaslabs.speedrun.ui.user;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.atlaslabs.speedrun.databinding.ViewListPbBinding;

class PersonalBestViewHolder extends RecyclerView.ViewHolder {
    public ViewListPbBinding binding;

    PersonalBestViewHolder(View view) {
        super(view);
        binding = ViewListPbBinding.bind(view);
    }
}