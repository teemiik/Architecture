package ru.gdgkazan.pictureofdaymvvm.screen.pictures;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.gdgkazan.pictureofdaymvvm.R;

/**
 * @author Artur Vasilov
 */
public class PicturesActivity extends AppCompatActivity {

    /**
     * TODO : task
     *
     * 1) Implement activity with list of images for each day
     * 2) New images must be loaded when scrolling to end
     * 3) Implement this screen with MVVM and Data Binding
     * 4) Test your ViewModel
     */

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_mars);

        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
                ImageFragment imageFragment = new ImageFragment();
                mFragmentTransaction.add(R.id.container, imageFragment);
        mFragmentTransaction.commit();
    }
}
