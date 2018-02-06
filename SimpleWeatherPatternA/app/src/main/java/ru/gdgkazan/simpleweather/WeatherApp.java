package ru.gdgkazan.simpleweather;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.os.Bundle;

import ru.arturvasilov.sqlite.core.SQLite;

/**
 * @author Artur Vasilov
 */
public class WeatherApp extends Application {

    public static final String ACCOUNT_TYPE = "ru.gdgkazan.simpleweather.account";

    public static final String AUTHORITY = "ru.gdgkazan.simpleweather";

    public static Account sAccount;

    @Override
    public void onCreate() {
        super.onCreate();
        SQLite.initialize(this);

        final AccountManager am = AccountManager.get(this);
        if (sAccount == null) {
            sAccount = new Account("default", ACCOUNT_TYPE);
        }
        if (am.addAccountExplicitly(sAccount, getPackageName(), new Bundle())) {
            ContentResolver.addPeriodicSync(
                    sAccount,
                    AUTHORITY,
                    Bundle.EMPTY,
                    10
            );
        }
    }
}
