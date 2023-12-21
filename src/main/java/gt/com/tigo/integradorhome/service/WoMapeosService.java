package gt.com.tigo.integradorhome.service;

import gt.com.tigo.integradorhome.dao.WoMapeosRepository;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.model.WoMapeosEntity;
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
public class WoMapeosService extends AbstractService<WoMapeosEntity, WoMapeosRepository> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WoMapeosService.class);

    @Override
    protected Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        LOGGER.debug(String.format("@%s::buildFilterSpecification(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            Map<String, FilterInfoDto> filtersInfo = super.buildFiltersInfo(dataTableRequestDto);

            // create filter specifications
            Specification tipoSpec = SpecFactory.<WoMapeosEntity>getLikeSpecification("tipo", getValueAsString(filtersInfo, "tipo"));
            Specification sistemaOrigenSpec = SpecFactory.<WoMapeosEntity>getLikeSpecification("sistemaOrigen", getValueAsString(filtersInfo, "sistemaOrigen"));
            Specification sistemaDestinoSpec = SpecFactory.<WoMapeosEntity>getLikeSpecification("sistemaDestino", getValueAsString(filtersInfo, "sistemaDestino"));
            Specification valorOrigenSpec = SpecFactory.<WoMapeosEntity>getLikeSpecification("valorOrigen", getValueAsString(filtersInfo, "valorOrigen"));
            Specification valorDestinoSpec = SpecFactory.<WoMapeosEntity>getLikeSpecification("valorDestino", getValueAsString(filtersInfo, "valorDestino"));

            // assemble filter specifications
            return Specification
                    .where(getDefaultSpecification())
                    .and(tipoSpec)
                    .and(sistemaOrigenSpec)
                    .and(sistemaDestinoSpec)
                    .and(valorOrigenSpec)
                    .and(valorDestinoSpec);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new InvalidFilterException();
        }
    }

    @Override
    public WoMapeosEntity create(WoMapeosEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        LOGGER.debug(String.format("@%s::create(%s, %d)", this.getClass().getName(), entity, requesterId));

        entity.setEstado("A");

        return super._create(entity, requesterId);
    }

    @Override
    public ResourceDto exportCsv(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException, IOException {
        LOGGER.debug(String.format("@%s::exportCsv(%s)", this.getClass().getName(), dataTableRequestDto));

        try{
            Map<String, Object> model = new HashMap<>();

            model.put(CsvViewBuilder.KEY_FILENAME, "WoMapeos.csv");
            model.put(CsvViewBuilder.KEY_COLUMNS, Arrays.asList(
                    "ID",
                    "TIPO",
                    "ID ORIGEN",
                    "ID DESTINO",
                    "SISTEMA ORIGEN",
                    "SISTEMA DESTINO",
                    "VALOR ORIGEN",
                    "VALOR DESTINO",
                    "ATTRIBUTE1",
                    "ATTRIBUTE2",
                    "ATTRIBUTE3",
                    "ESTADO",
                    "FECHA CREACIÓN",
                    "CREADO POR",
                    "FECHA ÚLTIMA MODIFICACIÓN",
                    "MODIFICADO POR"
            ));
            model.put(CsvViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(CsvViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, "idMapeo"));
            model.put(CsvViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(CsvViewBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(CsvViewBuilder.KEY_FIELD_MAPPER, (IListToListConverter<WoMapeosEntity>) items -> {
                List<List<String>> rows = new LinkedList<>();

                IDateTimeFormat dateFormat = new DateTimeFormatterFactory().getFormatter(DateTimeFormatterType.TIMESTAMP);

                for (WoMapeosEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(String.valueOf(item.getIdMapeo()));
                    row.add(item.getTipo());
                    row.add(String.valueOf(item.getIdOrigen()));
                    row.add(String.valueOf(item.getIdDestino()));
                    row.add(item.getSistemaOrigen());
                    row.add(item.getSistemaDestino());
                    row.add(item.getValorOrigen());
                    row.add(item.getValorDestino());
                    row.add(item.getAttribute1());
                    row.add(item.getAttribute2());
                    row.add(item.getAttribute3());
                    row.add(item.getEstado());
                    row.add(dateFormat.format(item.getFechaCreacion()));
                    row.add(item.getUsuarioCreacion());
                    row.add(dateFormat.format(item.getFechaModificacion()));
                    row.add(item.getUsuarioModificacion());

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
        LOGGER.debug(String.format("@%s::deleteById(%d)", this.getClass().getName(), entityId));

        return super._deleteById(entityId);
    }

    @Override
    public WoMapeosEntity deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException {
        LOGGER.debug(String.format("@%s::deleteById(%d, %d)", this.getClass().getName(), entityId, requesterId));

        WoMapeosEntity entity = this.findById(entityId);

        entity.setEstado("I");

        return super._update(entity, requesterId);
    }

    @Override
    public List<WoMapeosEntity> findAll() throws ResourcesNotFoundException {
        return null;
    }

    @Override
    public WoMapeosEntity findById(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public PaginatedDataDto<WoMapeosEntity> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        return super._findByPage(dataTableRequestDto, "tipo", Sort.Direction.ASC);
    }

    @Override
    public WoMapeosEntity update(WoMapeosEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
        LOGGER.debug(String.format("@%s::update(%s, %d)", this.getClass().getName(), entity, requesterId));

        return super._update(entity, requesterId);
    }

    @Override
    public ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::exportXlsx(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            Map<String, Object> model = new HashMap<>();

            model.put(XlsxViewBuilder.KEY_FILENAME, "WoMapeos.xlsx");
            model.put(XlsxViewBuilder.KEY_SHEET_NAME, "Datos");
            model.put(XlsxViewBuilder.KEY_COLUMNS, Arrays.asList(
                    "ID",
                    "TIPO",
                    "ID ORIGEN",
                    "ID DESTINO",
                    "SISTEMA ORIGEN",
                    "SISTEMA DESTINO",
                    "VALOR ORIGEN",
                    "VALOR DESTINO",
                    "ATTRIBUTE1",
                    "ATTRIBUTE2",
                    "ATTRIBUTE3",
                    "ESTADO",
                    "FECHA CREACIÓN",
                    "CREADO POR",
                    "FECHA ÚLTIMA MODIFICACIÓN",
                    "MODIFICADO POR"
            ));
            model.put(XlsxViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(XlsxViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, "idMapeo"));
            model.put(XlsxViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(XlsxByteArrayInputStreamBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(XlsxViewBuilder.KEY_FIELD_MAPPER, (IListToArrayConverter<WoMapeosEntity>) items -> {
                List<Object[]> rows = new LinkedList<>();

                IDateTimeFormat dateFormat = new DateTimeFormatterFactory().getFormatter(DateTimeFormatterType.TIMESTAMP);

                for (WoMapeosEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(String.valueOf(item.getIdMapeo()));
                    row.add(item.getTipo());
                    row.add(String.valueOf(item.getIdOrigen()));
                    row.add(String.valueOf(item.getIdDestino()));
                    row.add(item.getSistemaOrigen());
                    row.add(item.getSistemaDestino());
                    row.add(item.getValorOrigen());
                    row.add(item.getValorDestino());
                    row.add(item.getAttribute1());
                    row.add(item.getAttribute2());
                    row.add(item.getAttribute3());
                    row.add(item.getEstado());
                    row.add(dateFormat.format(item.getFechaCreacion()));
                    row.add(item.getUsuarioCreacion());
                    row.add(dateFormat.format(item.getFechaModificacion()));
                    row.add(item.getUsuarioModificacion());

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
