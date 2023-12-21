package gt.com.tigo.integradorhome.service;

import gt.com.tigo.integradorhome.dao.ApplicationParameterRepository;
import gt.com.tigo.integradorhome.dao.EstadosWoRepository;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.model.AdmParametroEntity;
import gt.com.tigo.integradorhome.model.AdmUsuarioEntity;
import gt.com.tigo.integradorhome.model.EstadosWoEntity;
import gt.com.tigo.integradorhome.util.IListToArrayConverter;
import gt.com.tigo.integradorhome.util.SpecFactory;
import gt.com.tigo.integradorhome.util.XlsxByteArrayInputStreamBuilder;
import gt.com.tigo.integradorhome.util.XlsxViewBuilder;
import gt.com.tigo.integradorhome.util.exception.*;
import gt.com.tigo.integradorhome.util.service.AbstractService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

import static gt.com.tigo.integradorhome.util.Functions.getCellValueAsString;
import static gt.com.tigo.integradorhome.util.SpecFactory.getDefaultSpecification;
import static gt.com.tigo.integradorhome.util.SpecFactory.getValueAsString;

@Service
public class EstadosWoService extends AbstractService<EstadosWoEntity, EstadosWoRepository> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstadosWoService.class);

    @Autowired
    private ApplicationParameterRepository applicationParameterRepository;

    @Override
    protected Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        LOGGER.debug(String.format("@%s::buildFilterSpecification(%s)", this.getClass().getName(), dataTableRequestDto));
        try {
            Map<String, FilterInfoDto> filtersInfo = super.buildFiltersInfo(dataTableRequestDto);

            // create filter specifications
            Specification otSpec = SpecFactory.<EstadosWoEntity>getEqualSpecification("ot", getValueAsString(filtersInfo, "ot"));
            Specification destinoSpec = SpecFactory.<EstadosWoEntity>getLikeSpecification("destino", getValueAsString(filtersInfo, "destino"));
            Specification estadoSpec = SpecFactory.<EstadosWoEntity>getLikeSpecification("estado", getValueAsString(filtersInfo, "estado"));
            Specification comentariosSpec = SpecFactory.<EstadosWoEntity>getLikeSpecification("comentarios", getValueAsString(filtersInfo, "comentarios"));

            // assemble filter specifications
            return Specification
                    .where(getDefaultSpecification())
                    .and(otSpec)
                    .and(destinoSpec)
                    .and(estadoSpec)
                    .and(comentariosSpec);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new InvalidFilterException();
        }
    }

    @Override
    public EstadosWoEntity create(EstadosWoEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        return null;
    }

    @Override
    public ResourceDto exportCsv(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException, IOException {
        return null;
    }

    @Override
    public boolean deleteById(Long entityId) throws ResourceDeleteException {
        return false;
    }

    @Override
    public EstadosWoEntity deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public List<EstadosWoEntity> findAll() throws ResourcesNotFoundException {
        return null;
    }

    @Override
    public EstadosWoEntity findById(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public PaginatedDataDto<EstadosWoEntity> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        return super._findByPage(dataTableRequestDto, "id", Sort.Direction.DESC);
    }

    @Override
    public EstadosWoEntity update(EstadosWoEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        return null;
    }

    public boolean importXlsx(MultipartFile multipartFile, Long requesterId) throws ResourceNotFoundException, ResourceImportException, ResourceConversionException, RequesterNotFoundException {
        LOGGER.debug(String.format("@%s::importXlsx(%s, %d)", this.getClass().getName(), multipartFile.getOriginalFilename(), requesterId));

        Optional<AdmUsuarioEntity> optionalAdmUsuarioEntity = this.userRepository.findById(requesterId);

        if (!optionalAdmUsuarioEntity.isPresent()) {
            throw new RequesterNotFoundException();
        }

        AdmUsuarioEntity requester = optionalAdmUsuarioEntity.get();

        int lineNumber = 0;
        int totalRows = 0;
        int batchSize;

        try {
            AdmParametroEntity parameter = this.applicationParameterRepository.findByCodigo("TEMPLATE_BATCH_SIZE");

            batchSize = Integer.parseInt(parameter.getValor());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            batchSize = 100;
        }

        try {
            this.repository.deleteAllInBatch();

            InputStream is = new ByteArrayInputStream(multipartFile.getBytes());

            Workbook workbook = WorkbookFactory.create(is);

            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = sheet.getPhysicalNumberOfRows();

            List<EstadosWoEntity> estadosWoEntitiesFromXlsx = new LinkedList<>();

            for (lineNumber = 1; lineNumber < rowCount; lineNumber += batchSize) {
                for (int i = lineNumber; i < rowCount && i < lineNumber + batchSize; i++) {
                    String cell0 = getCellValueAsString(sheet.getRow(i).getCell(0)); // ot
                    String cell1 = getCellValueAsString(sheet.getRow(i).getCell(1)); // destino
                    String cell2 = getCellValueAsString(sheet.getRow(i).getCell(2)); // estado
                    String cell3 = getCellValueAsString(sheet.getRow(i).getCell(3)); // comentarios

                    EstadosWoEntity xxxEntity = new EstadosWoEntity();

                    xxxEntity.setOt(cell0);
                    xxxEntity.setDestino(cell1);
                    xxxEntity.setEstado(cell2);
                    xxxEntity.setComentarios(cell3);
                    xxxEntity.setFechaCreacion(new Timestamp(new java.util.Date().getTime()));
                    xxxEntity.setUsuarioCreacion(requester.getCorreoElectronico());

                    estadosWoEntitiesFromXlsx.add(xxxEntity);
                }

                totalRows += estadosWoEntitiesFromXlsx.size();

                this.repository.saveAll(estadosWoEntitiesFromXlsx);

                estadosWoEntitiesFromXlsx.clear();
            }
        } catch (IOException | IllegalStateException ex) {
            LOGGER.error("{} - Processing line number {}", ex.getMessage(), lineNumber);

            throw new ResourceImportException();
        } catch (Exception ex2) {
            LOGGER.error("{} - Processing line number {}", ex2.getMessage(), lineNumber);

            throw ex2;
        }

        return true;
    }

    public boolean procesar(Long requesterId) throws TigoException {
        LOGGER.debug("@{}::procesar({})", this.getClass().getName(), requesterId);

        Optional<AdmUsuarioEntity> optionalAdmUsuarioEntity = this.userRepository.findById(requesterId);

        if (!optionalAdmUsuarioEntity.isPresent()) {
            throw new RequesterNotFoundException();
        }

        AdmUsuarioEntity requester = optionalAdmUsuarioEntity.get();

        try {
            int result = this.repository.procesar(requester.getCorreoElectronico());

            return result == 0;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new TigoException("Error al procesar estados WO.");
        }
    }

    public ModelAndView exportTemplate(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::exportTemplate(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            Map<String, Object> model = new HashMap<>();

            model.put(XlsxViewBuilder.KEY_FILENAME, "CambiosEstadoWO.xlsx");
            model.put(XlsxViewBuilder.KEY_SHEET_NAME, "Datos");
            model.put(XlsxViewBuilder.KEY_COLUMNS, Arrays.asList(
                    "WO",
                    "DESTINO",
                    "ESTADO",
                    "COMENTARIOS"
            ));
            model.put(XlsxViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(XlsxViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, "ot"));
            model.put(XlsxViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(XlsxByteArrayInputStreamBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(XlsxViewBuilder.KEY_FIELD_MAPPER, (IListToArrayConverter<EstadosWoEntity>) items -> {
                List<Object[]> rows = new LinkedList<>();

                for (EstadosWoEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(item.getOt());
                    row.add(item.getDestino());
                    row.add(item.getEstado());
                    row.add(item.getComentarios());
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
