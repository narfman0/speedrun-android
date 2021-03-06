package org.atlaslabs.speedrun.ui.runs;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.Realm;

public class RecentRunsListAdapter extends RecyclerView.Adapter<RunViewHolder> {
    private static final String TAG = RecentRunsListAdapter.class.getSimpleName();
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
        holder.binding.runGame.setText("");
        holder.binding.runUser.setText("");
        holder.binding.runPlatform.setText("");
        holder.binding.runCategory.setText("");
        try {
            Game.getOrFetch(realm, run.getGame())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((game) -> holder.binding.runGame.setText(game.getNames().getInternational()));
            for (String userID : run.getPlayersIDs()) {
                User.getOrFetch(realm, userID)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((user) -> {
                            boolean nameAccessible = user != null && user.getId() != null && user.getNames() != null;
                            if (nameAccessible)
                                holder.binding.runUser.setText(user.getNames().getInternational());
                            else
                                Log.w(TAG, "User name inaccessible for run: " + run.getID());
                            holder.binding.runUser.setVisibility(nameAccessible ? View.VISIBLE : View.GONE);
                        });
                break;
            }
            if (run.getSystem().getPlatform() != null)
                Platform.getOrFetch(realm, run.getSystem().getPlatform())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((platform) -> holder.binding.runPlatform.setText(platform.getName()));
            else
                Log.i(TAG, "No platform given for run: " + run.toString());
            if (run.getCategory() != null)
                Category.getOrFetch(realm, run.getCategory())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((category) -> holder.binding.runCategory.setText(category.getName()));
            else
                Log.i(TAG, "No category given for run: " + run.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error populating list item position: " + position + " exception: " + e.toString());
        } finally {
            realm.close();
        }
        holder.binding.runTime.setText(Utils.timePretty(run.getTimes().getPrimaryTime()));
    }

    @Override
    public int getItemCount() {
        return runs.size();
    }
}
