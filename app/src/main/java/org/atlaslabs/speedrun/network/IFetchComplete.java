package org.atlaslabs.speedrun.network;

import io.realm.RealmObject;

public interface IFetchComplete<T extends RealmObject> {
    void fetchComplete(T item);
}
