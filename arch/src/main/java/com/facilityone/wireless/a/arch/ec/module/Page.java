package com.facilityone.wireless.a.arch.ec.module;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:页码
 * Date: 2018/6/5 下午12:34
 */
public class Page {

    private Integer pageNumber;
    private Integer pageSize;

    private Integer totalPage;
    private Integer totalCount;

    public Page() {
        this.pageNumber = 0;
        this.pageSize = 20;
    }

    public void reset() {
        this.pageNumber = 0;
        this.pageSize = 20;
        this.totalPage = null;
        this.totalCount = null;
    }

    public boolean haveNext() {
        try {
            return (pageNumber + 1) < totalPage;
        } catch (Exception e) {
            return false;
        }
    }

    public Page nextPage() {
        pageNumber++;
        this.totalPage = null;
        this.totalCount = null;
        return this;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
