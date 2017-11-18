package org.atlaslabs.speedrun.ui.runs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.atlaslabs.speedrun.R;

public class RunViewHolder extends RecyclerView.ViewHolder {
    public TextView user, game, category, platform, time;

    public RunViewHolder(View view) {
        super(view);
        user = view.findViewById(R.id.run_user);
        game = view.findViewById(R.id.run_game);
        category = view.findViewById(R.id.run_category);
        platform = view.findViewById(R.id.run_platform);
        time = view.findViewById(R.id.run_time);
    }
}