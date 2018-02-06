package ru.gdgkazan.githubmosby.screen.walkthrough;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import ru.gdgkazan.githubmosby.content.Benefit;
import ru.gdgkazan.githubmosby.widget.PageChangeViewPager;


/**
 * Created by bolgov.artem on 13.11.2017.
 */

public interface WalkthroughView extends PageChangeViewPager.PagerStateListener, MvpView {

    void showBenefit(int index, boolean isLastBenefit);

    boolean isLastBenefit();

    void startAuthActivity();

    List<Benefit> getBenefits();

    @Override
    void onPageChanged(int selectedPage, boolean fromUser);
}
