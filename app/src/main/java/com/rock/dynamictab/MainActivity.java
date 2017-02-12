package com.rock.dynamictab;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rock.dynamictab.adapters.TeachAdapter;
import com.rock.dynamictab.callback.ItemTouchCallback;
import com.rock.rock.recyclerviewtorecyclerview.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TeachAdapter.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerSrc;
    private RecyclerView mRecyclerDst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mRecyclerSrc = (RecyclerView) findViewById(R.id.teach_src);
        mRecyclerDst = (RecyclerView) findViewById(R.id.teach_dst);
        GridLayoutManager layoutManagerSrc = new GridLayoutManager(this, 4);
        GridLayoutManager layoutManagerDst = new GridLayoutManager(this, 4);
        mRecyclerSrc.setLayoutManager(layoutManagerSrc);
        mRecyclerDst.setLayoutManager(layoutManagerDst);
        TeachAdapter srcAdapter = new TeachAdapter(this, getDataSrc());
        mRecyclerSrc.setAdapter(srcAdapter);
        srcAdapter.setListener(this);
        TeachAdapter detAdapter = new TeachAdapter(this, getDataDst());
        mRecyclerDst.setAdapter(detAdapter);
        detAdapter.setListener(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchCallback(srcAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerSrc);
    }

    private List<String> getDataDst() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            data.add("豆饼"+i);
        }
        return data;
    }

    private List<String> getDataSrc() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("猴子" + i);
        }
        return data;
    }

    @Override
    public void onItemClick(RecyclerView recycler,View v, int position) {
        switch (recycler.getId()) {
            case R.id.teach_src:
                clickSrc(v,position,true);
                break;
            case R.id.teach_dst:
                clickDst(v,position,false);
                break;
        }
    }



    private void clickSrc(View v,int position,boolean srcToDst) {
        /**
         *  先获取点击View的位置
         *
         *  计算目标位置
         *      SrcRecyclerView操作数据刚好换行时 需要进行一下特殊处理
         *
         *  动画飞过去
         *
         */

        int[] srcPosition = new int[2];
        v.getLocationOnScreen(srcPosition);
        int x = srcPosition[0];
        Log.e(TAG, "onItemClick: x:"+ x);
        int y = srcPosition[1];
        Log.e(TAG, "onItemClick: y" + y);

        CharSequence text = ((TextView) ((LinearLayout) v).getChildAt(0)).getText();

        /**
         *
         * 需要处理特殊情况
         *      没有Child的情况
         *      刚好需要换行的情况
         *
         *      在同行计算情况
         */
        int[] dstPosition = new int[2];
        int itemCount = mRecyclerDst.getAdapter().getItemCount();
        int i = itemCount % 4;
        if (i != 0) {
            // 就是直接向最后一个的后面添加
            Log.e(TAG, "onItemClick: 不是最后一个" );
            View childAt = mRecyclerDst.getChildAt(itemCount-1);
            childAt.getLocationOnScreen(dstPosition);
            Log.e(TAG, "onItemClick: toX" + dstPosition[0] );
            Log.e(TAG, "onItemClick: toY" + dstPosition[1] );

            dstPosition[0] += mRecyclerDst.getChildAt(0).getRight();

        }else{
            // 也会有两种情况，没有child，有整数倍的child
            Log.e(TAG, "onItemClick: 当前行没有位置了" );
            if (itemCount == 0) {
                mRecyclerDst.getLocationOnScreen(dstPosition);
            }else{
                View childAt = mRecyclerDst.getChildAt(0);
                childAt.getLocationOnScreen(dstPosition);
                int count = itemCount / 4;
                dstPosition[1] += count*childAt.getBottom();
            }
        }

        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.e(TAG, "onItemClick: " + statusBarHeight );

        if (srcToDst){
            String item = ((TeachAdapter) mRecyclerSrc.getAdapter()).getItem(position);
            ((TeachAdapter) mRecyclerDst.getAdapter()).onItemInserted(item);

            animationFromTo(text,position,true,x,dstPosition[0],y-statusBarHeight,dstPosition[1]-statusBarHeight);

        }
    }



    private void clickDst(View v,int position,boolean srcToDst) {

        /**
         *  先计算目标位置
         *
         *  获取点击位置
         *
         *  动画飞过去
         *
         */
        String item = ((TeachAdapter) mRecyclerDst.getAdapter()).getItem(position);
        ((TeachAdapter) mRecyclerSrc.getAdapter()).onItemInserted(item);

        int[] srcPosition = new int[2];
        v.getLocationOnScreen(srcPosition);
        int x = srcPosition[0];
        Log.e(TAG, "onItemClick: x:"+ x);
        int y = srcPosition[1];
        Log.e(TAG, "onItemClick: y" + y);

        /**
         *
         * 需要处理特殊情况
         *      没有Child的情况
         *      刚好需要换行的情况
         *
         *      在同行计算情况
         */
        int[] dstPosition = new int[2];
        int itemCount = mRecyclerSrc.getChildCount();
        int i = itemCount % 4;
        if (i != 0) {
            // 就是直接向最后一个的后面添加
            Log.e(TAG, "onItemClick: 不是最后一个" );
            View childAt = mRecyclerSrc.getChildAt(itemCount-1);
            childAt.getLocationOnScreen(dstPosition);
            Log.e(TAG, "onItemClick: toX" + dstPosition[0] );
            Log.e(TAG, "onItemClick: toY" + dstPosition[1] );

            dstPosition[0] += mRecyclerSrc.getChildAt(0).getRight();

        }else{
            // 也会有两种情况，没有child，有整数倍的child
            Log.e(TAG, "onItemClick: 当前行没有位置了" );
            if (itemCount == 0) {
                mRecyclerSrc.getLocationOnScreen(dstPosition);
            }else{
                View childAt = mRecyclerSrc.getChildAt(0);
                childAt.getLocationOnScreen(dstPosition);
                int count = itemCount / 4;
                dstPosition[1] += count*childAt.getBottom();
                srcPosition[1] += childAt.getBottom();
            }
        }

        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.e(TAG, "onItemClick: " + statusBarHeight );

        CharSequence text = ((TextView) ((LinearLayout) v).getChildAt(0)).getText();

        animationFromTo(text,position,false,x,dstPosition[0],srcPosition[1]-statusBarHeight,dstPosition[1]-statusBarHeight);

    }


    public void animationFromTo(CharSequence text, final int position, final boolean srcToDst, int xFrom, int xTo, int yFrom, int yTo){
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        final View layout = LayoutInflater.from(this).inflate(R.layout.src_to_dst, null);
        TextView textView = (TextView) layout.findViewById(R.id.teach_dema);
        int margin = (int) (getResources().getDisplayMetrics().density * 10);
        ((LinearLayout.LayoutParams) textView.getLayoutParams()).bottomMargin = margin;
        ((LinearLayout.LayoutParams) textView.getLayoutParams()).topMargin = margin;
        ((LinearLayout.LayoutParams) textView.getLayoutParams()).leftMargin = margin;
        ((LinearLayout.LayoutParams) textView.getLayoutParams()).rightMargin = margin;
        textView.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / ((GridLayoutManager) mRecyclerSrc.getLayoutManager()).getSpanCount() - margin * 2;
        textView.setText(text);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        params.format = PixelFormat.TRANSLUCENT;

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        windowManager.addView(layout,params);
        TranslateAnimation animation = new TranslateAnimation(xFrom, xTo, yFrom, yTo);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                windowManager.removeView(layout);

                if (srcToDst) {
                    ((TeachAdapter) mRecyclerSrc.getAdapter()).onItemRemoved(position);
                }else{
                    ((TeachAdapter) mRecyclerDst.getAdapter()).onItemRemoved(position);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textView.startAnimation(animation);
    }

}
