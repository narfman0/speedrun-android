package org.atlaslabs.speedrun.runs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.models.Run;
import org.atlaslabs.speedrun.models.User;

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
            Game game = Game.getByID(run.getGame(), realm);
            User user = User.getByID(run.getUser(), realm);
            holder.game.setText(game == null ? run.getGame() : game.getNames().getInternational());
            holder.user.setText(user == null ? run.getUser() : user.getNames().getInternational());
        } finally {
            realm.close();
        }
        holder.category.setText(run.getCategory());
        holder.platform.setText(run.getSystem().getPlatform());
        holder.time.setText(run.getTimes().getPrimaryTime());
    }

    @Override
    public int getItemCount() {
        return runs.size();
    }
}
