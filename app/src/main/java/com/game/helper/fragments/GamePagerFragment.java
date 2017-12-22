package com.game.helper.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.game.helper.data.RxConstant;
import com.game.helper.event.BusProvider;
import com.game.helper.event.MsgEvent;
import com.game.helper.fragments.BaseFragment.GameBasePagerFragment;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.model.LoginUserInfo;
import com.game.helper.utils.SharedPreUtil;

import java.lang.reflect.Method;
import java.util.List;

import zlc.season.practicalrecyclerview.ItemType;

/**
 * Created by zr on 2017-10-13.
 */

public class GamePagerFragment extends GameBasePagerFragment {

    FragmentPagerAdapter mAdapter;
    SparseArray sparseArray = new SparseArray<Fragment>();

//    @Override
//    public void onResume() {
//        super.onResume();
////        if (getUserVisibleHint())
//        {
//            LoginUserInfo info = SharedPreUtil.getLoginUserInfo();
//            BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, info == null ? "" : info.icon));
//        }
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LoginUserInfo info = SharedPreUtil.getLoginUserInfo();
            BusProvider.getBus().post(new MsgEvent<String>(RxConstant.Head_Image_Change_Type, RxConstant.Head_Image_Change_Type, info == null ? "" : info.icon));
        }
    }

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
                public Object instantiateItem(ViewGroup container, int position) {
                    // TODO Auto-generated method stub
                    if (position == 0)
                        removeFragment(container, position);
                    return super.instantiateItem(container, position);
                }

                private void removeFragment(ViewGroup container, int index) {
                    FragmentManager fm = getChildFragmentManager();
                    String tag = getFragmentTag(container.getId(), index);
                    Fragment fragment = fm.findFragmentByTag(tag);
                    if (fragment == null)
                        return;
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(fragment);
                    ft.commit();
                    ft = null;
                    fm.executePendingTransactions();
                }

                private String getFragmentTag(int viewId, int index) {
                    try {
                        Class<FragmentPagerAdapter> cls = FragmentPagerAdapter.class;
                        Class<?>[] parameterTypes = {int.class, long.class};
                        Method method = cls.getDeclaredMethod("makeFragmentName", parameterTypes);
                        method.setAccessible(true);
                        String tag = (String) method.invoke(this, viewId, index);
                        return tag;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
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
                public int getItemPosition(Object object) {
                    return POSITION_NONE;
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
