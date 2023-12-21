package gt.com.tigo.integradorhome.util.service;

import com.google.gson.Gson;
import gt.com.tigo.integradorhome.dao.CustomReportRepository;
import gt.com.tigo.integradorhome.dao.DbTableRepository;
import gt.com.tigo.integradorhome.dao.UserRepository;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.model.AdmUsuarioEntity;
import gt.com.tigo.integradorhome.model.CustomReportEntity;
import gt.com.tigo.integradorhome.util.AbstractEntity;
import gt.com.tigo.integradorhome.util.DateTimeFormatterFactory;
import gt.com.tigo.integradorhome.util.DateTimeFormatterType;
import gt.com.tigo.integradorhome.util.IDateTimeFormat;
import gt.com.tigo.integradorhome.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.*;

import static gt.com.tigo.integradorhome.util.SpecFactory.getValueAsLong;

/**
 *
 * @param <T> the entity type. It must extends AbstractEntity.
 * @param <U> the repository type. It must extends JpaRepository and JpaSpecificationExecutor interfaces.
 */
public abstract class AbstractService<T extends AbstractEntity, U extends JpaRepository & JpaSpecificationExecutor>
        implements FindAllService<T>, FindByIdService<T>, FindByPageService<T>, CreateService<T>, UpdateService<T>, DeleteByIdService<T>, XlsxExportService, CsvExportService, CountService {

    /**
     * The internal logger for this abstract service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

    /**
     * A reference to the main repository for this service.
     */
    @Autowired
    protected U repository;

    /**
     * A reference to the user repository for this service.
     */
    @Autowired
    protected UserRepository userRepository;

    /**
     * A reference to the custom report repository for this service.
     */
    @Autowired
    private CustomReportRepository customReportRepository;

    /**
     * A reference to the database table repository for this service.
     */
    @Autowired
    private DbTableRepository dbTableRepository;

    /**
     * Retrieves the list of columns to export. All columns are returned unless a custom report is provided.
     * @param dataTableRequestDto
     * @param tableName
     * @return
     */
    protected List<String> getColumnsToExport(DataTableRequestDto dataTableRequestDto, String tableName) {
        Map<String, FilterInfoDto> filtersInfo = this.buildFiltersInfo(dataTableRequestDto);

        Long customReportId = getValueAsLong(filtersInfo, "customReport");

        if (customReportId == 0) {
            return this.dbTableRepository.getColumnLabelsByTableName(tableName);
        } else {
            CustomReportEntity customReport = this.customReportRepository.findById(customReportId).get();

            String[] reportColumns = new Gson().fromJson(customReport.getColumnas(),String[].class);

            return Arrays.asList(reportColumns);
        }
    }

    /**
     * Maps entity field values to a list of strings.
     * @param item the object to get values from
     * @param columns the field names to get values from
     * @return
     */
    protected List<String> mapEntityToRow(T item, List<String> columns) {
        IDateTimeFormat dateFormat = new DateTimeFormatterFactory().getFormatter(DateTimeFormatterType.DATE);

        List<String> row = new LinkedList<>();

        for (String column : columns) {
            String[] fieldNameParts = column.split(" ");

            StringBuilder sb = new StringBuilder("get");

            for (int i = 0; i < fieldNameParts.length; i++) {
                sb.append(fieldNameParts[i].substring(0,1) + fieldNameParts[i].substring(1).toLowerCase());
            }

            String methodName = sb.toString();

            try {
                Object value = item.getClass().getMethod(methodName).invoke(item);

                String returnType = item.getClass().getMethod(methodName).getReturnType().getName();

                if (value == null) {
                    row.add("");
                    continue;
                }

                if (returnType.contains("java.sql.Date")) {
                    row.add(dateFormat.format((java.sql.Date) value));
                } else {
                    row.add(value.toString());
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LOGGER.error("@{}::mapEntityToRow({}, {})", this.getClass().getName(), item.getClass().getName(), columns);
            }
        }

        return row;
    }

    /**
     * Returns all items in the catalog.
     * @param sortByField the sort column name.
     * @return an ordered list of items of type T.
     * @throws ResourcesNotFoundException when no items are found in the catalog.
     */
    protected final List<T> _findAll(String sortByField) throws ResourcesNotFoundException {
        LOGGER.debug(String.format("@%s::_findAll(%s)", this.getClass().getName(), sortByField));

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, sortByField);

        List<T> entities = this.repository.findAll(Sort.by(order));

        if (entities == null) {
            throw new ResourcesNotFoundException();
        }

        return entities;
    }

    /**
     * Returns the item with the provided id. The identifier must be unique otherwise
     * an exception might be thrown. This scenario is not yet documented.
     * @param id the item unique identifier.
     * @return an item of type T.
     * @throws ResourceNotFoundException when no item with the provided id is found.
     */
    protected final T _findById(Long id) throws ResourceNotFoundException {
        LOGGER.debug(String.format("@%s::_findById(%d)", this.getClass().getName(), id));

        Optional<T> optionalT = this.repository.findById(id);

        if (!optionalT.isPresent()) {
            throw new ResourceNotFoundException();
        }

        T entity = optionalT.get();

        if (entity == null) {
            throw new ResourceNotFoundException();
        }

        return entity;
    }

    /**
     * Programmer must provide the implementation for this method. A JPA specification must
     * be built according to business requirements and the provided filters.
     * @param dataTableRequestDto the custom filters.
     * @return a JPA specification containing the required filters.
     * @throws InvalidFilterException when an invalid filter value is provided.
     */
    protected abstract Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException;

    /**
     * Returns a page of the catalog given a set of filters and sort column name.
     * @param dataTableRequestDto the custom filters.
     * @param defaultSortColumn the sort column name.
     * @return a page containing total items, total pages in the catalog and the items for this page.
     * @throws ResourcesNotFoundException
     * @throws InvalidFilterException
     */
    protected final PaginatedDataDto<T> _findByPage(DataTableRequestDto dataTableRequestDto, String defaultSortColumn) throws ResourcesNotFoundException, InvalidFilterException {
        return _findByPage(dataTableRequestDto, defaultSortColumn, Sort.Direction.ASC);
    }

    /**
     * Convenience method to find items by page with a custom sort direction.
     * @param dataTableRequestDto the custom filters.
     * @param defaultSortColumn the sort column name.
     * @param sortDirection the sort column direction.
     * @return a page containing total items, total pages in the catalog and the items for this page.
     * @throws ResourcesNotFoundException on any error.
     * @throws InvalidFilterException when an invalid filter value is provided.
     */
    protected final PaginatedDataDto<T> _findByPage(DataTableRequestDto dataTableRequestDto, String defaultSortColumn, Sort.Direction sortDirection) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::_findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        Specification filterSpec = buildFilterSpecification(dataTableRequestDto);

        try {
            Page<T> pageData;

            int page = dataTableRequestDto.getPage();
            int size = dataTableRequestDto.getPageSize();

            pageData = this.repository.findAll(filterSpec, PageRequest.of(page, size, Sort.by(buildSortOrder(dataTableRequestDto, defaultSortColumn, sortDirection))));

            // return rows, total rows and total pages
            return new PaginatedDataDto<>(pageData.getContent(), pageData.getTotalElements(), pageData.getTotalPages());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new ResourcesNotFoundException();
        }
    }

    /**
     * Build sort order with ascendant sort direction as a default.
     * @param dataTableRequestDto
     * @param defaultSortColumn
     * @return
     */
    protected final Sort.Order buildSortOrder(DataTableRequestDto dataTableRequestDto, String defaultSortColumn) {
        return buildSortOrder(dataTableRequestDto, defaultSortColumn, Sort.Direction.ASC);
    }

    /**
     * Convenience method to build sort order with a custom sort direction.
     * @param dataTableRequestDto
     * @param defaultSortColumn
     * @param sortDirection
     * @return
     */
    protected final Sort.Order buildSortOrder(DataTableRequestDto dataTableRequestDto, String defaultSortColumn, Sort.Direction sortDirection) {
        if (dataTableRequestDto.getSorted() == null || dataTableRequestDto.getSorted().isEmpty()) {
            // build default Sort.Order
            return new Sort.Order(sortDirection, defaultSortColumn).ignoreCase();
        } else {
            // get sort direction from request
            Sort.Direction dir = Boolean.TRUE.equals(dataTableRequestDto.getSorted().get(0).getDesc()) ? Sort.Direction.DESC : Sort.Direction.ASC;
            // get sort field from request
            String field = dataTableRequestDto.getSorted().get(0).getId();
            // build Sort.Order object
            return new Sort.Order(dir, field).ignoreCase();
        }
    }

    /**
     * Builds a map object containing the provided filters.
     * @param dataTableRequestDto the custom filters.
     * @return a map containing the provided filters.
     */
    protected final Map<String, FilterInfoDto> buildFiltersInfo(DataTableRequestDto dataTableRequestDto) {
        Map<String, FilterInfoDto> filtersInfo = new HashMap<>();

        if (dataTableRequestDto.getFiltered() != null) {
            for (FilterInfoDto filterInfo : dataTableRequestDto.getFiltered()) {
                filtersInfo.put(filterInfo.getId(), filterInfo);
            }
        }

        return filtersInfo;
    }

    /**
     * Saves a new item of type T into the repository.
     * @param entity the new item.
     * @param requesterId the identifier of the requester user.
     * @return the new item with its database generated id.
     * @throws RequesterNotFoundException when the requester doesn't exist.
     * @throws ResourceCreateException when the save operation fails.
     */
    protected final T _create(T entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        LOGGER.debug(String.format("@%s::_create(%s, %d)", this.getClass().getName(), entity, requesterId));

        Optional<AdmUsuarioEntity> optionalAdmUsuarioEntity = this.userRepository.findById(requesterId);

        if (!optionalAdmUsuarioEntity.isPresent()) {
            throw new RequesterNotFoundException();
        }

        AdmUsuarioEntity requester = optionalAdmUsuarioEntity.get();

        if (requester == null) {
            throw new RequesterNotFoundException();
        }

        entity.setFechaCreacion(new Timestamp(new Date().getTime()));
        entity.setUsuarioCreacion(requester.getUsuario());

        try {
            return (T) this.repository.save(entity);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new ResourceCreateException();
        }
    }

    /**
     * Saves changes on an existing item.
     * @param entity the existing item. It must contain its own unique identifier.
     * @param requesterId the identifier of the requester user.
     * @return the updated item.
     * @throws RequesterNotFoundException when the requester doesn't exist.
     * @throws ResourceUpdateException when the save operation fails.
     */
    protected final T _update(T entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
        LOGGER.debug(String.format("@%s::_update(%s, %d)", this.getClass().getName(), entity, requesterId));

        Optional<AdmUsuarioEntity> optionalAdmUsuarioEntity = this.userRepository.findById(requesterId);

        if (!optionalAdmUsuarioEntity.isPresent()) {
            throw new RequesterNotFoundException();
        }

        AdmUsuarioEntity requester = optionalAdmUsuarioEntity.get();

        if (requester == null) {
            throw new RequesterNotFoundException();
        }

        entity.setFechaModificacion(new Timestamp(new Date().getTime()));
        entity.setUsuarioModificacion(requester.getUsuario());

        try {
            return (T) this.repository.save(entity);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new ResourceUpdateException();
        }
    }

    /**
     * Removes an item from the catalog.
     * @param entityId the item unique identifier.
     * @return true if item was successfully removed or false if not.
     * @throws ResourceDeleteException when the delete operation fails.
     */
    protected final boolean _deleteById(Long entityId) throws ResourceDeleteException {
        LOGGER.debug(String.format("@%s::_deleteById(%d)", this.getClass().getName(), entityId));

        try {
            this.repository.deleteById(entityId);

            return true;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new ResourceDeleteException();
        }
    }

    /**
     * Returns the total number of rows in the repository.
     * @param dataTableRequestDto
     * @return items count
     * @throws InvalidFilterException
     */
    public final Long count(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        LOGGER.debug(String.format("@%s::count(%s)", this.getClass().getName(), dataTableRequestDto));

        return this.repository.count(this.buildFilterSpecification(dataTableRequestDto));
    }

}
