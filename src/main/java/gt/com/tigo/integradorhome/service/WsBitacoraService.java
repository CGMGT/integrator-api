package gt.com.tigo.integradorhome.service;

import gt.com.tigo.integradorhome.dao.WsBitacoraRepository;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.model.AdmUsuarioEntity;
import gt.com.tigo.integradorhome.model.WsBitacoraEntity;
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

@Service
public class WsBitacoraService extends AbstractService<WsBitacoraEntity, WsBitacoraRepository> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsBitacoraService.class);
    public static final String REQUEST_DATE = "requestDate";

    @Override
    protected Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        try {
            Map<String, FilterInfoDto> filtersInfo = super.buildFiltersInfo(dataTableRequestDto);

            String status = getValueAsString(filtersInfo,"status");
            String woNumber = getValueAsString(filtersInfo, "woNumber");


            // create filter specifications
            Specification requestDateSpec = SpecFactory.<WsBitacoraEntity>getBetweenSpecification(REQUEST_DATE, getValueAsDate(filtersInfo, "startDate"), getValueAsDate(filtersInfo, "endDate"));
            Specification statusSpec = status != null && status.equals("ERROR") ? getDefaultSpecification() : SpecFactory.<WsBitacoraEntity>getLikeSpecification("status", "OK");
            Specification woNumberSpec = woNumber != null ? SpecFactory.<WsBitacoraEntity>getLikeSpecification("woNumber", woNumber) : getDefaultSpecification();

            // assemble filter specifications
            return Specification
                    .where(getDefaultSpecification())
                    .and(requestDateSpec)
                    .and(statusSpec)
                    .and(woNumberSpec);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new InvalidFilterException();
        }
    }

    @Override
    public WsBitacoraEntity create(WsBitacoraEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        return null;
    }

    @Override
    public ResourceDto exportCsv(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException, IOException {
        LOGGER.debug(String.format("@%s::exportCsv(%s)", this.getClass().getName(), dataTableRequestDto));

        try{
            Map<String, Object> model = new HashMap<>();

            model.put(CsvViewBuilder.KEY_FILENAME, "BitacoraWS.csv");
            model.put(CsvViewBuilder.KEY_COLUMNS, Arrays.asList(
                    "ID",
                    "WEB SERVICE",
                    "FECHA SOLICITUD",
                    "COMENTARIOS",
                    "SOLICITUD",
                    "RESPUESTA",
                    "TIPO RESPUESTA",
                    "RESUMEN ERROR",
                    "ESTADO",
                    "NÚMERO WO"
            ));
            model.put(CsvViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(CsvViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, REQUEST_DATE));
            model.put(CsvViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(CsvViewBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(CsvViewBuilder.KEY_FIELD_MAPPER, (IListToListConverter<WsBitacoraEntity>) items -> {
                List<List<String>> rows = new LinkedList<>();

                IDateTimeFormat dateFormat = new DateTimeFormatterFactory().getFormatter(DateTimeFormatterType.TIMESTAMP);

                for (WsBitacoraEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(String.valueOf(item.getIdBitacora()));
                    row.add(item.getWebService());
                    row.add(dateFormat.format(item.getRequestDate()));
                    row.add(item.getObservations());
                    row.add(item.getRequest());
                    row.add(item.getResponse());
                    row.add(item.getResponseType());
                    row.add(item.getSummaryError());
                    row.add(item.getStatus());
                    row.add(item.getWoNumber());

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
    public WsBitacoraEntity deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public List<WsBitacoraEntity> findAll() throws ResourcesNotFoundException {
        return null;
    }

    @Override
    public WsBitacoraEntity findById(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public PaginatedDataDto<WsBitacoraEntity> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        return super._findByPage(dataTableRequestDto, "idBitacora", Sort.Direction.DESC);
    }

    @Override
    public WsBitacoraEntity update(WsBitacoraEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::exportXlsx(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            Map<String, Object> model = new HashMap<>();

            model.put(XlsxViewBuilder.KEY_FILENAME, "BitacoraWS.xlsx");
            model.put(XlsxViewBuilder.KEY_SHEET_NAME, "Datos");
            model.put(XlsxViewBuilder.KEY_COLUMNS, Arrays.asList(
                    "ID",
                    "WEB SERVICE",
                    "FECHA SOLICITUD",
                    "COMENTARIOS",
                    "SOLICITUD",
                    "RESPUESTA",
                    "TIPO RESPUESTA",
                    "RESUMEN ERROR",
                    "ESTADO",
                    "NÚMERO WO"
            ));
            model.put(XlsxViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(XlsxViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, REQUEST_DATE));
            model.put(XlsxViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(XlsxByteArrayInputStreamBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(XlsxViewBuilder.KEY_FIELD_MAPPER, (IListToArrayConverter<WsBitacoraEntity>) items -> {
                List<Object[]> rows = new LinkedList<>();

                IDateTimeFormat dateFormat = new DateTimeFormatterFactory().getFormatter(DateTimeFormatterType.TIMESTAMP);

                for (WsBitacoraEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(String.valueOf(item.getIdBitacora()));
                    row.add(item.getWebService());
                    row.add(dateFormat.format(item.getRequestDate()));
                    row.add(item.getObservations());
                    row.add(item.getRequest());
                    row.add(item.getResponse());
                    row.add(item.getResponseType());
                    row.add(item.getSummaryError());
                    row.add(item.getStatus());
                    row.add(item.getWoNumber());

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

    public boolean procesar(Long requesterId, String idBitacora) throws TigoException {
        LOGGER.debug("@{}::procesar({}, {})", this.getClass().getName(), requesterId, idBitacora);

        Optional<AdmUsuarioEntity> optionalAdmUsuarioEntity = this.userRepository.findById(requesterId);

        if (!optionalAdmUsuarioEntity.isPresent()) {
            throw new RequesterNotFoundException();
        }

        AdmUsuarioEntity requester = optionalAdmUsuarioEntity.get();

        try {
            int result = this.repository.procesar(requester.getCorreoElectronico(), idBitacora);

            return result == 0;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new TigoException("Error al procesar registro de bitacora WS.");
        }
    }
}
