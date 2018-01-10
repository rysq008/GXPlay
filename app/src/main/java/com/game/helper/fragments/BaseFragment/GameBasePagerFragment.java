package com.game.helper.fragments.BaseFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.game.helper.R;
import com.game.helper.model.ClassicalResults;
import com.game.helper.model.CommonResults;
import com.game.helper.present.GameFragmentPresent;
import com.game.helper.views.SearchComponentView;
import com.game.helper.views.XReloadableStateContorller;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.NetError;
import zlc.season.practicalrecyclerview.ItemType;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * Created by wanglei on 2016/12/31.
 */

public abstract class GameBasePagerFragment extends XBaseFragment<GameFragmentPresent> {

    @BindView(R.id.game_root_layout)
    XReloadableStateContorller xStateController;
    @BindView(R.id.game_viewpager)
    ViewPager viewPager;//content
    @BindView(R.id.common_search_view)
    SearchComponentView searchComponentView;
    @BindView(R.id.game_tabs)
    MagicIndicator tabStrip;
    @BindView(R.id.game_extran_iv)
    ImageView iv;

    List<ItemType> list = new ArrayList<ItemType>();

    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        getP().onInitData(xStateController, true);
    }

    private void initAdapter() {
        viewPager.setAdapter(getPageAdapter(list));
//        viewPager.setOffscreenPageLimit(2);
    }

    public abstract PagerAdapter getPageAdapter(List<ItemType> list);

    public void showError(NetError error) {
    }

    public void showData(List<ItemType> model) {
        if (Kits.Empty.check(model)) {
            xStateController.showEmpty();
        } else {
            xStateController.showContent();
            list.addAll(model);
            CommonNavigator commonNavigator = new CommonNavigator(context) {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    ItemType itemType = list.get(position);
                    if (viewPager.getTag() == null && position == 0) {
                        if (itemType instanceof ClassicalResults.ClassicalItem) {
                            (((ClassicalResults.ClassicalItem) itemType)).isCheck = true;
                        } else {
                            (((CommonResults.CommonItem) itemType)).isCheck = true;
                        }
                        viewPager.setTag(itemType);
                    } else {
                        if (itemType.equals(viewPager.getTag())) {
                            return;
                        } else {
                            ItemType oldType = (ItemType) viewPager.getTag();
                            if (oldType instanceof ClassicalResults.ClassicalItem) {
                                (((ClassicalResults.ClassicalItem) oldType)).isCheck = false;
                            } else {
                                (((CommonResults.CommonItem) oldType)).isCheck = false;
                            }
/*****************************************************************************************************************************/
                            if (itemType instanceof ClassicalResults.ClassicalItem) {
                                (((ClassicalResults.ClassicalItem) itemType)).isCheck = true;
                            } else {
                                (((CommonResults.CommonItem) itemType)).isCheck = true;
                            }
                            viewPager.setTag(itemType);
                        }
                    }
                }
            };
            commonNavigator.setAdapter(new CommonNavigatorAdapter() {

                @Override
                public int getCount() {
                    return list == null ? 0 : list.size();
                }

                @Override
                public IPagerTitleView getTitleView(Context context, final int index) {
                    ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                    colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                    colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                    final ItemType itemType = list.get(index);
                    if (itemType instanceof ClassicalResults.ClassicalItem) {
                        colorTransitionPagerTitleView.setText(((ClassicalResults.ClassicalItem) itemType).name);
                        (((ClassicalResults.ClassicalItem) itemType)).isCheck = true;
                    } else {
                        colorTransitionPagerTitleView.setText(((CommonResults.CommonItem) itemType).name);
                        (((CommonResults.CommonItem) itemType)).isCheck = true;
                    }
                    colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewPager.setCurrentItem(index);
                        }
                    });
                    return colorTransitionPagerTitleView;
                }

                @Override
                public IPagerIndicator getIndicator(Context context) {
                    LinePagerIndicator indicator = new LinePagerIndicator(context);
                    indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                    return indicator;
                }
            });
            tabStrip.setNavigator(commonNavigator);
            ViewPagerHelper.bind(tabStrip, viewPager);
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_game_layout;
    }

    @Override
    public GameFragmentPresent newP() {
        return new GameFragmentPresent();
    }

    @OnClick(R.id.game_extran_iv)
    public void OnClick(View v) {
//        Dialog dialog = new Dialog(context, R.style.umeng_socialize_popup_dialog);
        // 显示透明的对话框
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.FullScreenDialog).create();
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        recyclerView.setBackgroundResource(R.color.white);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                CheckedTextView tv = new CheckedTextView(context);
                tv.setPadding(10, 10, 10, 10);
                return new TViewHolder(tv);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ItemType itemType = list.get(position);
                ((TViewHolder) holder).setDisplay(itemType, position);
            }

            @Override
            public int getItemCount() {
                return list == null ? 0 : list.size();
            }

            class TViewHolder extends RecyclerView.ViewHolder {
                boolean mIsCheck;

                public TViewHolder(View itemView) {
                    super(itemView);
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                public void setData(ItemType itemType, boolean isCheck) {
                    if (itemType.equals(viewPager.getTag())) {
                        return;
                    }
                    if (itemType instanceof ClassicalResults.ClassicalItem) {
                        ((CheckedTextView) itemView).setText(((ClassicalResults.ClassicalItem) itemType).name);
                        (((ClassicalResults.ClassicalItem) itemType)).isCheck = isCheck;
                    } else {
                        ((CheckedTextView) itemView).setText(((CommonResults.CommonItem) itemType).name);
                        (((CommonResults.CommonItem) itemType)).isCheck = isCheck;
                    }
                    ((CheckedTextView) itemView).setChecked(isCheck);
                    ((CheckedTextView) itemView).setTextColor(isCheck?getResources().getColor(R.color.red):getResources().getColor(R.color.black));
                }

                public void setDisplay(final ItemType itemType, final int pos) {
                    if (viewPager.getTag() == null && pos == 0) {
                        viewPager.setTag(itemType);
                        mIsCheck = true;
                    } else {
                        if (itemType.equals(viewPager.getTag())) {
                            mIsCheck = true;
                        }
                    }
                    setData(itemType, mIsCheck);
                    ((CheckedTextView) itemView).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewPager.setCurrentItem(pos);
                            setData(itemType, !mIsCheck);
                            viewPager.setTag(itemType);
                            dialog.cancel();
                        }
                    });
                }
            }
        });
        dialog.show();

        //获取到当前Activity的Window
        Window dialog_window = dialog.getWindow();
        dialog_window.setContentView(recyclerView);
        dialog_window.getDecorView().setPadding(0, 0, 0, 0);
        //设置对话框的位置
        dialog_window.setGravity(Gravity.CENTER | Gravity.TOP);
        //获取到LayoutParams
        WindowManager.LayoutParams dialog_window_attributes = dialog_window.getAttributes();
        dialog_window_attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_window_attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置对话框位置的偏移量
        dialog_window_attributes.x = 0;
        dialog_window_attributes.y = (int) (v.getY() + v.getBottom());
        dialog_window.setAttributes(dialog_window_attributes);
//        dialog.show();
    }

}
