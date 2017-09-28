package com.example.zr.gxapplication.net.model;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.shandianshua.base.utils.SystemUtils;
import com.shandianshua.totoro.activity.LaunchTaskActivity;
import com.shandianshua.totoro.activity.WebActivity;
import com.shandianshua.totoro.event.model.Channel;
import com.shandianshua.totoro.eventbus.BusProvider;
import com.shandianshua.totoro.fragment.agent.AgentDetailFragment;
import com.shandianshua.totoro.fragment.detail.IncomeDetailFragment;
import com.shandianshua.totoro.fragment.detail.WithdrawFragment;
import com.shandianshua.totoro.fragment.main.MainFragment;
import com.shandianshua.totoro.fragment.main.TabFragment;
import com.shandianshua.totoro.utils.ChannelUtils;
import com.shandianshua.totoro.utils.SwitchFragmentUtil;

import java.io.Serializable;

/**
 * author: zhou date: 2016/5/9.
 */
public class BroadcastAdItem implements Serializable {
  public static final int HOME_BANNER_TYPE = 1001;

  private static final String ACTION_WEB_VIEW = "web_view";
  private static final String ACTION_BROWSER = "browser";

  private static final String ACTION_PAGE_INVITE = "page_invite";
  private static final String ACTION_PAGE_WITHDRAW = "page_withdraw";
  private static final String ACTION_PAGE_MINE = "page_mine";
  private static final String ACTION_PAGE_CHECK_IN = "page_check_in";
  private static final String ACTION_PAGE_INCOME = "page_income";

  private static final String ACTION_ENTER_CHANNEL = "enter_channel";
  private static final String ACTION_ENTER_PACKAGE = "enter_package";
  private static final String ACTION_ENTER_APP_CHECKIN = "enter_app_check_in";

  private static final String ACTION_INCOME_FRAGMENT = "income_fragment";
  private static final String ACTION_AGENT_TASK = "agent_task";

  public String image;
  public String action;
  public String url;
  public String title;

  public void invokeAction(Context context) {
    if (TextUtils.isEmpty(action)) {
      return;
    }
    switch (action) {
      case ACTION_PAGE_INVITE:
        BusProvider.getInstance().post(TabFragment.SwitchFragmentEvent.INVITE);
        break;
      case ACTION_PAGE_WITHDRAW:
        SwitchFragmentUtil.switchFragment(context, new WithdrawFragment());
        break;
      case ACTION_PAGE_MINE:
        BusProvider.getInstance().post(TabFragment.SwitchFragmentEvent.MINE);
        break;
      case ACTION_PAGE_INCOME:
        SwitchFragmentUtil.switchFragment(context, new IncomeDetailFragment());
        break;
      case ACTION_PAGE_CHECK_IN:
        context.startActivity(LaunchTaskActivity.getIntent(context));
        break;
      case ACTION_ENTER_APP_CHECKIN:
        context.startActivity(LaunchTaskActivity.getIntent(context));
        break;
      case ACTION_ENTER_CHANNEL:
        Channel channel = Channel.valueOf(url);
        if (channel != null && context instanceof Activity) {
          ChannelUtils.enterChannel((Activity) context, channel);
        }
        break;
      case ACTION_ENTER_PACKAGE:
        String packageName = url;
        if (!TextUtils.isEmpty(packageName)) {
          BusProvider.getInstance().post(new MainFragment.EnterPackageEvent(packageName));
        }
        break;
      case ACTION_BROWSER:
        if (!TextUtils.isEmpty(url)) {
          SystemUtils.openBrowser(context, url);
        }
        break;
      case ACTION_INCOME_FRAGMENT:
        BusProvider.getInstance().post(TabFragment.SwitchFragmentEvent.INCOME);
        break;
      case ACTION_AGENT_TASK:
        if (url != null) {
          try {
            Bundle bundle = new Bundle();
            bundle.putLong(AgentConfigEnum.TASK_NO, Long.valueOf(url));
            SwitchFragmentUtil.switchAgentFragment(context, new AgentDetailFragment(), bundle);
          } catch (ClassCastException e) {

          }
        }
        break;
      case ACTION_WEB_VIEW:
      default:
        if (!TextUtils.isEmpty(url)) {
          WebActivity.launch(context, title, url);
        }
        break;
    }
  }

  public String getDisplayAction() {
    return action + (!TextUtils.isEmpty(title) ? (":" + title) :
        !TextUtils.isEmpty(url) ? (":" + url) : "");
  }
}
