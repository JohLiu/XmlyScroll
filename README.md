# Android 仿喜马拉雅播放详情页滑动效果 （方案一）

标签（空格分隔）： Android 滑动效果

---

###前言
用过喜马拉雅APP的应该知道，它的播放详情页与淘宝京东这些购物应用的商品详情页的上拉展示效果是不一样的，所以就想着仿写一下喜马拉雅的这种效果。

###详情页滑动效果分析
> * 未折叠情况：页面展示的有一个头布局（包含音频名、图片、播放进度、播放设置等）和音频详细信息（需下拉才能看到全部）；
> * 折叠情况：头布局隐藏之后，显示详细信息（包含详情、主播、评论三个小版块）并展示Tab栏。

由分析可知，当头布局信息隐藏后，音频的详细信息分为三个Tab来展示，并显示Tab栏；当再次下滑，展示出头布局时，Tab栏隐藏。
除上述变化外，ToolBar栏也会跟随滑动变化，未折叠情况ToolBar栏是透明，折叠后Toobar变成不透明且会显示播放按钮。

###实现方案
分析之后，决定采用原生的滑动嵌套布局方案，这样可以省去很多麻烦，在 `Android Studio` 中创建项目时，选择创建 `ScrollingActivity` mobile，然后打开布局文件加以修改。

activity_scrolling.xml
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:context="com.joh.ximalayademo.ScrollingActivity">

    <android.support.design.widget.AppBarLayout
        android:id="@+id/app_bar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fitsSystemWindows="true"
        android:stateListAnimator="@null"
        android:theme="@style/AppTheme.AppBarOverlay">

        <android.support.design.widget.CollapsingToolbarLayout
            android:id="@+id/toolbar_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:fitsSystemWindows="true"
            app:contentScrim="?attr/colorPrimary"
            app:layout_scrollFlags="scroll|exitUntilCollapsed|snap"
            app:toolbarId="@+id/toolbar">

            <!--上滑时隐藏部分-->
            <LinearLayout
                android:id="@+id/layout_top"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="#fff"
                android:orientation="vertical">

                <ImageView
                    android:layout_width="match_parent"
                    android:layout_height="@dimen/dimen_400"
                    android:layout_marginTop="@dimen/dimen_56"
                    android:scaleType="centerCrop"
                    android:src="@mipmap/img_header" />
            </LinearLayout>

            <android.support.v7.widget.Toolbar
                android:id="@+id/toolbar"
                style="@style/searchToolbar"
                android:layout_width="match_parent"
                android:layout_height="@dimen/dimen_56"
                android:background="#33ffffff"
                app:layout_collapseMode="pin">

                <RelativeLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">

                    <ImageView
                        android:id="@+id/iv_back"
                        android:layout_width="56dp"
                        android:layout_height="56dp"
                        android:layout_alignParentLeft="true"
                        android:layout_centerVertical="true"
                        android:padding="12dp"
                        android:src="@mipmap/icon_left" />

                    <ImageView
                        android:layout_width="56dp"
                        android:layout_height="56dp"
                        android:layout_alignParentEnd="true"
                        android:layout_centerVertical="true"
                        android:padding="12dp"
                        android:src="@mipmap/icon_right" />

                    <TextView
                        android:id="@+id/tv_title"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_centerInParent="true"
                        android:text=""
                        android:textColor="#000"
                        android:textSize="16sp" />

                </RelativeLayout>
            </android.support.v7.widget.Toolbar>
        </android.support.design.widget.CollapsingToolbarLayout>
        
        <!--上滑时展示部分-->
        <LinearLayout
            android:id="@+id/layout_tab"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:visibility="gone">

            <View
                android:layout_width="match_parent"
                android:layout_height="1px"
                android:background="#ddd" />

            <com.flyco.tablayout.CommonTabLayout
                android:id="@+id/tabs"
                android:layout_width="match_parent"
                android:layout_height="48dp"
                android:background="#fff"
                app:tl_iconVisible="false"
                app:tl_indicator_color="#FA593B"
                app:tl_indicator_height="1.5dp"
                app:tl_textSelectColor="#FA593B"
                app:tl_textUnselectColor="#000"
                app:tl_textsize="15sp" />

            </android.support.design.widget.TabLayout>
        </LinearLayout>

    </android.support.design.widget.AppBarLayout>

    <include layout="@layout/content_scrolling" />

</android.support.design.widget.CoordinatorLayout>
```

content_scrolling.xml
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.v4.widget.NestedScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/scrollView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layout_behavior="@string/appbar_scrolling_view_behavior"
    tools:context="com.joh.ximalayademo.ScrollingActivity"
    tools:showIn="@layout/activity_scrolling">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <LinearLayout
            android:id="@+id/layout_one"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="1000dp"
                android:scaleType="fitCenter"
                android:src="@mipmap/img_010" />

        </LinearLayout>

        <LinearLayout
            android:id="@+id/layout_two"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="300dp"
                android:scaleType="fitXY"
                android:src="@mipmap/icon_0010" />

        </LinearLayout>

        <LinearLayout
            android:id="@+id/layout_three"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="1000dp"
                android:scaleType="fitXY"
                android:src="@mipmap/img_00010" />

        </LinearLayout>


    </LinearLayout>


</android.support.v4.widget.NestedScrollView>
```
布局完善之后，代码部分的实现也就不难了，只要对控件的滑动监听理清晰就可以了。

####1.监听AppBarLayout
对AppBarLayout的监听主要是知道当前控件是折叠状态还是展开状态,根据不同的状态来控制Tab栏的是否显示

```
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
```
####2.对TabLayout监听
对TabLayout的监听是主要用于控制ScrollView的滑动，在控制ScrollView滑动之前，需要在onWindowFocusChanged()中获取ScrollView的子View距离父控件的Top距离。

获取距离父控件的高度
```
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
```
TabLayout监听
```
 tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                chagePager(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

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
```

现在已经初步实现折叠和展开情况下布局的变更了，但是现在ScrollView的滑动监听还未实现，对应的ScrollView滑动到指定位置，TabLayout的指示器也对应变更。
```
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
```




