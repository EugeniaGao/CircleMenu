package com.hello.administrator.circlemenudemo;

/**
 * Created by Administrator on 2017/1/16.
 */

public class CircleUtil {

    private static float angle;

    //通过触摸事件获取的x,y坐标来计算以圆心为原点的坐标的角度
    public static float getAngle(float x,float y,int d){
        x = x-d/2f;
        y=y-d/2f;
        if(x != 0) {

            //获取角度转为弧度
            angle = (float) (Math.atan(y / x)*180/Math.PI);
        }
        return angle;
    }
}
