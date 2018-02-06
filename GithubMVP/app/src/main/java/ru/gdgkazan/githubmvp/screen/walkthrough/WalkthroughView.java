package ru.gdgkazan.githubmvp.screen.walkthrough;

import java.util.List;

import ru.gdgkazan.githubmvp.content.Benefit;
import ru.gdgkazan.githubmvp.widget.PageChangeViewPager;

/**
 * Created by bolgov.artem on 13.11.2017.
 */

public interface WalkthroughView extends PageChangeViewPager.PagerStateListener {

    void showBenefit(int index, boolean isLastBenefit);

    boolean isLastBenefit();

    void startAuthActivity();

    List<Benefit> getBenefits();

    @Override
    void onPageChanged(int selectedPage, boolean fromUser);
}
