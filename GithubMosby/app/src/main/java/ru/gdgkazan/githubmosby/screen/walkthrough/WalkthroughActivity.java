package ru.gdgkazan.githubmosby.screen.walkthrough;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;
import ru.gdgkazan.githubmosby.R;
import ru.gdgkazan.githubmosby.content.Benefit;
import ru.gdgkazan.githubmosby.screen.auth.AuthActivity;
import ru.gdgkazan.githubmosby.utils.PreferenceUtils;
import ru.gdgkazan.githubmosby.widget.PageChangeViewPager;

/**
 * @author Artur Vasilov
 */
public class WalkthroughActivity extends MvpActivity<WalkthroughView, WalkthroughPresenter> implements WalkthroughView {

    private static final int PAGES_COUNT = 3;

    @BindView(R.id.pager)
    PageChangeViewPager mPager;

    @BindView(R.id.btn_walkthrough)
    Button mActionButton;

    private int mCurrentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        ButterKnife.bind(this);

        mPager.setAdapter(new WalkthroughAdapter(getFragmentManager(), getBenefits()));
        mPager.setOnPageChangedListener(this);

        mActionButton.setText(R.string.next_uppercase);

        getPresenter().init();

        /**
         * TODO : task
         *
         * Refactor this screen using MVP pattern
         *
         * Hint: there are no requests on this screen, so it's good place to start
         *
         * You can simply go through each line of code and decide if it should be in View or in Presenter
         */
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_walkthrough)
    public void onActionButtonClick() {
        mCurrentItem = getPresenter().actionNext(mCurrentItem);
    }

    @NonNull
    @Override
    public WalkthroughPresenter createPresenter() {
        LifecycleHandler lifecycleHandler = LoaderLifecycleHandler.create(this, getSupportLoaderManager());
        return new WalkthroughPresenter(lifecycleHandler, this);
    }

    @Override
    public void onPageChanged(int selectedPage, boolean fromUser) {
        if (fromUser) {
            mCurrentItem = selectedPage;
            showBenefit(mCurrentItem, isLastBenefit());
        }
    }

    @Override
    public boolean isLastBenefit() {
        return mCurrentItem == PAGES_COUNT - 1;
    }

    @Override
    public void showBenefit(int index, boolean isLastBenefit) {
        mActionButton.setText(isLastBenefit ? R.string.finish_uppercase : R.string.next_uppercase);
        if (index == mPager.getCurrentItem()) {
            return;
        }
        mPager.smoothScrollNext(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    @Override
    public void startAuthActivity() {
        AuthActivity.start(this);
        finish();
    }

    @Override
    @NonNull
    public List<Benefit> getBenefits() {
        return new ArrayList<Benefit>() {
            {
                add(Benefit.WORK_TOGETHER);
                add(Benefit.CODE_HISTORY);
                add(Benefit.PUBLISH_SOURCE);
            }
        };
    }

}
