package org.atlaslabs.speedrun.ui.user;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Category;
import org.atlaslabs.speedrun.models.Game;
import org.atlaslabs.speedrun.models.Record;
import org.atlaslabs.speedrun.util.Utils;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;

class PersonalBestAdapter extends RecyclerView.Adapter<PersonalBestViewHolder> {
    private static final String TAG = org.atlaslabs.speedrun.ui.user.PersonalBestAdapter.class.getSimpleName();
    private final PublishSubject<Record> onClickSubject = PublishSubject.create();
    private final List<Record> records;
    private final Realm realm = Realm.getDefaultInstance();
    private final CompositeDisposable disposable = new CompositeDisposable();

    PersonalBestAdapter(List<Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public PersonalBestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_pb, parent, false);
        return new PersonalBestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalBestViewHolder holder, int position) {
        Record record = records.get(position);
        disposable.add(Game.getOrFetch(realm, record.getRun().getGame())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(g -> {
                    holder.binding.recordGame.setText(g.getNames().getInternational());
                }, e -> Log.w(TAG, "Error getting pb game: " + e)));
        disposable.add(Category.getOrFetch(realm, record.getRun().getCategory())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(c -> {
                    holder.binding.recordCategory.setText(c.getName());
                }, e -> Log.w(TAG, "Error getting category game: " + e)));
        holder.binding.recordPlace.setText(String.format(Locale.US, "%d", record.getPlace()));
        holder.binding.recordTime.setText(Utils.timePretty(record.getRun().getTimes().getPrimaryTime()));
        holder.itemView.setOnClickListener(v -> onClickSubject.onNext(record));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public Observable<Record> getClickedRecords() {
        return onClickSubject;
    }

    public void destroy() {
        disposable.dispose();
        realm.close();
    }
}