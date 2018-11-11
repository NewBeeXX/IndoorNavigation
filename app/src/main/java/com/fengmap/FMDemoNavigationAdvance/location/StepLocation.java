package com.fengmap.FMDemoNavigationAdvance.location;

import android.util.Log;


import com.fengmap.FMDemoNavigationAdvance.bean.AMapPoint;
/**
 * Created by Administrator on 2015/7/31.
 */
public class StepLocation {
    public  AMapPoint lastAMapPoint;
    private static class LazyHolder {
        private static StepLocation INSTANCE = new StepLocation();
    }
    public static  StepLocation getInstance() {
        return LazyHolder.INSTANCE;
    }
    private StepLocation(){
        Log.i("zjx","StepLocation 初始化");
        lastAMapPoint=new AMapPoint(0.5,0.5, 0/*Config.CurrentFloorDefault_暂时设0*/,30,0);
    }
    public static void refresh(){
        LazyHolder.INSTANCE=null;
        LazyHolder.INSTANCE = new StepLocation();
    }
}

