package ru.gdgkazan.pictureofdaymvvm.repository;

import android.support.annotation.NonNull;

import ru.gdgkazan.pictureofdaymvvm.content.DayPicture;
import ru.gdgkazan.pictureofdaymvvm.content.MarsPhoto;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public interface NasaRepository {

    @NonNull
    Observable<DayPicture> dayPicture();

    @NonNull
    Observable<MarsPhoto> photosMars(String date, int page);

}
