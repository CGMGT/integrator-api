package gt.com.tigo.integradorhome.dto;

import java.util.List;

public class PaginatedDataDto<T> {

    private List<T> rows;
    private Long totalElements;
    private Integer totalPages;

    public PaginatedDataDto(List<T> rows, Long totalElements, Integer totalPages) {
        this.rows = rows;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
