package org.atlaslabs.speedrun.ui.category;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaslabs.speedrun.R;
import org.atlaslabs.speedrun.models.Record;
import org.atlaslabs.speedrun.models.User;
import org.atlaslabs.speedrun.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;

class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder> {
    private static final String TAG = RecordAdapter.class.getSimpleName();
    private final PublishSubject<Record> onClickSubject = PublishSubject.create();
    private final List<Record> records;

    RecordAdapter(List<Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_record, parent, false);
        return new RecordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = records.get(position);
        Realm realm = Realm.getDefaultInstance();
        List<String> playerIDs = record.getRun().getPlayersIDs();
        List<String> usersPretty = new ArrayList<>(playerIDs.size());
        holder.binding.recordOwner.setText("");
        for (String userID : playerIDs)
            User.getOrFetch(realm, userID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(u -> {
                        usersPretty.add(u.getNamePretty());
                        if (usersPretty.size() == playerIDs.size()) {
                            String owner = Utils.join(usersPretty.iterator(), ", ");
                            holder.binding.recordOwner.setText(owner);
                            realm.close();
                        }
                    }, e -> Log.w(TAG, "Error getting user " + userID + " data: " + e));

        holder.binding.recordComment.setText(record.getRun().getComment());
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
}
