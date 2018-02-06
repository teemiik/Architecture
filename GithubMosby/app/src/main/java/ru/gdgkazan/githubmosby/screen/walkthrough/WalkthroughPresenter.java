package ru.gdgkazan.githubmosby.screen.walkthrough;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.gdgkazan.githubmosby.utils.PreferenceUtils;

/**
 * Created by bolgov.artem on 13.11.2017.
 */

public class WalkthroughPresenter extends MvpBasePresenter<WalkthroughView> {

    private final LifecycleHandler mLifecycleHandler;
    private final WalkthroughView mWalkthroughView;

    public WalkthroughPresenter(@NonNull LifecycleHandler lifecycleHandler,
                                @NonNull WalkthroughView walkthroughView) {
        mLifecycleHandler = lifecycleHandler;
        mWalkthroughView = walkthroughView;
    }


    public void init() {
        if (PreferenceUtils.isWalkthroughPassed()) {
            mWalkthroughView.startAuthActivity();
        }
    }

    public int actionNext(int mCurrentItem) {
        if (mWalkthroughView.isLastBenefit()) {
            PreferenceUtils.saveWalkthroughPassed();
            mWalkthroughView.startAuthActivity();
        } else {
            mCurrentItem++;
            mWalkthroughView.showBenefit(mCurrentItem, mWalkthroughView.isLastBenefit());
        }

        return mCurrentItem;
    }
}
