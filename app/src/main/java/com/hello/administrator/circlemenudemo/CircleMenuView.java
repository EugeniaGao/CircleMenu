package com.hello.administrator.circlemenudemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/1/15.
 */

public class CircleMenuView extends ViewGroup {

    private View viewById;
    //private float startAngle;
    private float lastX;
    private float lastY;
   //继承ViewGroup，自定义view串联构造
    public CircleMenuView(Context context) {
        this(context,null);
    }

    public CircleMenuView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }


    int d;
    @Override //1.测量
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mesureWidth;
        int mesureHeight;//定义测量结果的宽高
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //需求:不管用户传递过来的值是什么,我都要全部的
        //逻辑1:如果是确切的值取屏幕和他的最小值
        //如果是未指定的或是至多的,就取背景值,没有背景取屏幕值
     int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode != MeasureSpec.EXACTLY){
            //未指定的或是至多
            int suggestedMinimumWidth = getSuggestedMinimumWidth();
            if (suggestedMinimumWidth ==0){
                int screenWidth = getDefaultWidth();
                mesureWidth =mesureHeight=screenWidth;
            }else{
                mesureWidth =mesureHeight=Math.min(suggestedMinimumWidth, getDefaultWidth());
            }
        }else{
            //有确切的值
            int screenWidth = getDefaultWidth();
            mesureWidth =mesureHeight= Math.min(size, screenWidth);
        }

        //设置最终的布局,d是父控件矩形也就是原所在矩形的宽度
        d =mesureWidth;
        setMeasuredDimension(d,mesureHeight);
        /*测量子孩子*/
        //通过父亲的测量来测量孩子的位置
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int makeMeasureSpec = MeasureSpec.makeMeasureSpec(d / 3, MeasureSpec.EXACTLY);
           child.measure(makeMeasureSpec,makeMeasureSpec);

        }
    }
   private  int startAngle;
    @Override //2.进行布局
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
       //孩子相对于父亲的布局
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //获取孩子的宽度
            int childMeasuredWidth = child.getMeasuredWidth();
            float temp = d/3.0f;
            int left = (int) (d/2+Math.round(temp*Math.cos(Math.toRadians(startAngle)))-childMeasuredWidth/2);
            int right=left+childMeasuredWidth;
            /*bottom =(int) (d/2+Math.round(temp*Math.sin(Math.toRadians(startAngle)))+childMeasuredWidth/2);
            int top=bottom-childMeasuredWidth;*/
            int top = (int) (d/2+Math.round(temp*Math.sin(Math.toRadians(startAngle)))-childMeasuredWidth/2);
            int bottom =top+childMeasuredWidth;
            child.layout(left,top,right,bottom);//子孩子相对于父布局来说的
            startAngle += 360/getChildCount();
        }

    }

    public void setDatas(int[] image, String[] text) {
   //处理传递活过来的数据

        for (int i = 0; i < image.length; i++) {
            View itemView = View.inflate(getContext(), R.layout.circle_item, null);
            ImageView iv_item = (ImageView) itemView.findViewById(R.id.item_iv);
            TextView tv_item = (TextView) itemView.findViewById(R.id.item_tv);
            iv_item.setImageResource(image[i]);
            tv_item.setText(text[i]);
            addView(itemView);
        }
    }

   /**获取屏幕宽高*/
    public int getDefaultWidth() {
        DisplayMetrics metrics= getResources().getDisplayMetrics();
        //因为是圆形的区域,屏幕宽高取最小值
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        int result =Math.min(width,height);
        return result;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {//触摸事件
        float X =event.getX();
        float Y =event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = X;
                lastY = Y;
                break;
            case MotionEvent.ACTION_MOVE:
                //获取旋转的起始角度和结束角度,让孩子的起始角度+旋转的角度差值
            float touchStartAngle =CircleUtil.getAngle(lastX,lastY,d);
            float touchEndAngle =CircleUtil.getAngle(X,Y,d);//此处的X,Y是又被重新调用后的触发的,就是移动后的x,y
                float touchAngle =touchEndAngle-touchStartAngle;
                //老师代码是需要判断象限  // TODO
                startAngle +=touchAngle;
                requestLayout();
                lastX = X;
                lastY= Y;
                break;
            case MotionEvent.ACTION_UP:
                Log.d("test", "ACTION_UP");
                break;
        }

        return true; //返回true,表示这个控件毛遂自荐,其他人不处理我就处理
    }
}
