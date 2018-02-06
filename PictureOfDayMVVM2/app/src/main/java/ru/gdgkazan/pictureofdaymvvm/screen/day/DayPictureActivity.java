package ru.gdgkazan.pictureofdaymvvm.screen.day;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;
import ru.gdgkazan.pictureofdaymvvm.R;
import ru.gdgkazan.pictureofdaymvvm.databinding.ActivityDayPictureBinding;
import ru.gdgkazan.pictureofdaymvvm.screen.pictures.PicturesActivity;

public class DayPictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_picture);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        LifecycleHandler handler = LoaderLifecycleHandler
                .create(this, getSupportLoaderManager());
        DayViewModel viewModel = new DayViewModel(handler);

        ActivityDayPictureBinding binding = DataBindingUtil.
                setContentView(this, R.layout.activity_day_picture);
        binding.setModel(viewModel);
        binding.button.setOnClickListener(view ->
            startActivity(new Intent(this, PicturesActivity.class))
        );

        viewModel.init();
    }
}
