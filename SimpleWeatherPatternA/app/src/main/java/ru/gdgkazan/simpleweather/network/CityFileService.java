package ru.gdgkazan.simpleweather.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Artur Vasilov
 */
public interface CityFileService {

    @GET("sample/city.list.json.gz")
    Call<ResponseBody> getFileCity();
}
