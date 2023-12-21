package gt.com.tigo.integradorhome.service;

import gt.com.tigo.integradorhome.dao.WoControlStatusRepository;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.model.WoControlStatusEntity;
import gt.com.tigo.integradorhome.util.*;
import gt.com.tigo.integradorhome.util.exception.*;
import gt.com.tigo.integradorhome.util.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

import static gt.com.tigo.integradorhome.util.SpecFactory.*;
import static gt.com.tigo.integradorhome.util.SpecFactory.getValueAsZonedDate;

@Service
public class WoControlStatusService extends AbstractService<WoControlStatusEntity, WoControlStatusRepository> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WoControlStatusService.class);
    public static final String ID_STATUS = "idStatus";
    public static final String WO_ID_FS = "woIdFs";

    @Override
    protected Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        LOGGER.debug(String.format("@%s::buildFilterSpecification(%s)", this.getClass().getName(), dataTableRequestDto));
        try {
            Map<String, FilterInfoDto> filtersInfo = super.buildFiltersInfo(dataTableRequestDto);

            // create filter specifications
            Specification idStatusSpec = SpecFactory.<WoControlStatusEntity>getEqualSpecification(ID_STATUS, getValueAsLong(filtersInfo, ID_STATUS));
            Specification woIdTivoliSpec = SpecFactory.<WoControlStatusEntity>getLikeSpecification("woIdTivoli", getValueAsString(filtersInfo, "woIdTivoli"));
            Specification woTivoliStatusSpec = SpecFactory.<WoControlStatusEntity>getLikeSpecification("woTivoliStatus", getValueAsString(filtersInfo, "woTivoliStatus"));
            Specification woTivoliStatusFechaSpec = SpecFactory.<WoControlStatusEntity>getBetweenSpecification("woTivoliStatusFecha", getValueAsZonedDate(filtersInfo, "woTivoliStatusFechaStart"), getValueAsZonedDate(filtersInfo, "woTivoliStatusFechaEnd"));

            Specification woFsStatusSpec = SpecFactory.<WoControlStatusEntity>getLikeSpecification("woFsStatus", getValueAsString(filtersInfo, "woFsStatus"));
            Specification woFsStatusFechaSpec = SpecFactory.<WoControlStatusEntity>getLikeSpecification("woFsStatusFecha", getValueAsString(filtersInfo,"woFsStatusFecha"));
            Specification finalizadaSpec = SpecFactory.<WoControlStatusEntity>getEqualSpecification("finalizada", getValueAsLong(filtersInfo, "finalizada"));
            Specification fechaFinalizadaSpec = SpecFactory.<WoControlStatusEntity>getBetweenSpecification("fechaFinalizada", getValueAsZonedDate(filtersInfo, "fechaFinalizadaStart"), getValueAsZonedDate(filtersInfo, "fechaFinalizadaEnd"));
            Specification comentariosSpec = SpecFactory.<WoControlStatusEntity>getLikeSpecification("comentarios", getValueAsString(filtersInfo, "comentarios"));
            Specification periodoSpec = SpecFactory.<WoControlStatusEntity>getLikeSpecification("attribute4", getValueAsString(filtersInfo, "periodo"));
            Specification woIdFsSpec = SpecFactory.<WoControlStatusEntity>getLikeSpecification(WO_ID_FS, getValueAsString(filtersInfo, WO_ID_FS));
            Specification woIdFseRrrorSpec = SpecFactory.<WoControlStatusEntity>getLikeSpecificationN(WO_ID_FS, getValueAsString(filtersInfo, "woIdFsError"));

            // assemble filter specifications
            return Specification
                    .where(getDefaultSpecification())
                    .and(idStatusSpec)
                    .and(woIdTivoliSpec)
                    .and(woTivoliStatusSpec)
                    .and(woTivoliStatusFechaSpec)
                    .and(woIdFsSpec)
                    .and(woIdFseRrrorSpec)
                    .and(woFsStatusSpec)
                    .and(woFsStatusFechaSpec)
                    .and(finalizadaSpec)
                    .and(fechaFinalizadaSpec)
                    .and(comentariosSpec)
                    .and(periodoSpec);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new InvalidFilterException();
        }
    }

    @Override
    public WoControlStatusEntity create(WoControlStatusEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        return null;
    }

    @Override
    public ResourceDto exportCsv(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException, IOException {
        LOGGER.debug(String.format("@%s::exportCsv(%s)", this.getClass().getName(), dataTableRequestDto));

        try{
            Map<String, Object> model = new HashMap<>();

            model.put(CsvViewBuilder.KEY_FILENAME, "EstadosWO.csv");
            model.put(CsvViewBuilder.KEY_COLUMNS, Arrays.asList(
                    "ID",
                    "WO ID TIVOLI",
                    "WO TIVOLI STATUS",
                    "WO TIVOLI STATUS FECHA",
                    "WO ID FS",
                    "WO FS STATUS",
                    "WO FS STATUS FECHA",
                    "FINALIZADA",
                    "FECHA FINALIZADA",
                    "COMENTARIOS",
                    "FECHA CREACION",
                    "DISTRICT",
                    "TASK TYPE",
                    "TASK TYPE CATEGORY"
            ));
            model.put(CsvViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(CsvViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, ID_STATUS));
            model.put(CsvViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(CsvViewBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(CsvViewBuilder.KEY_FIELD_MAPPER, (IListToListConverter<WoControlStatusEntity>) items -> {
                List<List<String>> rows = new LinkedList<>();

                IDateTimeFormat dateFormat = new DateTimeFormatterFactory().getFormatter(DateTimeFormatterType.TIMESTAMP);

                for (WoControlStatusEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(String.valueOf(item.getIdStatus()));
                    row.add(item.getWoIdTivoli());
                    row.add(item.getWoTivoliStatus());
                    row.add(item.getWoTivoliStatusFecha());
                    row.add(item.getWoIdFs());
                    row.add(item.getWoFsStatus());
                    row.add(item.getWoFsStatusFecha());
                    row.add(String.valueOf(item.getFinalizada()));
                    row.add(dateFormat.format(item.getFechaFinalizada()));
                    row.add(item.getComentarios());
                    row.add(dateFormat.format(item.getFechaCreacion()));
                    row.add(item.getAttribute1());
                    row.add(item.getAttribute2());
                    row.add(item.getAttribute3());
                    rows.add(row);
                }

                return rows;
            });

            return new CsvViewBuilder().buildCsvFile(model);
        } catch (InvalidFilterException ex) {
            throw ex;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new ResourcesNotFoundException();
        }
    }

    @Override
    public boolean deleteById(Long entityId) throws ResourceDeleteException {
        return false;
    }

    @Override
    public WoControlStatusEntity deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public List<WoControlStatusEntity> findAll() throws ResourcesNotFoundException {
        return null;
    }

    @Override
    public WoControlStatusEntity findById(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public PaginatedDataDto<WoControlStatusEntity> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        return super._findByPage(dataTableRequestDto, ID_STATUS, Sort.Direction.DESC);
    }

    @Override
    public WoControlStatusEntity update(WoControlStatusEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::exportXlsx(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            Map<String, Object> model = new HashMap<>();

            model.put(XlsxViewBuilder.KEY_FILENAME, "EstadosWO.xlsx");
            model.put(XlsxViewBuilder.KEY_SHEET_NAME, "Datos");
            model.put(XlsxViewBuilder.KEY_COLUMNS, Arrays.asList(
                    "ID",
                    "WO ID TIVOLI",
                    "WO TIVOLI STATUS",
                    "WO TIVOLI STATUS FECHA",
                    "WO ID FS",
                    "WO FS STATUS",
                    "WO FS STATUS FECHA",
                    "FINALIZADA",
                    "FECHA FINALIZADA",
                    "COMENTARIOS",
                    "FECHA CREACION",
                    "DISTRICT",
                    "TASK TYPE",
                    "TASK TYPE CATEGORY"
            ));
            model.put(XlsxViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(XlsxViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, ID_STATUS));
            model.put(XlsxViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(XlsxByteArrayInputStreamBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(XlsxViewBuilder.KEY_FIELD_MAPPER, (IListToArrayConverter<WoControlStatusEntity>) items -> {
                List<Object[]> rows = new LinkedList<>();

                IDateTimeFormat dateFormat = new DateTimeFormatterFactory().getFormatter(DateTimeFormatterType.TIMESTAMP);

                for (WoControlStatusEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(String.valueOf(item.getIdStatus()));
                    row.add(item.getWoIdTivoli());
                    row.add(item.getWoTivoliStatus());
                    row.add(item.getWoTivoliStatusFecha());
                    row.add(item.getWoIdFs());
                    row.add(item.getWoFsStatus());
                    row.add(item.getWoFsStatusFecha());
                    row.add(String.valueOf(item.getFinalizada()));
                    row.add(dateFormat.format(item.getFechaFinalizada()));
                    row.add(item.getComentarios());
                    row.add(dateFormat.format(item.getFechaCreacion()));
                    row.add(item.getAttribute1());
                    row.add(item.getAttribute2());
                    row.add(item.getAttribute3());
                    rows.add(row.toArray());
                }

                return rows;
            });

            return new ModelAndView(new XlsxViewBuilder(), model);
        } catch (InvalidFilterException ex) {
            throw ex;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new ResourcesNotFoundException();
        }
    }
}
