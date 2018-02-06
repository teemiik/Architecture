package ru.gdgkazan.popularmoviesclean.screen.details;

import android.support.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.gdgkazan.popularmoviesclean.R;
import ru.gdgkazan.popularmoviesclean.domain.usecase.MoviesUseCase;
import ru.gdgkazan.popularmoviesclean.domain.usecase.VideosUseCase;
import ru.gdgkazan.popularmoviesclean.screen.movies.MoviesView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Artur Vasilov
 */
public class VideosPresenter {

    private final VideosView mVideosView;
    private final VideosUseCase mVideosUseCase;

    public VideosPresenter(@NonNull VideosView videosView, @NonNull VideosUseCase videosUseCase) {
        mVideosView = videosView;
        mVideosUseCase = videosUseCase;
    }

    public void init(String movie_id) {
        mVideosUseCase.Videos(movie_id)
                .doOnSubscribe(mVideosView::showLoadingIndicator)
                .doAfterTerminate(mVideosView::hideLoadingIndicator)
                .subscribe(mVideosView::showVideos, mVideosView::showError);
    }
}

