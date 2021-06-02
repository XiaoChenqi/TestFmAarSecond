package com.facilityone.wireless.a.arch.offline.model.entity;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:巡检基础检查项
 * Date: 2018/11/1 9:41 AM
 */
public class PatrolBaseItemEntity {
    
    private Long contentId;
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
    private Integer deleted;

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSelectEnums() {
        return selectEnums == null ? "" : selectEnums;
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
        return selectRightValue == null ? "" : selectRightValue;
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
        return defaultSelectValue == null ? "" : defaultSelectValue;
    }

    public void setDefaultSelectValue(String defaultSelectValue) {
        this.defaultSelectValue = defaultSelectValue;
    }

    public String getExceptions() {
        return exceptions == null ? "" : exceptions;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
