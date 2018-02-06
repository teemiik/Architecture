package ru.gdgkazan.popularmoviesclean.data.repository;

import ru.gdgkazan.popularmoviesclean.domain.MoviesRepository;
import ru.gdgkazan.popularmoviesclean.domain.ReviewsRepository;
import ru.gdgkazan.popularmoviesclean.domain.VideosRepository;

/**
 * @author Artur Vasilov
 */
public class RepositoryProvider {

    private static MoviesRepository sMoviesRepository;
    private static ReviewsRepository sReviewsRepository;
    private static VideosRepository sVideosRepository;

    public static MoviesRepository getMoviesRepository() {
        if (sMoviesRepository == null) {
            sMoviesRepository = new MoviesDataRepository();
        }
        return sMoviesRepository;
    }


    public static ReviewsRepository getReviewsRepository() {
        if (sReviewsRepository == null) {
            sReviewsRepository = new ReviewsDataRepository();
        }
        return sReviewsRepository;
    }

    public static VideosRepository getVideosRepository() {
        if (sVideosRepository == null) {
            sVideosRepository = new VideosDataRepository();
        }
        return sVideosRepository;
    }
}
