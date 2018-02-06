package ru.gdgkazan.pictureofdaymvvm.databinding;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ru.gdgkazan.pictureofdaymvvm.DayPictureApp;

/**
 * @author Artur Vasilov
 */
public class PicturesViewAdapters {

    @BindingAdapter("android:src")
    public static void setImageRUrl(@NonNull ImageView imageView, @NonNull String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Picasso.with(DayPictureApp.getAppContext())
                .load(url)
                .noFade()
                .into(imageView);
    }

}
