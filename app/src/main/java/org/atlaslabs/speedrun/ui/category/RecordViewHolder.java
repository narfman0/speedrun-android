package org.atlaslabs.speedrun.ui.category;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.atlaslabs.speedrun.databinding.ViewListRecordBinding;

class RecordViewHolder extends RecyclerView.ViewHolder {
    public ViewListRecordBinding binding;

    RecordViewHolder(View view) {
        super(view);
        binding = ViewListRecordBinding.bind(view);
    }
}
