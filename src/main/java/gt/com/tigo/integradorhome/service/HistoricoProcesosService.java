package gt.com.tigo.integradorhome.service;

import gt.com.tigo.integradorhome.dao.HistoricoProcesosRepository;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.model.HistoricoProcesosEntity;
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

import static gt.com.tigo.integradorhome.util.SpecFactory.getDefaultSpecification;
import static gt.com.tigo.integradorhome.util.SpecFactory.getValueAsZonedDate;

@Service
public class HistoricoProcesosService extends AbstractService<HistoricoProcesosEntity, HistoricoProcesosRepository> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricoProcesosService.class);


    @Override
    protected Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        try {
            Map<String, FilterInfoDto> filtersInfo = super.buildFiltersInfo(dataTableRequestDto);

            // create filter specifications
            Specification fechaInicialEjecucionSpec = SpecFactory.<HistoricoProcesosEntity>getBetweenSpecification("fechaInicialEjecucion", getValueAsZonedDate(filtersInfo, "startDate"), getValueAsZonedDate(filtersInfo, "endDate"));

            // assemble filter specifications
            return Specification
                    .where(getDefaultSpecification())
                    .and(fechaInicialEjecucionSpec);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new InvalidFilterException();
        }
    }

    @Override
    public HistoricoProcesosEntity create(HistoricoProcesosEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        return null;
    }

    @Override
    public ResourceDto exportCsv(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException, IOException {
        LOGGER.debug(String.format("@%s::exportCsv(%s)", this.getClass().getName(), dataTableRequestDto));

        try{
            Map<String, Object> model = new HashMap<>();

            model.put(CsvViewBuilder.KEY_FILENAME, "HistoricoProcesos.csv");
            model.put(CsvViewBuilder.KEY_COLUMNS, Arrays.asList(
                    "ID",
                    "NOMBRE",
                    "DESCRIPCIÓN",
                    "ESTADO",
                    "FECHA INICIAL EJECUCIÓN",
                    "FECHA FINAL EJECUCIÓN",
                    "TIEMPO EJECUCIÓN",
                    "NÚMERO REGISTROS CARGADOS",
                    "USUARIO EJECUCIÓN",
                    "STATUS RESULTADO",
                    "COMENTARIOS RESULTADO",
                    "ATRIBUTO 1",
                    "ATRIBUTO 2",
                    "ATRIBUTO 3",
                    "ATRIBUTO 4",
                    "ATRIBUTO 5",
                    "ATRIBUTO 6",
                    "ATRIBUTO 7"
            ));
            model.put(CsvViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(CsvViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, "id"));
            model.put(CsvViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(CsvViewBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(CsvViewBuilder.KEY_FIELD_MAPPER, (IListToListConverter<HistoricoProcesosEntity>) items -> {
                List<List<String>> rows = new LinkedList<>();

                IDateTimeFormat dateFormat = new DateTimeFormatterFactory().getFormatter(DateTimeFormatterType.TIMESTAMP);

                for (HistoricoProcesosEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(String.valueOf(item.getId()));
                    row.add(item.getNombre());
                    row.add(item.getDescripcion());
                    row.add(item.getEstado());
                    row.add(dateFormat.format(item.getFechaInicialEjecucion()));
                    row.add(dateFormat.format(item.getFechaFinalEjecucion()));
                    row.add(String.valueOf(item.getTiempoEjecucion()));
                    row.add(String.valueOf(item.getNumRegistrosCargados()));
                    row.add(item.getUsuarioEjecucion());
                    row.add(item.getStatusResultado());
                    row.add(item.getAttribute1());
                    row.add(item.getAttribute2());
                    row.add(item.getAttribute3());
                    row.add(item.getAttribute4());
                    row.add(item.getAttribute5());
                    row.add(item.getAttribute6());
                    row.add(item.getAttribute7());

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
    public HistoricoProcesosEntity deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public List<HistoricoProcesosEntity> findAll() throws ResourcesNotFoundException {
        return null;
    }

    @Override
    public HistoricoProcesosEntity findById(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public PaginatedDataDto<HistoricoProcesosEntity> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        return super._findByPage(dataTableRequestDto, "id", Sort.Direction.DESC);
    }

    @Override
    public HistoricoProcesosEntity update(HistoricoProcesosEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::exportXlsx(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            Map<String, Object> model = new HashMap<>();

            model.put(XlsxViewBuilder.KEY_FILENAME, "HistoricoProcesos.xlsx");
            model.put(XlsxViewBuilder.KEY_SHEET_NAME, "Datos");
            model.put(XlsxViewBuilder.KEY_COLUMNS, Arrays.asList(
                    "ID",
                    "NOMBRE",
                    "DESCRIPCIÓN",
                    "ESTADO",
                    "FECHA INICIAL EJECUCIÓN",
                    "FECHA FINAL EJECUCIÓN",
                    "TIEMPO EJECUCIÓN",
                    "NÚMERO REGISTROS CARGADOS",
                    "USUARIO EJECUCIÓN",
                    "STATUS RESULTADO",
                    "COMENTARIOS RESULTADO",
                    "ATRIBUTO 1",
                    "ATRIBUTO 2",
                    "ATRIBUTO 3",
                    "ATRIBUTO 4",
                    "ATRIBUTO 5",
                    "ATRIBUTO 6",
                    "ATRIBUTO 7"
            ));
            model.put(XlsxViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(XlsxViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, "id"));
            model.put(XlsxViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(XlsxByteArrayInputStreamBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(XlsxViewBuilder.KEY_FIELD_MAPPER, (IListToArrayConverter<HistoricoProcesosEntity>) items -> {
                List<Object[]> rows = new LinkedList<>();

                IDateTimeFormat dateFormat = new DateTimeFormatterFactory().getFormatter(DateTimeFormatterType.TIMESTAMP);

                for (HistoricoProcesosEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(String.valueOf(item.getId()));
                    row.add(item.getNombre());
                    row.add(item.getDescripcion());
                    row.add(item.getEstado());
                    row.add(dateFormat.format(item.getFechaInicialEjecucion()));
                    row.add(dateFormat.format(item.getFechaFinalEjecucion()));
                    row.add(String.valueOf(item.getTiempoEjecucion()));
                    row.add(String.valueOf(item.getNumRegistrosCargados()));
                    row.add(item.getUsuarioEjecucion());
                    row.add(item.getStatusResultado());
                    row.add(item.getAttribute1());
                    row.add(item.getAttribute2());
                    row.add(item.getAttribute3());
                    row.add(item.getAttribute4());
                    row.add(item.getAttribute5());
                    row.add(item.getAttribute6());
                    row.add(item.getAttribute7());

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
