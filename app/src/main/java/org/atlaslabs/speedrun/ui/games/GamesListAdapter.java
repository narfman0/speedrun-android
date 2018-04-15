package org.atlaslabs.speedrun.ui.games;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Game;

import java.util.List;

class GamesListAdapter extends RecyclerView.Adapter<GameViewHolder> {
    private List<Game> games;

    GamesListAdapter(List<Game> games) {
        this.games = games;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_game, parent, false);
        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = games.get(position);
        holder.binding.gameName.setText(game.getNames().getInternational());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public Game getItem(int index) {
        return games.get(index);
    }
}
