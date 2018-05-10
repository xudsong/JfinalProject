package hisense.code.utils;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 15-10-15.
 */
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开始行
     */
    private int startRow;

    /**
     * 结束行
     */
    private int endRow;

    /**
     * 当前页
     */
    private int curPageNo;

    /**
     * 每页记录数
     */
    private int pageSize = 50;

    /**
     * 排序项 （ASC  DESC)
     */
    private String orderBy;

    private String order;

    /**
     * 是否是搜索请求
     */
    private boolean search;

    /**
     * 总页数
     */
    private long totalPages;

    /**
     * 总记录数
     */
    private Long totalRecords;

    /**
     * 结果集合
     */
    protected List dataRows;

    public int getStartRow() {
        if (this.curPageNo <= 1) {
            startRow = 0;
        } else {
            startRow = (this.curPageNo - 1) * this.pageSize;
        }
        return startRow;
    }

    public int getEndRow() {
        if (this.curPageNo <= 1) {
            endRow = this.pageSize;
        } else {
            endRow = this.curPageNo * this.pageSize;
        }
        return endRow;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurPageNo() {
        return curPageNo;
    }

    public void setCurPageNo(int curPageNo) {
        this.curPageNo = curPageNo;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public boolean getSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public List getDataRows() {
        return dataRows;
    }

    public void setDataRows(List dataRows) {
        this.dataRows = dataRows;
    }

    /**
     * 计算总页数
     *
     * @param totalRecords 总记录数
     */
    public void computePageCount(long totalRecords) {
        long totalPagesCount = totalRecords % this.getPageSize() == 0 ?
                totalRecords / this.getPageSize() : (totalRecords / this.getPageSize() + 1);

        if (this.getCurPageNo() > totalPagesCount) {
            this.setCurPageNo(1);
        }

        this.setTotalPages(totalPagesCount);
        this.setTotalRecords(totalRecords);
    }

//    private List<Condition> conditions;
//
//    public List<Condition> getConditions() {
//        return conditions;
//    }
//
//    public void setConditions(List<Condition> conditions) {
//        this.conditions = conditions;
//    }

    /**
     * 分页是否有效
     *
     * @return this.pageSize==-1
     */
//    @JsonIgnore
    public boolean isDisabled() {
        return this.pageSize == -1;
    }

    /**
     * 是否进行总数统计
     *
     * @return this.count==-1
     */
//    @JsonIgnore
    public boolean isNotCount() {
        return this.totalRecords == -1;
    }

    @Override
    public String toString() {
        return "Page{" +
                "startRow=" + startRow +
                ", endRow=" + endRow +
                ", curPageNo=" + curPageNo +
                ", pageSize=" + pageSize +
                ", orderBy='" + orderBy + '\'' +
                ", order='" + order + '\'' +
                ", search=" + search +
                ", totalPages=" + totalPages +
                ", totalRecords=" + totalRecords +
                ", dataRows=" + dataRows +
//                ", conditions=" + conditions +
                '}';
    }
}
