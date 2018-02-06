package ru.gdgkazan.popularmoviesclean.screen.details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.arturvasilov.rxloader.RxUtils;
import ru.gdgkazan.popularmoviesclean.R;
import ru.gdgkazan.popularmoviesclean.data.repository.RepositoryProvider;
import ru.gdgkazan.popularmoviesclean.domain.model.Movie;
import ru.gdgkazan.popularmoviesclean.domain.model.Review;
import ru.gdgkazan.popularmoviesclean.domain.model.Video;
import ru.gdgkazan.popularmoviesclean.domain.usecase.ReviewsUseCase;
import ru.gdgkazan.popularmoviesclean.domain.usecase.VideosUseCase;
import ru.gdgkazan.popularmoviesclean.screen.general.LoadingDialog;
import ru.gdgkazan.popularmoviesclean.screen.general.LoadingView;
import ru.gdgkazan.popularmoviesclean.utils.Images;

public class MovieDetailsActivity extends AppCompatActivity implements ReviewsView, VideosView{

    private static final String MAXIMUM_RATING = "10";

    public static final String IMAGE = "image";
    public static final String EXTRA_MOVIE = "extraMovie";

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

    @BindView(R.id.review_author)
    TextView mReviewAuthor;

    @BindView(R.id.review_content)
    TextView mReviewContent;

    @BindView(R.id.video_key)
    TextView mVideoKey;

    @BindView(R.id.video_name)
    TextView mVideoName;

    private LoadingView mLoadingView;

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

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        showMovie(movie);
        loadVideo(movie);
        loadReview(movie);
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

    private void loadReview(Movie movie) {
        ReviewsUseCase reviewsUseCase = new ReviewsUseCase(RepositoryProvider.getReviewsRepository(), RxUtils.async());
        ReviewsPresenter presenter_rev = new ReviewsPresenter(this, reviewsUseCase);
        presenter_rev.init(String.valueOf(movie.getId()));
    }

    private void loadVideo(Movie movie) {
        VideosUseCase videosUseCase = new VideosUseCase(RepositoryProvider.getVideosRepository(), RxUtils.async());
        VideosPresenter presenter_vid = new VideosPresenter(this, videosUseCase);
        presenter_vid.init(String.valueOf(movie.getId()));
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

    @Override
    public void showLoadingIndicator() {
        mLoadingView.showLoadingIndicator();
    }

    @Override
    public void hideLoadingIndicator() {
        mLoadingView.hideLoadingIndicator();
    }

    @Override
    public void showVideos(@NonNull List<Video> videos) {
        mVideoKey.setText(videos.get(0).getKey());
        mVideoName.setText(videos.get(0).getName());
        Log.e("mi_log", String.valueOf(videos));
    }

    @Override
    public void showReviews(@NonNull List<Review> reviews) {
        mReviewAuthor.setText(reviews.get(0).getAuthor());
        mReviewContent.setText(reviews.get(0).getContent());
        Log.e("mi_log", String.valueOf(reviews));
    }

    @Override
    public void showError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
    }
}
