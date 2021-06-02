package com.facilityone.wireless.a.arch.offline.model.entity;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:检查项
 * Date: 2018/11/1 3:30 PM
 */
public class PatrolItemEntity {
    private Long eqId;
    private Long taskId;
    private Long spotId;
    private Long contentId;
    private Long contentResultId;
    private Integer sort;
    private Integer deleted;
    private String select;
    private String input;
    private String comment;
    private Boolean completed;
    private String content;
    private String selectEnums;
    private Integer contentType;
    private Integer resultType;
    private String selectRightValue;
    private Double inputUpper;
    private Double inputFloor;
    private Double defaultInputValue;
    private String defaultSelectValue;
    private String exceptions;
    private String unit;
    private Integer validStatus;
    private List<PatrolPicEntity> picEntities;
    private List<LocalMedia> medias;
    private List<Long> picIds;

    public List<LocalMedia> getMedias() {
        if (medias == null) {
            medias = new ArrayList<>();
        }
        return medias;
    }

    public List<Long> getPicIds() {
        return picIds;
    }

    public void setPicIds(List<Long> picIds) {
        this.picIds = picIds;
    }

    public void setMedias(List<LocalMedia> medias) {
        this.medias = medias;
    }

    public List<PatrolPicEntity> getPicEntities() {
        return picEntities;
    }

    public void setPicEntities(List<PatrolPicEntity> picEntities) {
        this.picEntities = picEntities;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSelectEnums() {
        return selectEnums;
    }

    public void setSelectEnums(String selectEnums) {
        this.selectEnums = selectEnums;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public Integer getResultType() {
        return resultType;
    }

    public void setResultType(Integer resultType) {
        this.resultType = resultType;
    }

    public String getSelectRightValue() {
        return selectRightValue;
    }

    public void setSelectRightValue(String selectRightValue) {
        this.selectRightValue = selectRightValue;
    }

    public Double getInputUpper() {
        return inputUpper;
    }

    public void setInputUpper(Double inputUpper) {
        this.inputUpper = inputUpper;
    }

    public Double getInputFloor() {
        return inputFloor;
    }

    public void setInputFloor(Double inputFloor) {
        this.inputFloor = inputFloor;
    }

    public Double getDefaultInputValue() {
        return defaultInputValue;
    }

    public void setDefaultInputValue(Double defaultInputValue) {
        this.defaultInputValue = defaultInputValue;
    }

    public String getDefaultSelectValue() {
        return defaultSelectValue;
    }

    public void setDefaultSelectValue(String defaultSelectValue) {
        this.defaultSelectValue = defaultSelectValue;
    }

    public String getExceptions() {
        return exceptions;
    }

    public void setExceptions(String exceptions) {
        this.exceptions = exceptions;
    }

    public String getUnit() {
        return unit == null ? "" : unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Long getEqId() {
        return eqId;
    }

    public void setEqId(Long eqId) {
        this.eqId = eqId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getContentResultId() {
        return contentResultId;
    }

    public void setContentResultId(Long contentResultId) {
        this.contentResultId = contentResultId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
