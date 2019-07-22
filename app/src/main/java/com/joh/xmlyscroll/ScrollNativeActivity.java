package com.joh.xmlyscroll;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 仿喜马拉雅FM详情页滑动（CoordinatorLayout实现）
 */
public class ScrollNativeActivity extends AppCompatActivity {

    /**
     * toolbar
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    /**
     * 上滑折叠隐藏部分
     */
    @BindView(R.id.layout_top)
    LinearLayout layoutTop;
    /**
     * 上滑显示部分（展开时隐藏）
     */
    @BindView(R.id.layout_tab)
    LinearLayout layoutTab;
    @BindView(R.id.tabs)
    CommonTabLayout tabLayout;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    /**
     * ScrollView部分
     */
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.layout_one)
    LinearLayout layoutOne;
    @BindView(R.id.layout_two)
    LinearLayout layoutTwo;
    @BindView(R.id.layout_three)
    LinearLayout layoutThree;

    int y1 = 0;
    int y2 = 0;
    int y3 = 0;

    //是否是点击事件
    boolean isClick = false;

    private String[] mTitles = {"详情", "主播", "评论"};
    private int[] mIconUnselectIds = {
            R.mipmap.ic_arrow, R.mipmap.ic_arrow,
            R.mipmap.ic_arrow, R.mipmap.ic_arrow};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_native);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        //设置AppBarLayout的监听
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    tvTitle.setText("");
                    layoutTab.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    tvTitle.setText("测试");
                    layoutTab.setVisibility(View.VISIBLE);
                } else {
                    //中间状态
                    tvTitle.setText("");
                    layoutTab.setVisibility(View.GONE);
                }
            }
        });

        //初始化tabLayout
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconUnselectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (isScrollOver(scrollView)) {
                    chagePager(position);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView,
                                       int scrollX, int scrollY,
                                       int oldScrollX, int oldScrollY) {

                if (!isClick) {
                    /**
                     * 测量view当前的高度
                     */
                    int h1 = layoutOne.getHeight();
                    int h2 = layoutTwo.getHeight();
                    int h3 = layoutThree.getHeight();

                    if (scrollY >= 0 && scrollY < h1) {
                        setScrollPosition(0);
                    } else if (scrollY >= h1 && scrollY < (h1 + h2)) {
                        setScrollPosition(1);
                    } else if (scrollY >= (h1 + h2) && scrollY < ((h1 + h2 + h3))) {
                        setScrollPosition(2);
                    }
                }
            }
        });
    }

    /**
     * scrollView滑动到指定位置
     *
     * @param position
     */
    private void chagePager(int position) {
        isClick = true;
        if (position == 0) {
            scrollView.scrollTo(0, y1);
        } else if (position == 1) {
            scrollView.scrollTo(0, y2);
        } else if (position == 2) {
            scrollView.scrollTo(0, y3);
        }
        isClick = false;
    }

    /**
     * Set the scroll position of the tabs.
     *
     * @param position current scroll position
     */
    private void setScrollPosition(int position) {
        tabLayout.setCurrentTab(position);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        /**
         * 测量view顶部距离父容器的Y轴距离
         * y1,y2,y3分别代表详情、主播、评论距离父控件的高度，也就是其对应滑动偏移量
         */
        y1 = layoutOne.getTop();
        y2 = layoutTwo.getTop();
        y3 = layoutThree.getTop();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 获取NestedScrollView是否滑动完成
     *
     * @param scrollView
     * @return
     */
    public static boolean isScrollOver(NestedScrollView scrollView) {
        try {
            if (scrollView != null) {
                Field mScroller = scrollView.getClass().getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                Object object = mScroller.get(scrollView);
                if (object instanceof OverScroller) {
                    OverScroller overScroller = (OverScroller) object;
                    return overScroller.isFinished();
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

}
