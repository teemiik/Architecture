package ru.gdgkazan.popularmoviesclean.domain;

import java.util.List;

import rx.Observable;

/**
 * @author Artur Vasilov
 */
public interface ReviewsRepository {

    Observable<List<ru.gdgkazan.popularmoviesclean.domain.model.Review>> Reviews(String movie_id);
}
