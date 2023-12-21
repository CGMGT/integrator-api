package gt.com.tigo.integradorhome.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class XlsxViewBuilder extends AbstractXlsxView {

    private static final Logger LOGGER = LoggerFactory.getLogger(XlsxViewBuilder.class);

    public static final String KEY_FILENAME = "FILENAME";
    public static final String KEY_ATTACH_TIMESTAMP = "FILENAME TIMESTAMP";
    public static final String KEY_SHEET_NAME = "SHEET_NAME";
    public static final String KEY_COLUMNS = "COLUMNS";
    public static final String KEY_HIGHLIGHT_COLUMNS = "HIGHLIGHT COLUMNS";
    public static final String KEY_SPECIFICATION = "SPECIFICATION";
    public static final String KEY_ORDER_BY = "ORDER_BY";
    public static final String KEY_REPOSITORY = "REPOSITORY";
    public static final String KEY_FIELD_MAPPER = "FIELD_MAPPER";
    public static final String KEY_BATCH_SIZE = "BATCH_SIZE";

    private static final String HEADER_CONTENT_KEY = "Content-Disposition";
    private static final String HEADER_CONTENT_VALUE = "attachment; filename=\"%s\"";
    private static final String HEADER_ACCESS_KEY = "access-control-expose-headers";
    private static final String HEADER_ACCESS_VALUE = "Filename";
    private static final String HEADER_FILENAME_KEY = "Filename";
    private static final String HEADER_FILENAME_VALUE = "%s";

    private static final String DEFAULT_SHEET_NAME = "Datos";

    private CellStyle getHeaderStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    private CellStyle getHighlightedHeaderStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    private CellStyle getHighlightedStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    private String attachTimestamp(String  filename) {
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");

        return filename.replace(".xlsx", String.format("_%s.xlsx", sdf.format(new Date())));
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filename = model.get(KEY_FILENAME).toString();

        if (model.containsKey(KEY_ATTACH_TIMESTAMP) && (boolean) model.get(KEY_ATTACH_TIMESTAMP)) {
            filename = this.attachTimestamp(filename);
        }

        response.setHeader(HEADER_CONTENT_KEY, String.format(HEADER_CONTENT_VALUE, filename));
        response.setHeader(HEADER_ACCESS_KEY, HEADER_ACCESS_VALUE);
        response.setHeader(HEADER_FILENAME_KEY, String.format(HEADER_FILENAME_VALUE, filename));

        CellStyle headerStyle = this.getHeaderStyle(workbook);
        CellStyle highlightedHeaderStyle = this.getHighlightedHeaderStyle(workbook);
        CellStyle highlightedStyle = this.getHighlightedStyle(workbook);

        // create a sheet
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet(model.get(KEY_SHEET_NAME) != null ? model.get(KEY_SHEET_NAME).toString() : DEFAULT_SHEET_NAME);

        int rowCount = 0;
        XSSFRow row = sheet.createRow(rowCount++);

        List<String> columns = (List<String>) model.get(KEY_COLUMNS);
        List<String> highlightColumns = model.get(KEY_HIGHLIGHT_COLUMNS) != null ? (List<String>) model.get(KEY_HIGHLIGHT_COLUMNS) : Collections.emptyList();
        List<Integer> highlightColumnsIndexes = this.getHighlightCellIndexes(columns, highlightColumns);

        // fill column names
        int columnCount = 0;

        for (String column : columns) {
            XSSFCell cell = row.createCell(columnCount++);
            cell.setCellStyle(highlightColumns.contains(column) ? highlightedHeaderStyle : headerStyle);
            cell.setCellValue(column);
        }

        // fill rows
        Specification filterSpecification = (Specification) model.get(KEY_SPECIFICATION);
        Sort.Order order = (Sort.Order) model.get(KEY_ORDER_BY);
        JpaSpecificationExecutor repository = (JpaSpecificationExecutor) model.get(KEY_REPOSITORY);
        IListToArrayConverter listToArrayConverter = (IListToArrayConverter) model.get(KEY_FIELD_MAPPER);

        long totalItems = repository.count(filterSpecification);
        int page;

        for (page = 0; page < totalItems / (Integer) model.get(KEY_BATCH_SIZE); page++) {
            rowCount = this.findPagedData(sheet, repository, filterSpecification, page, order, listToArrayConverter, rowCount, (Integer) model.get(KEY_BATCH_SIZE), highlightColumnsIndexes, highlightedStyle);
        }

        this.findPagedData(sheet, repository, filterSpecification, page, order, listToArrayConverter, rowCount, (Integer) model.get(KEY_BATCH_SIZE), highlightColumnsIndexes, highlightedStyle);

        for (int i = 0; i < columns.size(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private int findPagedData(
            XSSFSheet sheet,
            JpaSpecificationExecutor repository,
            Specification filterSpecification,
            int page,
            Sort.Order order,
            IListToArrayConverter listToArrayConverter,
            int rowCount,
            int batchSize,
            List<Integer> highlightColumnsIndexes,
            CellStyle highlightCellStyle
    ) {
        LOGGER.debug(String.format("@%s::findPagedData(..., page=%d, rowCount=%d, batchSize=%d)", this.getClass().getName(), page, rowCount, batchSize));

        Page pageData = repository.findAll(filterSpecification, PageRequest.of(page, batchSize, Sort.by(order)));

        List<Object[]> dataRows = listToArrayConverter.toArray(pageData.getContent());
        int columnCount = 0;

        for (Object[] dataRow : dataRows) {
            XSSFRow row = sheet.createRow(rowCount++);

            for (Object dataCell : dataRow) {
                XSSFCell cell = row.createCell(columnCount);
                if (highlightColumnsIndexes.contains(columnCount++)) {
                    cell.setCellStyle(highlightCellStyle);
                }
                cell.setCellValue(dataCell == null ? "" : dataCell.toString());
            }

            columnCount = 0;
        }

        return rowCount;
    }

    private List<Integer> getHighlightCellIndexes(List<String> columns, List<String> highlightColumns) {
        List<Integer> indexes = new LinkedList<>();

        for (int i = 0; i < columns.size(); i++) {
            if (highlightColumns.contains(columns.get(i))) {
                indexes.add(i);
            }
        }

        return indexes;
    }

}
