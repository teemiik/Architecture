package ru.gdgkazan.popularmoviesclean.data.network;

import android.support.annotation.NonNull;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.gdgkazan.popularmoviesclean.data.model.response.MoviesResponse;
import ru.gdgkazan.popularmoviesclean.data.model.response.ReviewsResponse;
import ru.gdgkazan.popularmoviesclean.data.model.response.VideosResponse;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public interface MovieService {

    @GET("popular/")
    Observable<MoviesResponse> popularMovies();

    @GET("{movie_id}/reviews")
    Observable<ReviewsResponse> Reviews(@NonNull @Path("movie_id") String query);

    @GET("{movie_id}/videos")
    Observable<VideosResponse> Videos(@NonNull @Path("movie_id") String query);

}
