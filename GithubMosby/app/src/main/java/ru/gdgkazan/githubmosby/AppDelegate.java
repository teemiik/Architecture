package ru.gdgkazan.githubmosby;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.rx.RealmObservableFactory;
import ru.gdgkazan.githubmosby.api.ApiFactory;
import ru.gdgkazan.githubmosby.repository.RepositoryProvider;

/**
 * @author Artur Vasilov
 */
public class AppDelegate extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        Realm.init(this);

        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)
                .build();

        Hawk.clear();
        Hawk.resetCrypto();

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .rxFactory(new RealmObservableFactory())
                .build();
        Realm.setDefaultConfiguration(configuration);

        ApiFactory.recreate();
        RepositoryProvider.init();
    }

    @NonNull
    public static Context getContext() {
        return sContext;
    }
}
