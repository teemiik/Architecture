package ru.gdgkazan.popularmovies.model.content;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * @author Artur Vasilov
 */
public class Review extends RealmObject implements Serializable {

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("content")
    private String mContent;

    @NonNull
    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(@NonNull String author) {
        mAuthor = author;
    }

    @NonNull
    public String getContent() {
        return mContent;
    }

    public void setContent(@NonNull String content) {
        mContent = content;
    }
}
