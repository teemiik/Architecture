package ru.gdgkazan.pictureofdaymvvm.content;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by artem.bolgov on 24.11.2017.
 */

public class MarsPhoto extends BaseObservable {

    @SerializedName("sol")
    private int mSol;

    @SerializedName("img_src")
    private String mImgSrc;

    @Bindable
    @NonNull
    public int getSol() {
        return mSol;
    }

    public void setSol(int sol) {
        this.mSol = sol;
    }

    @Bindable
    @NonNull
    public String getImgSrc() {
        return mImgSrc;
    }

    public void setImgSrc(String img_src) {
        this.mImgSrc = img_src;
    }
}
