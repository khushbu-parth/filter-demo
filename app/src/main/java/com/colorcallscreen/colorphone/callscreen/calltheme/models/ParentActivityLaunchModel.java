package com.colorcallscreen.colorphone.callscreen.calltheme.models;


public class ParentActivityLaunchModel {
    private int callStatus;
    private boolean fromWindow;
    private boolean withAnimation;

    public void setWithAnimation(boolean z) {
    }

    public ParentActivityLaunchModel(int i, boolean z, boolean z2) {
        this.callStatus = i;
        this.fromWindow = z2;
        this.withAnimation = z;
    }

    public int getCallStatus() {
        return this.callStatus;
    }

    public boolean isFromWindow() {
        return this.fromWindow;
    }

    public boolean isWithAnimation() {
        return this.withAnimation;
    }

    public void setCallStatus(int i) {
        this.callStatus = i;
    }

    public void setFromWindow(boolean z) {
        this.fromWindow = z;
    }
}
