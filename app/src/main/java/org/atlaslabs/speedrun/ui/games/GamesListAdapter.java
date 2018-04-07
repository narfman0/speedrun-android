package org.atlaslabs.speedrun.ui.games;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Game;

import java.util.List;

public class GamesListAdapter extends RecyclerView.Adapter<GameViewHolder> {
    private List<Game> games;

    public GamesListAdapter(List<Game> games) {
        this.games = games;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_game, parent, false);
        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        Game game = games.get(position);
        holder.binding.gameName.setText(game.getNames().getInternational());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}
