package org.atlaslabs.speedrun.ui.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.ui.favorite.FavoriteActivity;
import org.atlaslabs.speedrun.ui.games.GamesActivity;
import org.atlaslabs.speedrun.ui.runs.RecentRunActivity;

import io.reactivex.disposables.CompositeDisposable;

public abstract class AbstractActivity extends AppCompatActivity {
    protected CompositeDisposable disposable;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorites:
                startActivity(new Intent(this, FavoriteActivity.class));
                return true;
            case R.id.menu_recent:
                startActivity(new Intent(this, RecentRunActivity.class));
                return true;
            case R.id.menu_games:
                startActivity(new Intent(this, GamesActivity.class));
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposable = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}
