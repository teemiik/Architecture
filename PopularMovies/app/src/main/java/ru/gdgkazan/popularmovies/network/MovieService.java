package ru.gdgkazan.popularmovies.network;

import android.support.annotation.NonNull;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.gdgkazan.popularmovies.model.response.MoviesResponse;
import ru.gdgkazan.popularmovies.model.response.ReviewsResponse;
import ru.gdgkazan.popularmovies.model.response.VideosResponse;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public interface MovieService {

    @GET("popular/")
    Observable<MoviesResponse> popularMovies();

    @GET("{movie_id}/reviews")
    Observable<ReviewsResponse> getReviews(@NonNull @Path("movie_id") String query);

    @GET("{movie_id}/videos")
    Observable<VideosResponse> getVideos(@NonNull @Path("movie_id") String query);

}
