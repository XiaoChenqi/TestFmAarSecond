package com.facilityone.wireless.a.arch.ec.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:选择统一实体
 * Date: 2018/10/25 11:40 AM
 */
public class SelectDataBean implements Parcelable {
    private Long id;
    private Long parentId;
    private String name;
    private String fullName;
    private String desc;
    private Boolean haveChild;
    private LocationBean location;
    private transient String namePinyin;//name的拼音全拼
    private transient String nameFirstLetters;//name所有汉字首字母
    private transient String fullNamePinyin;//fullName的拼音全拼
    private transient String fullNameFirstLetters;//fullName所有汉字首字母
    private List<Long> parentIds;
    private Long standbyId;//备用id
    private String standbyName;//备用name
    private String locationName;
    private transient int start;//搜索匹配的开始位置(name 中的位置顺序)
    private transient int end;//搜索匹配的结束位置(name 中的位置顺序)
    private transient int subStart;//搜索匹配的开始位置(fullName 中的位置顺序)
    private transient int subEnd;//搜索匹配的结束位置(fullName 中的位置顺序)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName == null ? "" : fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDesc() {
        return desc == null ? "" : desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getHaveChild() {
        return haveChild;
    }

    public void setHaveChild(Boolean haveChild) {
        this.haveChild = haveChild;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public String getNamePinyin() {
        return namePinyin == null ? "" : namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public String getNameFirstLetters() {
        return nameFirstLetters == null ? "" : nameFirstLetters;
    }

    public void setNameFirstLetters(String nameFirstLetters) {
        this.nameFirstLetters = nameFirstLetters;
    }

    public String getFullNamePinyin() {
        return fullNamePinyin == null ? "" : fullNamePinyin;
    }

    public void setFullNamePinyin(String fullNamePinyin) {
        this.fullNamePinyin = fullNamePinyin;
    }

    public String getFullNameFirstLetters() {
        return fullNameFirstLetters == null ? "" : fullNameFirstLetters;
    }

    public void setFullNameFirstLetters(String fullNameFirstLetters) {
        this.fullNameFirstLetters = fullNameFirstLetters;
    }

    public List<Long> getParentIds() {
        if(parentIds == null) {
            parentIds = new ArrayList<>();
        }
        return parentIds;
    }

    public void setParentIds(List<Long> parentIds) {
        this.parentIds = parentIds;
    }

    public SelectDataBean() {
        setHaveChild(false);
    }

    public Long getStandbyId() {
        return standbyId;
    }

    public void setStandbyId(Long standbyId) {
        this.standbyId = standbyId;
    }

    public String getStandbyName() {
        return standbyName == null ? "" : standbyName;
    }

    public void setStandbyName(String standbyName) {
        this.standbyName = standbyName;
    }

    public String getLocationName() {
        return locationName == null ? "" : locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getSubStart() {
        return subStart;
    }

    public void setSubStart(int subStart) {
        this.subStart = subStart;
    }

    public int getSubEnd() {
        return subEnd;
    }

    public void setSubEnd(int subEnd) {
        this.subEnd = subEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.parentId);
        dest.writeString(this.name);
        dest.writeString(this.fullName);
        dest.writeString(this.desc);
        dest.writeValue(this.haveChild);
        dest.writeParcelable(this.location, flags);
        dest.writeString(this.namePinyin);
        dest.writeString(this.nameFirstLetters);
        dest.writeString(this.fullNamePinyin);
        dest.writeString(this.fullNameFirstLetters);
        dest.writeList(this.parentIds);
        dest.writeValue(this.standbyId);
        dest.writeString(this.standbyName);
        dest.writeString(this.locationName);
        dest.writeInt(this.start);
        dest.writeInt(this.end);
        dest.writeInt(this.subStart);
        dest.writeInt(this.subEnd);
    }

    protected SelectDataBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.parentId = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.fullName = in.readString();
        this.desc = in.readString();
        this.haveChild = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.location = in.readParcelable(LocationBean.class.getClassLoader());
        this.namePinyin = in.readString();
        this.nameFirstLetters = in.readString();
        this.fullNamePinyin = in.readString();
        this.fullNameFirstLetters = in.readString();
        this.parentIds = new ArrayList<Long>();
        in.readList(this.parentIds, Long.class.getClassLoader());
        this.standbyId = (Long) in.readValue(Long.class.getClassLoader());
        this.standbyName = in.readString();
        this.locationName = in.readString();
        this.start = in.readInt();
        this.end = in.readInt();
        this.subStart = in.readInt();
        this.subEnd = in.readInt();
    }

    public static final Creator<SelectDataBean> CREATOR = new Creator<SelectDataBean>() {
        @Override
        public SelectDataBean createFromParcel(Parcel source) {
            return new SelectDataBean(source);
        }

        @Override
        public SelectDataBean[] newArray(int size) {
            return new SelectDataBean[size];
        }
    };
}
