package com.joh.xmlyscroll;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 仿喜马拉雅FM详情页滑动（滑动监听实现）
 * 
 * @author : Joh Liu
 * @date : 2019/7/22 15:33
 */
public class ScrollCustomActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.layout_tab)
    LinearLayout layout;

    @BindView(R.id.layout_one)
    LinearLayout layoutOne;
    @BindView(R.id.layout_two)
    LinearLayout layoutTwo;
    @BindView(R.id.layout_three)
    LinearLayout layoutThree;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    int y1 = 0;
    int y2 = 0;
    int y3 = 0;
    int yh = 0;

    @BindView(R.id.layout_toolbar)
    RelativeLayout layoutTool;
    @BindView(R.id.layout_top)
    LinearLayout layout_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_custom);
        ButterKnife.bind(this);

        //初始化tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("详情"));
        tabLayout.addTab(tabLayout.newTab().setText("主播"));
        tabLayout.addTab(tabLayout.newTab().setText("评论"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                chagePager(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView,
                                       int scrollX, int scrollY,
                                       int oldScrollX, int oldScrollY) {

                /**
                 * 测量view当前的高度
                 */
                int h1 = layoutOne.getHeight();
                int h2 = layoutTwo.getHeight();
                int h3 = layoutThree.getHeight();
                int ht = layout_top.getHeight();

                int offset = ht - (dp2px(48 + 56) + 1);

                if (scrollY >= offset) {
                    layout.setVisibility(View.VISIBLE);
                    tvTitle.setText("测试");
                    layoutTool.setBackgroundResource(android.R.color.white);
                } else {
                    layout.setVisibility(View.GONE);
                    tvTitle.setText("");
                    layoutTool.setBackgroundResource(R.color.transparent);
                }
                if (scrollY >= offset && scrollY < offset + h1) {
                    setScrollPosition(0);
                } else if (scrollY >= offset + h1 && scrollY < offset + h1 + h2) {
                    setScrollPosition(1);
                } else if (scrollY >= offset + h1 + h2 && scrollY <= offset + h1 + h2 + h3) {
                    setScrollPosition(2);
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
        if (position == 0) {
            scrollView.scrollTo(0, y1);
        } else if (position == 1) {
            scrollView.scrollTo(0, y2);
        } else if (position == 2) {
            scrollView.scrollTo(0, y3);
        }
    }

    /**
     * Set the scroll position of the tabs.
     *
     * @param position current scroll position
     */
    private void setScrollPosition(int position) {
        tabLayout.setScrollPosition(position, 0, true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        /**
         * 测量view顶部距离父容器的Y轴距离
         */
        //稍微调整偏移量
        int topH = dp2px(48 + 56) + 1;

        Log.e("TAG", "topH=" + layout_top.getHeight());
        Log.e("TAG", "topH2=" + dp2px(48 + 56) + 1);
        y1 = layoutOne.getTop() - topH;
        y2 = layoutTwo.getTop() - topH;
        y3 = layoutThree.getTop() - topH;

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * convert dp to its equivalent px
     * <p>
     * 将dp转换为与之相等的px
     */
    public int dp2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
