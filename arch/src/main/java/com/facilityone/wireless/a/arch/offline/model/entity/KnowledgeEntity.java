package com.facilityone.wireless.a.arch.offline.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by: owen.
 * Date: on 2018/12/17 下午6:26.
 * Description:
 * email:
 */

public class KnowledgeEntity implements Parcelable {
    public Long typeId;
    public Boolean deleted;
    public String code;
    public String name;
    public String fullName;
    public String imageName;
    public String desc;
    public Long parentTypeId;
    public Integer classify;//分类
    public Integer sort;//排序
    public boolean isClassify;
    public transient String namePinyin;
    public transient String nameFirstLetters;
    public transient int start;//搜索匹配的开始位置(name 中的位置顺序)
    public transient int end;//搜索匹配的结束位置(name 中的位置顺序)

    public KnowledgeEntity() {
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long projectId;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getParentTypeId() {
        return parentTypeId;
    }

    public void setParentTypeId(Long parentTypeId) {
        this.parentTypeId = parentTypeId;
    }

    public Integer getClassify() {
        return classify;
    }

    public void setClassify(Integer classify) {
        this.classify = classify;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.typeId);
        dest.writeValue(this.deleted);
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeString(this.fullName);
        dest.writeString(this.imageName);
        dest.writeString(this.desc);
        dest.writeValue(this.parentTypeId);
        dest.writeValue(this.classify);
        dest.writeValue(this.sort);
        dest.writeByte(this.isClassify ? (byte) 1 : (byte) 0);
        dest.writeString(this.namePinyin);
        dest.writeString(this.nameFirstLetters);
        dest.writeInt(this.start);
        dest.writeInt(this.end);
        dest.writeValue(this.projectId);
    }

    protected KnowledgeEntity(Parcel in) {
        this.typeId = (Long) in.readValue(Long.class.getClassLoader());
        this.deleted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.code = in.readString();
        this.name = in.readString();
        this.fullName = in.readString();
        this.imageName = in.readString();
        this.desc = in.readString();
        this.parentTypeId = (Long) in.readValue(Long.class.getClassLoader());
        this.classify = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sort = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isClassify = in.readByte() != 0;
        this.namePinyin = in.readString();
        this.nameFirstLetters = in.readString();
        this.start = in.readInt();
        this.end = in.readInt();
        this.projectId = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<KnowledgeEntity> CREATOR = new Creator<KnowledgeEntity>() {
        @Override
        public KnowledgeEntity createFromParcel(Parcel source) {
            return new KnowledgeEntity(source);
        }

        @Override
        public KnowledgeEntity[] newArray(int size) {
            return new KnowledgeEntity[size];
        }
    };
}
