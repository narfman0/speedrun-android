package org.atlaslabs.speedrun.ui.favorite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Category;
import org.atlaslabs.speedrun.models.Favorite;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.models.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;

class FavoriteAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {
    private final PublishSubject<Favorite> onClickSubject = PublishSubject.create();
    private final List<Favorite> favorites;

    FavoriteAdapter(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_favorite, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite favorite = favorites.get(position);
        Realm realm = Realm.getDefaultInstance();
        switch (favorite.getType()) {
            case CATEGORY:
                Category category = Category.get(realm, favorite.getId());
                holder.binding.favoriteName.setText(category.getName());
                break;
            case GAME:
                Game game = Game.get(realm, favorite.getId());
                holder.binding.favoriteName.setText(game.getNames().getInternational());
                break;
            case USER:
                User user = User.get(realm, favorite.getId());
                holder.binding.favoriteName.setText(user.getNamePretty());
                break;
        }
        realm.close();
        holder.binding.favoriteType.setText(favorite.getType().name());
        holder.itemView.setOnClickListener(v -> onClickSubject.onNext(favorite));
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public Observable<Favorite> getClicked(){
        return onClickSubject;
    }
}