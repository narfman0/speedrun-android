package org.atlaslabs.speedrun.ui.runs;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.atlaslabs.speedrun.databinding.ViewListRunBinding;

public class RunViewHolder extends RecyclerView.ViewHolder {
    public ViewListRunBinding binding;

    public RunViewHolder(View view) {
        super(view);
        binding = ViewListRunBinding.bind(view);
    }
}