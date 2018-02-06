package ru.gdgkazan.simpleweather.screen.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import ru.gdgkazan.simpleweather.model.City;
import ru.gdgkazan.simpleweather.network.ApiFactory;

/**
 * @author Artur Vasilov
 */
public class RetrofitWeatherLoader extends Loader<List<City>> {

    private List<Call> listCall;

    @Nullable
    private List<City> mCity;

    public RetrofitWeatherLoader(Context context, @NonNull String[] cityArray) {
        super(context);
        listCall = new ArrayList<>();
        mCity = new ArrayList<>();
        for (String city : cityArray) {
            listCall.add(ApiFactory.getWeatherService().getWeather(city));
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (!mCity.isEmpty()) {
            deliverResult(mCity);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        for (Call val : listCall) {
            val.enqueue(new Callback<City>() {
                @Override
                public void onResponse(Call<City> call, Response<City> response) {

                    mCity.add(response.body());

                    if (mCity != null && mCity.size() == listCall.size()) {
                        deliverResult(mCity);
                    }
                }

                @Override
                public void onFailure(Call<City> call, Throwable t) {
                    deliverResult(null);
                }
            });
        }
    }

    @Override
    protected void onStopLoading() {
        for (Call val : listCall) {
            val.cancel();
        }
        super.onStopLoading();
    }
}

