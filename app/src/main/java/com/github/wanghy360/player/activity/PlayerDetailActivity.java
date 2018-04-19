package com.github.wanghy360.player.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.wanghy360.hyvideolibrary.util.LogPrint;
import com.github.wanghy360.player.R;
import com.github.wanghy360.player.adapter.SlidTabViewPagerAdapter;
import com.github.wanghy360.player.bean.PlayerBean;
import com.github.wanghy360.player.bean.FragmentTabItem;
import com.github.wanghy360.player.fragment.PlayFragment;
import com.github.wanghy360.player.fragment.TabFragment;
import com.github.wanghy360.player.ui.MagicLayout;
import com.github.wanghy360.player.util.CustomOrientationListener;
import com.github.wanghy360.player.util.DensityUtils;
import com.github.wanghy360.player.util.RingListenerTool;
import com.github.wanghy360.player.util.keyboard.KeyboardHeightObserver;
import com.github.wanghy360.player.util.keyboard.KeyboardHeightProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Wanghy on 2017/4/24
 */
public class PlayerDetailActivity extends BaseActivity implements KeyboardHeightObserver {
    @BindView(R.id.id_play_container)
    RelativeLayout videoContainer;
    @BindView(R.id.id_play_top_slidtab)
    PagerTabStrip pagerTabStrip;
    @BindView(R.id.id_play_viewpager)
    ViewPager viewPager;
    @BindView(R.id.id_play_container_layout)
    RelativeLayout tabContainer;
    //竖屏工具栏
    @BindView(R.id.id_tool_container)
    RelativeLayout rightToolContainer;
    @BindView(R.id.id_switch_orientation)
    ImageView switchOrientationBtn;
    @BindView(R.id.id_root_view)
    RelativeLayout rootView;

    //横屏顶部工具栏
    @BindView(R.id.id_top_land)
    RelativeLayout topToolContainer;
    @BindView(R.id.id_back)
    TextView backBtn;
    //横屏底部工具栏背景
    @BindView(R.id.id_bottom_land)
    View bottomToolBg;
    //是否为横屏
    private boolean isLand = false;
    //屏幕宽度
    private int screenWidth;
    //数据
    private PlayerBean mPlayerBean;
    //Tab Adapter
    private SlidTabViewPagerAdapter tabAdapter;
    //Fragment
    private PlayFragment playFragment;

    //The keyboard height provider
    private KeyboardHeightProvider keyboardHeightProvider;

    //工具栏高度
    private int toolHeight;
    //Magic
    @BindView(R.id.id_magic_view)
    MagicLayout magicView;

    //当前Tab位置
    private int currTabPosition = 0;
    //NavigationBar
    private boolean hasNavigationBar = false;
    //来电监听
    private RingListenerTool ringListenerTool;

    private Handler handler;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_detail);
        unbinder = ButterKnife.bind(this);
        mPlayerBean = new PlayerBean();
        mPlayerBean.setUrl("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        initView();
        initTool();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();
        handler = new Handler();
        toolHeight = getResources().getDimensionPixelSize(R.dimen.half_room_tool_height);
        keyboardHeightProvider = new KeyboardHeightProvider(this);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                keyboardHeightProvider.start();
            }
        });

        screenWidth = DensityUtils.getScreenWidth(this);
        hasNavigationBar = DensityUtils.checkDeviceHasNavigationBar(this);
        updateVideoSize(isLand);

        magicView.setToolHeight(toolHeight);

        playFragment = PlayFragment.newInstance(mPlayerBean);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.id_play_container, playFragment);
        transaction.commitNow();

        List<FragmentTabItem> mGameFragmentList = new ArrayList<>();
        mGameFragmentList.add(new FragmentTabItem(getString(R.string.tab_chat), TabFragment.newInstance(getString(R.string.tab_chat))));
        mGameFragmentList.add(new FragmentTabItem(getString(R.string.tab_detail), TabFragment.newInstance(getString(R.string.tab_detail))));
        mGameFragmentList.add(new FragmentTabItem(getString(R.string.tab_rank), TabFragment.newInstance(getString(R.string.tab_rank))));
        mGameFragmentList.add(new FragmentTabItem(getString(R.string.tab_recommend), TabFragment.newInstance(getString(R.string.tab_recommend))));
        viewPager.setOffscreenPageLimit(mGameFragmentList.size());
        tabAdapter = new SlidTabViewPagerAdapter(getSupportFragmentManager(), null);
        viewPager.setAdapter(tabAdapter);
        pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.transparent));
        pagerTabStrip.setTextColor(getResources().getColor(R.color.app_font_t2_color));
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.app_base_color));
        pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tabAdapter.setList(mGameFragmentList);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!isLand) {
                    if (position == 0) {
                        magicView.setTranslationX(-positionOffsetPixels);
                    } else {
                        magicView.setTranslationX(-screenWidth);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                currTabPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        hideToolDelay();
    }

    private CustomOrientationListener orientationListener;

    public void initTool() {
        orientationListener = new CustomOrientationListener(PlayerDetailActivity.this) {
            @Override
            public void onLandStateChanged(int screenOrientation) {
                if (isLand) {
                    changeOrientation(screenOrientation);
                }
            }
        };
        ringListenerTool = new RingListenerTool();
        ringListenerTool.registerReceiver(this);
    }

    private boolean isToolShown = true;
    private static final int TOOL_SHOW_MILLIS = 10 * 1000;
    private Runnable showToolRunable;

    @OnClick(R.id.id_play_container)
    public void onClickPlayer() {//点击背景，显示隐藏工具栏
        if (videoContainer.isEnabled()) {
            hideOrShowTool(!isToolShown);
        }
    }

    public void hideOrShowTool(boolean showTool) {
        hideOrShowTool(showTool, false);
    }

    public void hideOrShowTool(boolean showTool, boolean keepChat) {
        if (isKeyboardVisible)
            return;
        LogPrint.d(TAG, "showTool:" + showTool + " isLand:" + isLand);
        isToolShown = showTool;
        if (magicView != null)
            magicView.updateShowTool(showTool);
        if (showToolRunable != null)
            handler.removeCallbacks(showToolRunable);
        if (isLand) {
            videoContainer.setEnabled(false);
            float translateY = bottomToolBg.getTranslationY();
            if (showTool) {
                showView(topToolContainer, true);
                topToolContainer.animate().setInterpolator(new AccelerateInterpolator()).translationY(0).setDuration(300).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        videoContainer.setEnabled(true);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        videoContainer.setEnabled(true);
                    }
                }).start();
                if (!keepChat && translateY > 0.0f) {
                    bottomToolBg.animate().setInterpolator(new AccelerateInterpolator()).translationY(0).setDuration(300).start();
                    if (magicView != null)
                        magicView.showChatInput();

                }
                hideToolDelay();
            } else {
                topToolContainer.animate().setInterpolator(new DecelerateInterpolator()).translationY(-toolHeight).setDuration(300).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        videoContainer.setEnabled(true);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        videoContainer.setEnabled(true);
                    }
                }).start();
                if (!keepChat && translateY == 0.0f) {
                    bottomToolBg.animate().setInterpolator(new DecelerateInterpolator()).translationY(toolHeight).setDuration(300).start();
                    if (magicView != null)
                        magicView.hideChatInput();
                }
            }
            if (keepChat) {
                hideToolDelay();
            }
        } else {
            if (showTool) {
                showView(topToolContainer, true);
                showView(rightToolContainer, true);
                hideToolDelay();
            } else {
                showView(topToolContainer, false);
                showView(rightToolContainer, false);
            }
        }
    }

    protected void showView(View view, boolean show) {
        if (view != null) {
            view.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void hideToolDelay() {
        if (showToolRunable == null) {
            showToolRunable = new Runnable() {
                @Override
                public void run() {
                    hideOrShowTool(false);
                }
            };
        }
        handler.postDelayed(showToolRunable, TOOL_SHOW_MILLIS);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isAboveInput(ev)) {
                if (isKeyboardVisible)
                    magicView.hideSoftInputWindow();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isAboveInput(MotionEvent event) {//点击在colorContainer之上隐藏输入法弹框
        int[] leftTop = {0, 0};
        magicView.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        return !(event.getX() > left && event.getY() > top);
    }

    private boolean isRootShrunken = false;

    @Override
    public void onKeyboardHeightChanged(int height) {
        magicView.translateInput(height);
        if (height > 0) {
            if (hasNavigationBar && isLand) {
                isRootShrunken = true;
                ViewGroup.LayoutParams lp = rootView.getLayoutParams();
                lp.width = DensityUtils.getScreenWidth(PlayerDetailActivity.this);
                rootView.setLayoutParams(lp);
            }
            keyboardVisibleChanged(true);
        } else {
            if (hasNavigationBar && isLand && isRootShrunken) {
                isRootShrunken = false;
                hideSystemUI();
                ViewGroup.LayoutParams lp = rootView.getLayoutParams();
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                rootView.setLayoutParams(lp);
            }
            keyboardVisibleChanged(false);
        }
    }

    @OnClick(R.id.id_switch_orientation)
    public void onClickSwitchOrientation() {
        changeOrientation();
    }

    @OnClick(R.id.id_back)
    public void onClickBack() {
        if (isLand) {
            changeOrientation();
        } else {
            finish();
        }
    }

    private Runnable connectChatRoom;

    private void updateVideoSize(boolean isLand) {
        if (videoContainer != null) {
            if (isLand) {//横屏
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) videoContainer.getLayoutParams();
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoContainer.setLayoutParams(lp);
                hideSystemUI();
            } else {//竖屏
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) videoContainer.getLayoutParams();
                lp.width = screenWidth;
                lp.height = (screenWidth * 9) / 16;
                videoContainer.setLayoutParams(lp);
                showSystemUI();
            }
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideSystemUI() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * 显示虚拟按键，退出全屏
     */
    protected void showSystemUI() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.VISIBLE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isLand = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
        LogPrint.d(TAG, "onConfigurationChanged: isLand:" + isLand);
        updateVideoSize(isLand);
        updateViewByOrientation(isLand);
        magicView.updateOrientation(isLand);
    }

    private void changeOrientation() {
        if (isLand) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            orientationListener.setRealState(CustomOrientationListener.State.NOT_LAND);
        } else {
            if (orientationListener.isTendLand()) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                orientationListener.setRealState(CustomOrientationListener.State.LAND);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                orientationListener.setRealState(CustomOrientationListener.State.REVERSE_LAND);
            }
        }
    }

    private void changeOrientation(int orientation) {
        setRequestedOrientation(orientation);
    }

    private void updateViewByOrientation(boolean isLand) {
        if (isLand) {
            magicView.setTranslationX(0);
            switchOrientationBtn.setVisibility(View.GONE);
            //顶部工具栏
            topToolContainer.setBackgroundResource(R.drawable.bg_play_top);
            //底部工具栏背景
            showView(bottomToolBg, true);
        } else {
            if (currTabPosition != 0) {
                magicView.setTranslationX(-screenWidth);
            }
            switchOrientationBtn.setVisibility(View.VISIBLE);

            //系统navi
            if (isRootShrunken) {
                isRootShrunken = false;
            }

            topToolContainer.setTranslationY(0);
            backBtn.setText("");
            //顶部工具栏
            topToolContainer.setBackgroundColor(Color.TRANSPARENT);
            //底部工具栏背景
            showView(bottomToolBg, false);
        }
        hideOrShowTool(true);
    }

    private boolean isKeyboardVisible = false;

    private void keyboardVisibleChanged(boolean isVisible) {
        if (isKeyboardVisible == isVisible)
            return;
        isKeyboardVisible = isVisible;
        magicView.updateKeyboardVisibility(isVisible);
        magicView.updateChatView(isKeyboardVisible);
        if (!isVisible) {
            hideToolDelay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardHeightProvider.setKeyboardHeightObserver(null);
        orientationListener.disable();
    }

    @Override
    public void onResume() {
        super.onResume();
        keyboardHeightProvider.setKeyboardHeightObserver(this);
        orientationListener.enable();
    }

    private void clearDataAndView() {
        magicView.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearDataAndView();
        if (connectChatRoom != null)
            handler.removeCallbacks(connectChatRoom);
        connectChatRoom = null;
        if (keyboardHeightProvider != null) {
            keyboardHeightProvider.dismiss();
            keyboardHeightProvider.clear();
        }
        if (ringListenerTool != null)
            ringListenerTool.unregisterReceiver(this);
        magicView.unBind();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onBackPressed() {
        if (isLand) {
            changeOrientation();
        } else {
            super.onBackPressed();
        }
    }
}
