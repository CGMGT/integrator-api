package gt.com.tigo.integradorhome.util;

import gt.com.tigo.integradorhome.dto.ResourceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CsvViewBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvViewBuilder.class);

    public static final String HEADER_CONTENT_DISPOSITION = "attachment; filename=\"%s\"";
    public static final String KEY_FILENAME = "FILENAME";
    public static final String KEY_ATTACH_TIMESTAMP = "FILENAME TIMESTAMP";
    public static final String KEY_COLUMNS = "COLUMNS";
    public static final String KEY_SPECIFICATION = "SPECIFICATION";
    public static final String KEY_ORDER_BY = "ORDER_BY";
    public static final String KEY_REPOSITORY = "REPOSITORY";
    public static final String KEY_FIELD_MAPPER = "FIELD_MAPPER";
    public static final String KEY_BATCH_SIZE = "BATCH_SIZE";

    public static final HttpHeaders HEADERS;

    static {
        HEADERS = new HttpHeaders();

        HEADERS.setAccessControlExposeHeaders(Arrays.asList("Content-Disposition"));
    }


    public ResourceDto buildCsvFile(Map<String, Object> model) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(byteStream);

        int rowCount = 0;

        List<String> columns = (List<String>) model.get(KEY_COLUMNS);

        String row = String.format("\"%s\"%n", String.join("\",\"", columns));

        writer.write(row);

        // fill rows
        Specification filterSpecification = (Specification) model.get(KEY_SPECIFICATION);
        Sort.Order order = (Sort.Order) model.get(KEY_ORDER_BY);
        JpaSpecificationExecutor repository = (JpaSpecificationExecutor) model.get(KEY_REPOSITORY);
        IListToListConverter listToListConverter = (IListToListConverter) model.get(KEY_FIELD_MAPPER);

        long totalItems = repository.count(filterSpecification);
        int page;

        for (page = 0; page < totalItems / (Integer) model.get(KEY_BATCH_SIZE); page++) {
            rowCount = this.findPagedData(writer, repository, filterSpecification, page, order, listToListConverter, rowCount, (Integer) model.get(KEY_BATCH_SIZE));
        }

        this.findPagedData(writer, repository, filterSpecification, page, order, listToListConverter, rowCount, (Integer) model.get(KEY_BATCH_SIZE));

        writer.flush();
        writer.close();

        String filename = model.get(KEY_FILENAME).toString();

        if (model.containsKey(KEY_ATTACH_TIMESTAMP) && (boolean) model.get(KEY_ATTACH_TIMESTAMP)) {
            filename = this.attachTimestamp(filename);
        }

        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setFilename(filename);
        resourceDto.setMediaType(MediaType.TEXT_PLAIN);
        resourceDto.setResource(new ByteArrayResource(byteStream.toByteArray()));
        resourceDto.setLength(byteStream.toByteArray().length);

        return resourceDto;
    }

    private int findPagedData(OutputStreamWriter writer, JpaSpecificationExecutor repository, Specification filterSpecification, int page, Sort.Order order, IListToListConverter listToListConverter, int rowCount, int batchSize) throws IOException {
        LOGGER.debug(String.format("@%s::findPagedData(..., page=%d, rowCount=%d, batchSize=%d)", this.getClass().getName(), page, rowCount, batchSize));

        Page pageData = repository.findAll(filterSpecification, PageRequest.of(page, batchSize, Sort.by(order)));

        List<List<String>> dataRows = listToListConverter.toList(pageData.getContent());

        for (List<String> dataRow : dataRows) {

            // sanitize row values
            for (int i = 0; i < dataRow.size(); i++) {
                if (dataRow.get(i) == null) {
                    dataRow.set(i, "");
                }
            }

            String row = String.format("\"%s\"%n", String.join("\",\"", dataRow));

            writer.write(row);

            rowCount++;
        }

        return rowCount;
    }

    private String attachTimestamp(String  filename) {
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");

        return filename.replace(".csv", String.format("_%s.csv", sdf.format(new Date())));
    }
}
