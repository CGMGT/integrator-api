package gt.com.tigo.integradorhome.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class XlsxByteArrayInputStreamBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(XlsxByteArrayInputStreamBuilder.class);

    public static final String KEY_FILENAME = "FILENAME";
    public static final String KEY_SHEET_NAME = "SHEET_NAME";
    public static final String KEY_COLUMNS = "COLUMNS";
    public static final String KEY_SPECIFICATION = "SPECIFICATION";
    public static final String KEY_ORDER_BY = "ORDER_BY";
    public static final String KEY_REPOSITORY = "REPOSITORY";
    public static final String KEY_FIELD_MAPPER = "FIELD_MAPPER";
    public static final String KEY_BATCH_SIZE = "BATCH_SIZE";

    public static final String HEADER_CONTENT_VALUE = "attachment; filename=\"%s\"";
    public static final String MEDIA_TYPE = "application/vnd.ms-excel";

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

    public ByteArrayInputStream buildExcelDocument(Map<String, Object> model) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        CellStyle headerStyle = this.getHeaderStyle(workbook);

        // create a sheet
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet(model.get(KEY_SHEET_NAME) != null ? model.get(KEY_SHEET_NAME).toString() : DEFAULT_SHEET_NAME);

        int rowCount = 0;
        XSSFRow row = sheet.createRow(rowCount++);

        // fill column names
        List<String> columns = (List<String>) model.get(KEY_COLUMNS);
        int columnCount = 0;

        for (String column : columns) {
            XSSFCell cell = row.createCell(columnCount++);
            cell.setCellStyle(headerStyle);
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
            rowCount = this.findPagedData(sheet, repository, filterSpecification, page, order, listToArrayConverter, rowCount, (Integer) model.get(KEY_BATCH_SIZE));
        }

        this.findPagedData(sheet, repository, filterSpecification, page, order, listToArrayConverter, rowCount, (Integer) model.get(KEY_BATCH_SIZE));

        for (int i = 0; i < columns.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(out);

        return new ByteArrayInputStream(out.toByteArray());
    }

    private int findPagedData(XSSFSheet sheet, JpaSpecificationExecutor repository, Specification filterSpecification, int page, Sort.Order order, IListToArrayConverter listToArrayConverter, int rowCount, int batchSize) {
        LOGGER.debug(String.format("@%s::findPagedData(..., page=%d, rowCount=%d, batchSize=%d)", this.getClass().getName(), page, rowCount, batchSize));

        Page pageData = repository.findAll(filterSpecification, PageRequest.of(page, batchSize, Sort.by(order)));

        List<Object[]> dataRows = listToArrayConverter.toArray(pageData.getContent());
        int columnCount = 0;

        for (Object[] dataRow : dataRows) {
            XSSFRow row = sheet.createRow(rowCount++);

            for (Object dataCell : dataRow) {
                XSSFCell cell = row.createCell(columnCount++);
                cell.setCellValue(dataCell == null ? "" : dataCell.toString());
            }

            columnCount = 0;
        }

        return rowCount;
    }

}
