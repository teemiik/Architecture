package ru.gdgkazan.popularmovies.screen.details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.gdgkazan.popularmovies.R;
import ru.gdgkazan.popularmovies.model.content.Movie;
import ru.gdgkazan.popularmovies.model.content.Review;
import ru.gdgkazan.popularmovies.model.content.Video;
import ru.gdgkazan.popularmovies.model.response.ReviewsResponse;
import ru.gdgkazan.popularmovies.model.response.VideosResponse;
import ru.gdgkazan.popularmovies.network.ApiFactory;
import ru.gdgkazan.popularmovies.screen.loading.LoadingDialog;
import ru.gdgkazan.popularmovies.screen.loading.LoadingView;
import ru.gdgkazan.popularmovies.utils.Images;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieDetailsActivity extends RxAppCompatActivity {

    private static final String MAXIMUM_RATING = "10";

    public static final String IMAGE = "image";
    public static final String EXTRA_MOVIE = "extraMovie";

    private static final String MOVIE_KEY = "movie";
    private static final String REVIEW_KEY = "review";
    private static final String VIDEO_KEY = "video";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.image)
    ImageView mImage;

    @BindView(R.id.title)
    TextView mTitleTextView;

    @BindView(R.id.overview)
    TextView mOverviewTextView;

    @BindView(R.id.rating)
    TextView mRatingTextView;

    @BindView(R.id.reviewAuthor)
    TextView mReviewAuthor;

    @BindView(R.id.reviewContent)
    TextView mReviewContent;

    @BindView(R.id.videosKey)
    TextView mVideosKey;

    @BindView(R.id.videosName)
    TextView mVideosName;

    private Subscription mMoviesSubscription;

    private LoadingView loadingView;

    @Nullable
    private Movie mMovie;

    @Nullable
    private Review mReview;

    @Nullable
    private Video mVideo;

    public static void navigate(@NonNull AppCompatActivity activity, @NonNull View transitionImage,
                                @NonNull Movie movie) {
        Intent intent = new Intent(activity, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareWindowForAnimation();
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar), IMAGE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        loadingView = LoadingDialog.view(getSupportFragmentManager());

        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_KEY) || !savedInstanceState.containsKey(REVIEW_KEY) || !savedInstanceState.containsKey(VIDEO_KEY)) {
            showMovie(mMovie);
            loadReviews(String.valueOf(mMovie.getId()));
            loadVideos(String.valueOf(mMovie.getId()));
        } else {
            mMovie = savedInstanceState.getParcelable(MOVIE_KEY);
            mReview = (Review) savedInstanceState.getSerializable(REVIEW_KEY);
            mVideo = (Video) savedInstanceState.getSerializable(VIDEO_KEY);
            showMovie(mMovie);
            showReviews(mReview);
            showTrailers(mVideo);
        }



        /**
         * TODO : task
         *
         * Load movie trailers and reviews and display them
         *
         * 1) See http://docs.themoviedb.apiary.io/#reference/movies/movieidtranslations/get?console=1
         * http://docs.themoviedb.apiary.io/#reference/movies/movieidtranslations/get?console=1
         * for API documentation
         *
         * 2) Add requests to {@link ru.gdgkazan.popularmovies.network.MovieService} for trailers and videos
         *
         * 3) Execute requests in parallel and show loading progress until both of them are finished
         *
         * 4) Save trailers and videos to Realm and use cached version when error occurred
         *
         * 5) Handle lifecycle changes any way you like
         */
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mVideo != null || mReview != null || mMovie != null) {
            outState.putParcelable(MOVIE_KEY, mMovie);
            outState.putSerializable(REVIEW_KEY, mReview);
            outState.putSerializable(VIDEO_KEY, mVideo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepareWindowForAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void showMovie(@NonNull Movie movie) {
        String title = getString(R.string.movie_details);
        mCollapsingToolbar.setTitle(title);
        mCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

        Images.loadMovie(mImage, movie, Images.WIDTH_780);

        String year = movie.getReleasedDate().substring(0, 4);
        mTitleTextView.setText(getString(R.string.movie_title, movie.getTitle(), year));
        mOverviewTextView.setText(movie.getOverview());

        String average = String.valueOf(movie.getVoteAverage());
        average = average.length() > 3 ? average.substring(0, 3) : average;
        average = average.length() == 3 && average.charAt(2) == '0' ? average.substring(0, 1) : average;
        mRatingTextView.setText(getString(R.string.rating, average, MAXIMUM_RATING));
    }

    private void showTrailers(@NonNull Video video) {
        mVideosKey.setText(video.getKey());
        mVideosName.setText(video.getName());
    }

    private void showError() {
        mReviewAuthor.setText("Ошибка при загрузке!");
    }

    private void showReviews(@NonNull Review review) {
        mReviewAuthor.setText(review.getAuthor());
        mReviewContent.setText(review.getContent());
    }

    private  void loadReviews(String queryReviews) {
        mMoviesSubscription = ApiFactory.getMoviesService()
                .getReviews(queryReviews)
                .map(ReviewsResponse::getReviews)
                .flatMap(reviews -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        realm.delete(Review.class);
                        realm.insert(reviews);
                    });
                    return Observable.just(reviews);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Review> results = realm.where(Review.class).findAll();
                    return Observable.just(realm.copyFromRealm(results));
                })
                .doOnSubscribe(loadingView::showLoadingIndicator)
                .doAfterTerminate(loadingView::hideLoadingIndicator)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviews -> {
                    this.showReviews(reviews.get(0));
                    mReview = reviews.get(0);
                }, throwable -> showError());

    }

    private  void loadVideos(String queryVideos) {
        mMoviesSubscription = ApiFactory.getMoviesService()
                .getVideos(queryVideos)
                .map(VideosResponse::getVideos)
                .flatMap(videos -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        realm.delete(Video.class);
                        realm.insert(videos);
                    });
                    return Observable.just(videos);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Video> results = realm.where(Video.class).findAll();
                    return Observable.just(realm.copyFromRealm(results));
                })
                .doOnSubscribe(loadingView::showLoadingIndicator)
                .doAfterTerminate(loadingView::hideLoadingIndicator)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos -> {
                    this.showTrailers(videos.get(0));
                    mVideo = videos.get(0);
                } , throwable -> showError());
    }

    @Override
    protected void onPause() {
        if (mMoviesSubscription != null) {
            mMoviesSubscription.unsubscribe();
        }
        super.onPause();
    }
}
