package ca.unb.mobiledev.aceit;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class RulesPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public RulesPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HorseRaceRules();
                break;
            case 1:
                fragment = new CatchTheDealerRules();
                break;
            case 2:
                fragment = new CrossTheBridgeRules();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}