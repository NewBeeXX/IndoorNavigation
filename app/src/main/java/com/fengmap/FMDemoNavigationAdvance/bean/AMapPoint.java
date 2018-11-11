package com.fengmap.FMDemoNavigationAdvance.bean;

import java.io.Serializable;



/** 重载BmobChatUser对象：若还有其他需要增加的属性可在此添加
 * @ClassName: TextUser
 * @Description: TODO
 * @author smile
 * @date 2014-5-29 下午6:15:45
 */
public class AMapPoint implements Serializable {
    private static final long serialVersionUID = -2527515194340586771L;
    private String amapId =String.valueOf("amapid");
    private Double x = Double.valueOf(0.0D);
    private Double y = Double.valueOf(0.0D);
    private Integer z = Integer.valueOf(0);
    private Double degree = Double.valueOf(0.0D);
    private int step =  Integer.valueOf(0);
    private String detailAddress = String.valueOf("detailAddress");;
    private int state = 1;
    public AMapPoint() {
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public AMapPoint(double x, double y,Integer z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }
    public AMapPoint(double x, double y,Integer z,String amapId) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setAmapId(amapId);
    }
    public AMapPoint(double x, double y,Integer z,String amapId,String detailAddress) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setAmapId(amapId);
        this.setDetailAddress(detailAddress);
    }
    public AMapPoint(double x, double y,Integer z,double degree,int step){
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setDegree(degree);
        this.setStep(step);
    }
    public AMapPoint(int state) {
        this.setState(state);
    }
    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public String getAmapId() {
        return amapId;
    }

    public void setAmapId(String amapId) {
        this.amapId = amapId;
    }
    public String toString(){
        return "[amapId:"+amapId+",x:"+x+",y:"+y+",z:"+z+",state:"+state+",detailAddress:"+detailAddress+"}";
    }
    public boolean equal(AMapPoint a){
        if(this.state==a.getState()&&Math.abs(a.getX()-this.x)<0.005&&Math.abs(a.getY()-this.y)<0.005&&this.z==a.z)return true;
        return false;
    }

    public Double getDegree() {
        return degree;
    }

    public void setDegree(Double degree) {
        this.degree = degree;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
