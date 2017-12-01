package com.game.helper.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;

import com.game.helper.fragments.BaseFragment.GameBasePagerFragment;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;

import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by zr on 2017-10-13.
 */

public class GamePagerFragment extends GameBasePagerFragment {

    FragmentPagerAdapter mAdapter;
    SparseArray sparseArray = new SparseArray<Fragment>();

    @Override
    public FragmentPagerAdapter getPageAdapter(final List<ItemType> list) {
        if (mAdapter == null) {
            mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    ItemType itemType = list.get(position);
                    if (null == sparseArray.get(position)) {
                        if (itemType instanceof ClassicalResults.ClassicalItem) {
                            Fragment fragment = GameListFragment.newInstance(((ClassicalResults.ClassicalItem) itemType).id, 0);
                            sparseArray.put(position, fragment);
                            return fragment;
                        } else if (itemType instanceof CommonResults.CommonItem) {
                            Fragment fragment = GameListFragment.newInstance(0, ((CommonResults.CommonItem) itemType).id);
                            sparseArray.put(position, fragment);
                            return fragment;
                        }
                    }
                    return (Fragment) sparseArray.get(position);
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    ItemType itemType = list.get(position);
                    if (itemType instanceof ClassicalResults.ClassicalItem) {
                        return ((ClassicalResults.ClassicalItem) itemType).name;
                    } else {
                        return ((CommonResults.CommonItem) itemType).name;
                    }
                }

                @Override
                public int getCount() {
                    return list.size();
                }
            };
        }
        return mAdapter;
    }

    public static GamePagerFragment newInstance() {
        return new GamePagerFragment();
    }

}
