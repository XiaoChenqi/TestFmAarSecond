package com.facilityone.wireless.a.arch.ec.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:附件
 * Date: 2018/6/28 下午7:00
 */
public class AttachmentBean implements Parcelable {
    public long value;
    public String src;
    public String name;
    public String url;
    public boolean check;

    public AttachmentBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.value);
        dest.writeString(this.src);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeByte(this.check ? (byte) 1 : (byte) 0);
    }

    protected AttachmentBean(Parcel in) {
        this.value = in.readLong();
        this.src = in.readString();
        this.name = in.readString();
        this.url = in.readString();
        this.check = in.readByte() != 0;
    }

    public static final Creator<AttachmentBean> CREATOR = new Creator<AttachmentBean>() {
        @Override
        public AttachmentBean createFromParcel(Parcel source) {
            return new AttachmentBean(source);
        }

        @Override
        public AttachmentBean[] newArray(int size) {
            return new AttachmentBean[size];
        }
    };
}
