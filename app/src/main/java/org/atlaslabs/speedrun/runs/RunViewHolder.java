package org.atlaslabs.speedrun.runs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.atlaslabs.speedrun.R;

public class RunViewHolder extends RecyclerView.ViewHolder{
    public TextView user, game, category, platform, time;

    public RunViewHolder(View view) {
        super(view);
        user = (TextView) view.findViewById(R.id.run_user);
        game = (TextView) view.findViewById(R.id.run_game);
        category = (TextView) view.findViewById(R.id.run_category);
        platform = (TextView) view.findViewById(R.id.run_platform);
        time = (TextView) view.findViewById(R.id.run_time);
    }
}