package org.atlaslabs.speedrun.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestUtil {
    public static SpeedrunAPI createAPI(){
        return new Retrofit.Builder()
                .baseUrl(SpeedrunAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(SpeedrunAPI.class);
    }
}
