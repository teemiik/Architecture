package ru.gdgkazan.popularmoviesclean.screen.details;

import android.support.annotation.NonNull;

import java.util.List;

import ru.gdgkazan.popularmoviesclean.domain.model.Video;
import ru.gdgkazan.popularmoviesclean.screen.general.LoadingView;

/**
 * @author Artur Vasilov
 */
public interface VideosView extends LoadingView {

    void showVideos(@NonNull List<Video> videos);

    void showError(Throwable throwable);

}


