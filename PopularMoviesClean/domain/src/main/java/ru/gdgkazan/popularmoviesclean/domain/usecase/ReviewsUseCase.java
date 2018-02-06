package ru.gdgkazan.popularmoviesclean.domain.usecase;

import java.util.List;

import ru.gdgkazan.popularmoviesclean.domain.ReviewsRepository;
import ru.gdgkazan.popularmoviesclean.domain.model.Review;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public class ReviewsUseCase {

    private final ReviewsRepository mRepository;
    private final Observable.Transformer<List<Review>, List<Review>> mAsyncTransformer;

    public ReviewsUseCase(ReviewsRepository repository,
                          Observable.Transformer<List<Review>, List<Review>>  asyncTransformer) {
        mRepository = repository;
        mAsyncTransformer = asyncTransformer;
    }

    public Observable<List<Review>> Reviews(String movie_id) {
        return mRepository.Reviews(movie_id)
                .compose(mAsyncTransformer);
    }
}


