package ru.gdgkazan.popularmovies;

import android.app.Application;
import android.support.annotation.NonNull;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.rx.RealmObservableFactory;

/**
 * @author Artur Vasilov
 */
public class MoviesApp extends Application {

    private static MoviesApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        sInstance = this;

        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(this))
                .build();
        Picasso.setSingletonInstance(picasso);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .rxFactory(new RealmObservableFactory())
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    @NonNull
    public static MoviesApp getAppContext() {
        return sInstance;
    }
}
