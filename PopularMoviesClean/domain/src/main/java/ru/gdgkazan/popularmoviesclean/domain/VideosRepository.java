package ru.gdgkazan.popularmoviesclean.domain;

import java.util.List;

import rx.Observable;

/**
 * @author Artur Vasilov
 */
public interface VideosRepository {

    Observable<List<ru.gdgkazan.popularmoviesclean.domain.model.Video>> Videos(String movie_id);
}
