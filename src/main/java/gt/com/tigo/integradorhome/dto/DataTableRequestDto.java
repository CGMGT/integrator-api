package gt.com.tigo.integradorhome.dto;

import java.util.List;

public class DataTableRequestDto {

    private int page;
    private int pageSize;
    private List<SortInfoDto> sorted;
    private List<FilterInfoDto> filtered;
    private String operator;

    public DataTableRequestDto() {
        // no constructor for the moment
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<SortInfoDto> getSorted() {
        return sorted;
    }

    public void setSorted(List<SortInfoDto> sorted) {
        this.sorted = sorted;
    }

    public List<FilterInfoDto> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<FilterInfoDto> filtered) {
        this.filtered = filtered;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String toString() {
        return String.format("{page: %d, pageSize: %d, sorted: %s, filtered: %s, operator: %s}", this.page, this.pageSize, this.sorted, this.filtered, this.operator);
    }
}
