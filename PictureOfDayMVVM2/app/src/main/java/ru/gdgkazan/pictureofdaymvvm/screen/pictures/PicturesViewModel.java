package ru.gdgkazan.pictureofdaymvvm.screen.pictures;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.gdgkazan.pictureofdaymvvm.BR;
import ru.gdgkazan.pictureofdaymvvm.content.MarsPhoto;
import ru.gdgkazan.pictureofdaymvvm.repository.RepositoryProvider;

/**
 * @author Artur Vasilov
 */
public class PicturesViewModel extends BaseObservable {

    @Nullable
    private MarsPhoto mMarsPhoto;

    private final LifecycleHandler mLifecycleHandler;

    private boolean mIsLoading = false;

    public PicturesViewModel(@NonNull LifecycleHandler lifecycleHandler) {
        mLifecycleHandler = lifecycleHandler;
    }

    public void init(String date, int page) {
        RepositoryProvider.provideNasaRepository()
                .photosMars(date, page)
                .doOnSubscribe(() -> setLoading(true))
                .doOnTerminate(() -> setLoading(false))
                //.compose(mLifecycleHandler.load(R.id.day_picture_request))
                .subscribe(marsPhoto -> {
                    mMarsPhoto = mMarsPhoto;
                }, throwable -> {
                    //TODO : handle error
                });
    }

    @Bindable
    @NonNull
    public int getSol() {
        if (mMarsPhoto == null) {
            return 0;
        }
        return mMarsPhoto.getSol();
    }

    @Bindable
    @NonNull
    public String getImageSrc() {
        if (mMarsPhoto == null) {
            return "";
        }
        return mMarsPhoto.getImgSrc();
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    @VisibleForTesting
    void setLoading(boolean isLoading) {
        mIsLoading = isLoading;
        notifyPropertyChanged(BR.loading);
    }
}
