package ru.gdgkazan.popularmoviesclean.domain.usecase;

import java.util.List;

import ru.gdgkazan.popularmoviesclean.domain.VideosRepository;
import ru.gdgkazan.popularmoviesclean.domain.model.Video;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public class VideosUseCase {

    private final VideosRepository mRepository;
    private final Observable.Transformer<List<Video>, List<Video>> mAsyncTransformer;

    public VideosUseCase(VideosRepository repository,
                         Observable.Transformer<List<Video>, List<Video>> asyncTransformer) {
        mRepository = repository;
        mAsyncTransformer = asyncTransformer;
    }

    public Observable<List<Video>> Videos(String movie_id) {
        return mRepository.Videos(movie_id)
                .compose(mAsyncTransformer);
    }
}


