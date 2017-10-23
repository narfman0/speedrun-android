package org.atlaslabs.speedrun.ui.runs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Category;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.models.Platform;
import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.models.User;
import org.atlaslabs.speedrun.util.Utils;

import java.util.List;

import io.realm.Realm;

public class RecentRunsListAdapter extends RecyclerView.Adapter<RunViewHolder>{
    private List<Run> runs;

    public RecentRunsListAdapter(List<Run> runs) {
        this.runs = runs;
    }

    @Override
    public RunViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_run, parent, false);
        return new RunViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RunViewHolder holder, int position) {
        Run run = runs.get(position);
        Realm realm = Realm.getDefaultInstance();
        try {
            Game.getOrFetch(realm, run.getGame(), (game) ->
                    Utils.runOnUIThread(() ->
                            holder.game.setText(game.getNames().getInternational())
                    )
            );
            for(User player : run.getPlayers()) {
                User.getOrFetch(realm, player.getId(), (user) ->
                        Utils.runOnUIThread(() ->
                                holder.user.setText(user.getNames().getInternational())
                        )
                );
            }
            Platform.getOrFetch(realm, run.getSystem().getPlatform(), (platform) ->
                    Utils.runOnUIThread(() ->
                            holder.platform.setText(platform.getName())
                    )
            );
            Category.getOrFetch(realm, run.getCategory(), (category) ->
                    Utils.runOnUIThread(() ->
                            holder.category.setText(category.getName())
                    )
            );
        } finally {
            realm.close();
        }
        holder.time.setText(run.getTimes().getPrimaryTime());
    }

    @Override
    public int getItemCount() {
        return runs.size();
    }
}
