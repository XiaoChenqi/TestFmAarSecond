package com.facilityone.wireless.a.arch.ec.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:位置信息
 * Date: 2018/6/28 下午6:56
 */
public class LocationBean implements Parcelable {
    public Long cityId;
    public Long siteId;
    public Long buildingId;
    public Long floorId;
    public Long roomId;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.cityId);
        dest.writeValue(this.siteId);
        dest.writeValue(this.buildingId);
        dest.writeValue(this.floorId);
        dest.writeValue(this.roomId);
    }

    public LocationBean() {
    }

    protected LocationBean(Parcel in) {
        this.cityId = (Long) in.readValue(Long.class.getClassLoader());
        this.siteId = (Long) in.readValue(Long.class.getClassLoader());
        this.buildingId = (Long) in.readValue(Long.class.getClassLoader());
        this.floorId = (Long) in.readValue(Long.class.getClassLoader());
        this.roomId = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<LocationBean> CREATOR = new Parcelable.Creator<LocationBean>() {
        @Override
        public LocationBean createFromParcel(Parcel source) {
            return new LocationBean(source);
        }

        @Override
        public LocationBean[] newArray(int size) {
            return new LocationBean[size];
        }
    };
}
