package ru.gdgkazan.popularmoviesclean.screen.details;

import android.support.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.gdgkazan.popularmoviesclean.R;
import ru.gdgkazan.popularmoviesclean.domain.model.Review;
import ru.gdgkazan.popularmoviesclean.domain.usecase.MoviesUseCase;
import ru.gdgkazan.popularmoviesclean.domain.usecase.ReviewsUseCase;
import ru.gdgkazan.popularmoviesclean.screen.movies.MoviesView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Artur Vasilov
 */
public class ReviewsPresenter {

    private final ReviewsView mReviewsView;
    private final ReviewsUseCase mReviewsUseCase;

    public ReviewsPresenter(@NonNull ReviewsView reviewsView, @NonNull ReviewsUseCase reviewsUseCase) {
        mReviewsView = reviewsView;
        mReviewsUseCase = reviewsUseCase;
    }

    public void init(String movie_id) {
        mReviewsUseCase.Reviews(movie_id)
                .doOnSubscribe(mReviewsView::showLoadingIndicator)
                .doAfterTerminate(mReviewsView::hideLoadingIndicator)
                .subscribe(mReviewsView::showReviews, mReviewsView::showError);
    }
}

