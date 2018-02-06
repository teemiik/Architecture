package ru.gdgkazan.pictureofdaymvvm.api;

import retrofit2.http.GET;
import ru.gdgkazan.pictureofdaymvvm.content.DayPicture;
import ru.gdgkazan.pictureofdaymvvm.content.MarsPhoto;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public interface NasaService {

    @GET("/planetary/apod")
    Observable<DayPicture> todayPicture();

    @GET("/planetary/earth/imagery")
    Observable<MarsPhoto> photosMars(String date, int page);

}
