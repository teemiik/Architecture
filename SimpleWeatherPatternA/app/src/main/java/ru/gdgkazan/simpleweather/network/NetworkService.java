package ru.gdgkazan.simpleweather.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;
import ru.arturvasilov.sqlite.core.SQLite;
import ru.arturvasilov.sqlite.core.Where;
import ru.gdgkazan.simpleweather.data.GsonHolder;
import ru.gdgkazan.simpleweather.data.model.City;
import ru.gdgkazan.simpleweather.data.model.WeatherCity;
import ru.gdgkazan.simpleweather.data.tables.CityTable;
import ru.gdgkazan.simpleweather.data.tables.RequestTable;
import ru.gdgkazan.simpleweather.data.tables.WeatherCityTable;
import ru.gdgkazan.simpleweather.network.model.NetworkRequest;
import ru.gdgkazan.simpleweather.network.model.Request;
import ru.gdgkazan.simpleweather.network.model.RequestStatus;

/**
 * @author Artur Vasilov
 */
public class NetworkService extends IntentService {

    private static final String REQUEST_KEY = "request";
    private static final String CITY_NAME_KEY = "city_name";

    public static void start(@NonNull Context context, @NonNull Request request, @NonNull String cityName) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.putExtra(REQUEST_KEY, GsonHolder.getGson().toJson(request));
        intent.putExtra(CITY_NAME_KEY, cityName);
        context.startService(intent);
    }

    public static void start(@NonNull Context context, @NonNull Request request) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.putExtra(REQUEST_KEY, GsonHolder.getGson().toJson(request));
        context.startService(intent);
    }

    @SuppressWarnings("unused")
    public NetworkService() {
        super(NetworkService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Request request = GsonHolder.getGson().fromJson(intent.getStringExtra(REQUEST_KEY), Request.class);
        Request savedRequest = SQLite.get().querySingle(RequestTable.TABLE,
                Where.create().equalTo(RequestTable.REQUEST, request.getRequest()));

        if (savedRequest != null && request.getStatus() == RequestStatus.IN_PROGRESS) {
            return;
        }
        request.setStatus(RequestStatus.IN_PROGRESS);
        SQLite.get().insert(RequestTable.TABLE, request);
        SQLite.get().notifyTableChanged(RequestTable.TABLE);

        if (TextUtils.equals(NetworkRequest.CITY, request.getRequest())) {
            executeFileRequest(request);
        }

        if (TextUtils.equals(NetworkRequest.CITY_WEATHER, request.getRequest())) {
            String cityName = intent.getStringExtra(CITY_NAME_KEY);
            executeCityRequest(request, cityName);
        }
    }

    private void executeCityRequest(@NonNull Request request, @NonNull String cityName) {
        try {
            City city = ApiFactory.getWeatherService()
                    .getWeather(cityName)
                    .execute()
                    .body();
            SQLite.get().delete(CityTable.TABLE);
            SQLite.get().insert(CityTable.TABLE, city);
            request.setStatus(RequestStatus.SUCCESS);
        } catch (IOException e) {
            request.setStatus(RequestStatus.ERROR);
            request.setError(e.getMessage());
        } finally {
            SQLite.get().insert(RequestTable.TABLE, request);
            SQLite.get().notifyTableChanged(RequestTable.TABLE);
        }
    }

    private void executeFileRequest(@NonNull Request request) {

        try {
            ResponseBody responseBody = ApiFactory.getCityFileService()
                    .getFileCity()
                    .execute()
                    .body();
            ByteArrayInputStream bais = new ByteArrayInputStream(responseBody.bytes());

            GZIPInputStream gzis = new GZIPInputStream(bais);
            InputStreamReader reader = new InputStreamReader(gzis, "UTF-8");
            BufferedReader in = new BufferedReader(reader);

            String readed;
            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            while ((readed = in.readLine()) != null) {
                stringBuilder.append(readed);
                System.out.println(readed);
                if (i == 90) break;
                i++;
            }

            String result = stringBuilder.toString();
            result = result.substring(0, stringBuilder.length() - 1) + "]";
            result = result.replace(" ", "");
            result = result.replace("", "");

            ArrayList<WeatherCity> arrayCity = json_encode(result);
            if (arrayCity != null) {
                SQLite.get().delete(WeatherCityTable.TABLE);
                SQLite.get().insert(WeatherCityTable.TABLE, arrayCity);
            }

            request.setStatus(RequestStatus.SUCCESS);
        } catch (IOException e) {
            request.setStatus(RequestStatus.ERROR);
            request.setError(e.getMessage());
        } finally {
            SQLite.get().insert(RequestTable.TABLE, request);
            SQLite.get().notifyTableChanged(RequestTable.TABLE);
        }
    }

    public static ArrayList<WeatherCity> json_encode(String json_str) {

        ArrayList<WeatherCity> array_city = new ArrayList<>();

        try {
            JSONArray dataAr = new JSONArray(json_str);
            int len_array = dataAr.length();

            for (int i = 0; i < len_array; i++) {
                JSONObject city = dataAr.getJSONObject(i);
                array_city.add(new WeatherCity(city.getInt("id"), city.getString("name")));
            }

            return array_city;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}

