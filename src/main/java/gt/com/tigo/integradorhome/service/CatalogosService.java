package gt.com.tigo.integradorhome.service;

import gt.com.tigo.integradorhome.dao.ApplicationParameterRepository;
import gt.com.tigo.integradorhome.dao.CatalogosRepository;
import gt.com.tigo.integradorhome.dto.*;
import gt.com.tigo.integradorhome.model.AdmParametroEntity;
import gt.com.tigo.integradorhome.model.AdmUsuarioEntity;
import gt.com.tigo.integradorhome.model.CatalogosEntity;
import gt.com.tigo.integradorhome.util.*;
import gt.com.tigo.integradorhome.util.exception.*;
import gt.com.tigo.integradorhome.util.service.AbstractService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CatalogosService extends AbstractService<CatalogosEntity, CatalogosRepository> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosService.class);
    public static final String GRUPO = "grupo";
    public static final String NOMBRE = "nombre";
    public static final String GRUPO1 = "GRUPO";
    public static final String LLAVE = "LLAVE";
    public static final String NOMBRE1 = "NOMBRE";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String ESTADO = "ESTADO";
    public static final String FECHA_CREACION = "FECHA CREACION";
    public static final String USUARIO_CREACION = "USUARIO CREACION";
    public static final String FECHA_MODIFICACION = "FECHA MODIFICACION";
    public static final String USUARIO_MODIFICACION = "USUARIO MODIFICACION";
    public static final String DEPARTMENT = "DEPARTMENT";
    public static final String MUNICIPALITY = "MUNICIPALITY";
    public static final String STATUS = "STATUS";
    public static final String CLASS = "CLASS";
    public static final String SERVICE_TERRITORY_POWER = "SERVICE TERRITORY POWER";
    public static final String SERVICE_TERRITORY_PLIN = "SERVICE TERRITORY PLIN";
    public static final String SERVICE_TERRITORY_PLEX = "SERVICE TERRITORY PLEX";
    public static final String DESCRIPCION = "DESCRIPCION";
    public static final String WORK_TYPE_CATEGORY = "WORK TYPE CATEGORY";
    public static final String DURATION_TYPE = "DURATION TYPE";
    public static final String ESTIMATED_DURATION = "ESTIMATED DURATION";
    public static final String ATTRIBUTE_3 = "ATTRIBUTE3";
    public static final String ATTRIBUTE_4 = "ATTRIBUTE4";
    public static final String ATTRIBUTE_5 = "ATTRIBUTE5";
    public static final String ATTRIBUTE_6 = "ATTRIBUTE6";
    public static final String ATTRIBUTE_7 = "ATTRIBUTE7";
    public static final String WORK_TYPES = "WORK_TYPES";


    @Autowired
    private CatalogosRepository catalogosRepository;

    @Autowired
    private ApplicationParameterRepository applicationParameterRepository;

    @Override
    protected Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        LOGGER.debug(String.format("@%s::buildFilterSpecification(%s)", this.getClass().getName(), dataTableRequestDto));
        try {
            Map<String, FilterInfoDto> filtersInfo = super.buildFiltersInfo(dataTableRequestDto);

            // create filter specifications
            Specification grupoSpec = SpecFactory.<CatalogosEntity>getEqualSpecification(GRUPO, getValueAsString(filtersInfo, GRUPO));
            Specification llaveSpec = SpecFactory.<CatalogosEntity>getLikeSpecification("llave", getValueAsString(filtersInfo, "llave"));
            Specification nombreSpec = SpecFactory.<CatalogosEntity>getLikeSpecification(NOMBRE, getValueAsString(filtersInfo, NOMBRE));
            Specification descriptionSpec = SpecFactory.<CatalogosEntity>getLikeSpecification("descripcion", getValueAsString(filtersInfo, "descripcion"));
            Specification valorSpec = SpecFactory.<CatalogosEntity>getLikeSpecification("valor", getValueAsString(filtersInfo, "valor"));
            Specification estadoSpec = SpecFactory.<CatalogosEntity>getLikeSpecification("estado", getValueAsString(filtersInfo, "estado"));
            Specification attribute1Spec = SpecFactory.<CatalogosEntity>getLikeSpecification("attribute1", getValueAsString(filtersInfo, "attribute1"));
            Specification attribute2Spec = SpecFactory.<CatalogosEntity>getLikeSpecification("attribute2", getValueAsString(filtersInfo, "attribute2"));
            Specification attribute3Spec = SpecFactory.<CatalogosEntity>getLikeSpecification("attribute3", getValueAsString(filtersInfo, "attribute3"));
            Specification attribute4Spec = SpecFactory.<CatalogosEntity>getLikeSpecification("attribute4", getValueAsString(filtersInfo, "attribute4"));
            Specification attribute5Spec = SpecFactory.<CatalogosEntity>getLikeSpecification("attribute5", getValueAsString(filtersInfo, "attribute5"));
            Specification attribute6Spec = SpecFactory.<CatalogosEntity>getLikeSpecification("attribute6", getValueAsString(filtersInfo, "attribute6"));
            Specification attribute7Spec = SpecFactory.<CatalogosEntity>getLikeSpecification("attribute7", getValueAsString(filtersInfo, "attribute7"));


            // assemble filter specifications
            return Specification
                    .where(getDefaultSpecification())
                    .and(grupoSpec)
                    .and(llaveSpec)
                    .and(nombreSpec)
                    .and(descriptionSpec)
                    .and(valorSpec)
                    .and(estadoSpec)
                    .and(attribute1Spec)
                    .and(attribute2Spec)
                    .and(attribute3Spec)
                    .and(attribute4Spec)
                    .and(attribute5Spec)
                    .and(attribute6Spec)
                    .and(attribute7Spec);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new InvalidFilterException();
        }
    }

    @Override
    public CatalogosEntity create(CatalogosEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        LOGGER.debug(String.format("@%s::create(%s, %d)", this.getClass().getName(), entity, requesterId));
        entity.setEstado("A");
        return super._create(entity, requesterId);
    }


    public ResourceDto exportCsv(DataTableRequestDto dataTableRequestDto, String grupo) throws ResourcesNotFoundException, InvalidFilterException, IOException {
        LOGGER.debug(String.format("@%s::exportCsv(%s)", this.getClass().getName(), dataTableRequestDto));

        try{
            Map<String, Object> model = new HashMap<>();

            model.put(CsvViewBuilder.KEY_FILENAME, "Catalogos.csv");
            String[] sitios = new String[] { "ID",
                    GRUPO1,
                    LLAVE,
                    NOMBRE1,
                    LATITUDE,
                    LONGITUDE,
                    ESTADO,
                    FECHA_CREACION,
                    USUARIO_CREACION,
                    FECHA_MODIFICACION,
                    USUARIO_MODIFICACION,
                    DEPARTMENT,
                    MUNICIPALITY,
                    STATUS,
                    CLASS,
                    SERVICE_TERRITORY_POWER,
                    SERVICE_TERRITORY_PLIN,
                    SERVICE_TERRITORY_PLEX};
            String[] workTypes = new String[] { "ID",
                    GRUPO1,
                    LLAVE,
                    NOMBRE1,
                    DESCRIPCION,
                    WORK_TYPE_CATEGORY,
                    ESTADO,
                    FECHA_CREACION,
                    USUARIO_CREACION,
                    FECHA_MODIFICACION,
                    USUARIO_MODIFICACION,
                    DURATION_TYPE,
                    ESTIMATED_DURATION,
                    ATTRIBUTE_3,
                    ATTRIBUTE_4,
                    ATTRIBUTE_5,
                    ATTRIBUTE_6,
                    ATTRIBUTE_7};

            if(grupo.equals(WORK_TYPES)){
                model.put(CsvViewBuilder.KEY_COLUMNS, Arrays.asList(workTypes));}
            else {model.put(CsvViewBuilder.KEY_COLUMNS, Arrays.asList(sitios));}
            model.put(CsvViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(CsvViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, GRUPO));
            model.put(CsvViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(CsvViewBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(CsvViewBuilder.KEY_FIELD_MAPPER, (IListToListConverter<CatalogosEntity>) items -> {
                List<List<String>> rows = new LinkedList<>();

                IDateTimeFormat dateFormat = new DateTimeFormatterFactory().getFormatter(DateTimeFormatterType.DATE);
                for (CatalogosEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(String.valueOf(item.getId()));
                    row.add(item.getGrupo());
                    row.add(item.getLlave());
                    row.add(item.getNombre());
                    row.add(item.getDescripcion());
                    row.add(item.getValor());
                    row.add(item.getEstado());
                    row.add(dateFormat.format(item.getFechaCreacion()));
                    row.add(item.getUsuarioCreacion());
                    row.add(dateFormat.format(item.getFechaModificacion()));
                    row.add(item.getUsuarioModificacion());
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
        LOGGER.debug(String.format("@%s::deleteById(%d)", this.getClass().getName(), entityId));

        return super._deleteById(entityId);
    }

    @Override
    public CatalogosEntity deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException {
        LOGGER.debug(String.format("@%s::deleteById(%d, %d)", this.getClass().getName(), entityId, requesterId));

        CatalogosEntity entity = this.findById(entityId);

        entity.setEstado("I");

        return super._update(entity, requesterId);
    }

    @Override
    public List<CatalogosEntity> findAll() throws ResourcesNotFoundException {
        return null;
    }

    @Override
    public CatalogosEntity findById(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public PaginatedDataDto<CatalogosEntity> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        return super._findByPage(dataTableRequestDto, NOMBRE);
    }

    @Override
    public CatalogosEntity update(CatalogosEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
        LOGGER.debug(String.format("@%s::update(%s, %d)", this.getClass().getName(), entity, requesterId));

        return super._update(entity, requesterId);
    }


    public ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto, String grupo) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::exportXlsx(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            Map<String, Object> model = new HashMap<>();

            model.put(XlsxViewBuilder.KEY_FILENAME, "Catalogos.xlsx");
            model.put(XlsxViewBuilder.KEY_SHEET_NAME, "Datos");

            String[] sitios = new String[] { "ID",
                    GRUPO1,
                    LLAVE,
                    NOMBRE1,
                    LATITUDE,
                    LONGITUDE,
                    ESTADO,
                    FECHA_CREACION,
                    USUARIO_CREACION,
                    FECHA_MODIFICACION,
                    USUARIO_MODIFICACION,
                    DEPARTMENT,
                    MUNICIPALITY,
                    STATUS,
                    CLASS,
                    SERVICE_TERRITORY_POWER,
                    SERVICE_TERRITORY_PLIN,
                    SERVICE_TERRITORY_PLEX};
            String[] workTypes = new String[] { "ID",
                    GRUPO1,
                    LLAVE,
                    NOMBRE1,
                    DESCRIPCION,
                    WORK_TYPE_CATEGORY,
                    ESTADO,
                    FECHA_CREACION,
                    USUARIO_CREACION,
                    FECHA_MODIFICACION,
                    USUARIO_MODIFICACION,
                    DURATION_TYPE,
                    ESTIMATED_DURATION,
                    ATTRIBUTE_3,
                    ATTRIBUTE_4,
                    ATTRIBUTE_5,
                    ATTRIBUTE_6,
                    ATTRIBUTE_7};

            if(grupo.equals(WORK_TYPES)){
                model.put(XlsxViewBuilder.KEY_COLUMNS, Arrays.asList(workTypes));}
            else {model.put(XlsxViewBuilder.KEY_COLUMNS, Arrays.asList(sitios));}
            model.put(XlsxViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(XlsxViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, GRUPO));
            model.put(XlsxViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(XlsxByteArrayInputStreamBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(XlsxViewBuilder.KEY_FIELD_MAPPER, (IListToArrayConverter<CatalogosEntity>) items -> {
                List<Object[]> rows = new LinkedList<>();

                IDateTimeFormat dateFormat = new DateTimeFormatterFactory().getFormatter(DateTimeFormatterType.DATE);

                for (CatalogosEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(String.valueOf(item.getId()));
                    row.add(item.getGrupo());
                    row.add(item.getLlave());
                    row.add(item.getNombre());
                    row.add(item.getDescripcion());
                    row.add(item.getValor());
                    row.add(item.getEstado());
                    row.add(dateFormat.format(item.getFechaCreacion()));
                    row.add(item.getUsuarioCreacion());
                    row.add(dateFormat.format(item.getFechaModificacion()));
                    row.add(item.getUsuarioModificacion());
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

    public List<Object> getGroups() {
        LOGGER.debug(String.format("@%s::getGroups()", this.getClass().getName()));

        return this.catalogosRepository.getGroups();
    }

    public List<CatalogosEntity> findByGroup(String group) {
        LOGGER.debug(String.format("@%s::findByGroup(%s)", this.getClass().getName(), group));

        return this.catalogosRepository.findByGrupoOrderByNombre(group);
    }

    public ModelAndView exportTemplate(DataTableRequestDto dataTableRequestDto, String grupo) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::exportTemplate(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            Map<String, Object> model = new HashMap<>();

            model.put(XlsxViewBuilder.KEY_FILENAME, "Catalogos.xlsx");
            model.put(XlsxViewBuilder.KEY_SHEET_NAME, "Datos");
            String[] sitios = new String[] { "ID",
                    GRUPO1,
                    LLAVE,
                    NOMBRE1,
                    LATITUDE,
                    LONGITUDE,
                    DEPARTMENT,
                    MUNICIPALITY,
                    STATUS,
                    CLASS,
                    SERVICE_TERRITORY_POWER,
                    SERVICE_TERRITORY_PLIN,
                    SERVICE_TERRITORY_PLEX};
            String[] workTypes = new String[] { "ID",
                    GRUPO1,
                    LLAVE,
                    NOMBRE1,
                    DESCRIPCION,
                    WORK_TYPE_CATEGORY,
                    DURATION_TYPE,
                    ESTIMATED_DURATION,
                    ATTRIBUTE_3,
                    ATTRIBUTE_4,
                    ATTRIBUTE_5,
                    ATTRIBUTE_6,
                    ATTRIBUTE_7};

            if(grupo.equals(WORK_TYPES)){
                model.put(XlsxViewBuilder.KEY_COLUMNS, Arrays.asList(workTypes));}
            else {model.put(XlsxViewBuilder.KEY_COLUMNS, Arrays.asList(sitios));}
            model.put(XlsxViewBuilder.KEY_SPECIFICATION, this.buildFilterSpecification(dataTableRequestDto));
            model.put(XlsxViewBuilder.KEY_ORDER_BY, super.buildSortOrder(dataTableRequestDto, "id"));
            model.put(XlsxViewBuilder.KEY_REPOSITORY, super.repository);
            model.put(XlsxByteArrayInputStreamBuilder.KEY_BATCH_SIZE, dataTableRequestDto.getPageSize());
            model.put(XlsxViewBuilder.KEY_FIELD_MAPPER, (IListToArrayConverter<CatalogosEntity>) items -> {
                List<Object[]> rows = new LinkedList<>();

                for (CatalogosEntity item : items) {
                    List<String> row = new LinkedList<>();

                    row.add(String.valueOf(item.getId()));
                    row.add(item.getGrupo());
                    row.add(item.getLlave());
                    row.add(item.getNombre());
                    row.add(item.getDescripcion());
                    row.add(item.getValor());
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

    public boolean importXlsx(MultipartFile multipartFile, Long requesterId) throws ResourceImportException, ResourceConversionException, RequesterNotFoundException {
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
            InputStream is = new ByteArrayInputStream(multipartFile.getBytes());

            Workbook workbook = WorkbookFactory.create(is);

            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = sheet.getPhysicalNumberOfRows();

            Map<Long, CatalogosEntity> catalogEntriesMapFromRepository = new HashMap<>();
            List<CatalogosEntity> catalogEntriesFromXlsx = new LinkedList<>();

            for (lineNumber = 1; lineNumber < rowCount; lineNumber += batchSize) {
                List<Long> ids = new LinkedList<>();

                for (int i = lineNumber; i < rowCount && i < lineNumber + batchSize; i++) {
                    String cell0 = getCellValueAsString(sheet.getRow(i).getCell(0)); // id
                    String cell1 = getCellValueAsString(sheet.getRow(i).getCell(1)); // grupo
                    String cell2 = getCellValueAsString(sheet.getRow(i).getCell(2)); // llave
                    String cell3 = getCellValueAsString(sheet.getRow(i).getCell(3)); // nombre
                    String cell4 = getCellValueAsString(sheet.getRow(i).getCell(4)); // descripcion
                    String cell5 = getCellValueAsString(sheet.getRow(i).getCell(5)); // valor
                    String cell6 = getCellValueAsString(sheet.getRow(i).getCell(6)); // attribute1
                    String cell7 = getCellValueAsString(sheet.getRow(i).getCell(7)); // attribute2
                    String cell8 = getCellValueAsString(sheet.getRow(i).getCell(8)); // attribute3
                    String cell9 = getCellValueAsString(sheet.getRow(i).getCell(9)); // attribute4
                    String cell10 = getCellValueAsString(sheet.getRow(i).getCell(10)); // attribute5
                    String cell11 = getCellValueAsString(sheet.getRow(i).getCell(11)); // attribute6
                    String cell12 = getCellValueAsString(sheet.getRow(i).getCell(12)); // attribute7

                    CatalogosEntity catalogEntity = new CatalogosEntity();

                    if (cell0 != null) {
                        catalogEntity.setId(Long.parseLong(cell0));
                        ids.add(Long.parseLong(cell0));
                    }

                    catalogEntity.setGrupo(cell1);
                    catalogEntity.setLlave(cell2);
                    catalogEntity.setNombre(cell3);
                    catalogEntity.setDescripcion(cell4);
                    catalogEntity.setValor(cell5);
                    catalogEntity.setAttribute1(cell6);
                    catalogEntity.setAttribute2(cell7);
                    catalogEntity.setAttribute3(cell8);
                    catalogEntity.setAttribute4(cell9);
                    catalogEntity.setAttribute5(cell10);
                    catalogEntity.setAttribute6(cell11);
                    catalogEntity.setAttribute7(cell12);

                    catalogEntriesFromXlsx.add(catalogEntity);
                }

                // get all records from database
                List<CatalogosEntity> catalogEntriesFromRepository = this.repository.findAllById(ids);

                catalogEntriesMapFromRepository.clear();

                // fill a map with all records from database
                for (CatalogosEntity catalogEntityFromRepository : catalogEntriesFromRepository) {
                    catalogEntriesMapFromRepository.put(catalogEntityFromRepository.getId(), catalogEntityFromRepository);
                }

                List<CatalogosEntity> finalCatalogEntries = new LinkedList<>();

                for (CatalogosEntity catalogEntityFromXlsx : catalogEntriesFromXlsx) {
                    // if the record from xlsx does not have an id then it is a new catalog entry
                    if (catalogEntityFromXlsx.getId() == null) {
                        catalogEntityFromXlsx.setUsuarioCreacion(requester.getUsuario());
                        catalogEntityFromXlsx.setFechaCreacion(new Timestamp(new Date().getTime()));
                        catalogEntityFromXlsx.setEstado("A");
                        finalCatalogEntries.add(catalogEntityFromXlsx);
                        continue;
                    }

                    CatalogosEntity catalogEntityFromRepository = catalogEntriesMapFromRepository.get(catalogEntityFromXlsx.getId());

                    // if the record from xlsx does not have a corresponding entry in database (less probable) then
                    // it is considered a new catalog entry
                    if (catalogEntityFromRepository == null) {
                        catalogEntityFromXlsx.setUsuarioCreacion(requester.getUsuario());
                        catalogEntityFromXlsx.setFechaCreacion(new Timestamp(new Date().getTime()));
                        catalogEntityFromXlsx.setEstado("A");
                        finalCatalogEntries.add(catalogEntityFromXlsx);
                        continue;
                    }

                    // if the record from xlsx does have a coincidence in database then update that item with
                    // the new data
                    catalogEntityFromRepository.setGrupo(catalogEntityFromXlsx.getGrupo());
                    catalogEntityFromRepository.setLlave(catalogEntityFromXlsx.getLlave());
                    catalogEntityFromRepository.setNombre(catalogEntityFromXlsx.getNombre());
                    catalogEntityFromRepository.setDescripcion(catalogEntityFromXlsx.getDescripcion());
                    catalogEntityFromRepository.setValor(catalogEntityFromXlsx.getValor());
                    catalogEntityFromRepository.setAttribute1(catalogEntityFromXlsx.getAttribute1());
                    catalogEntityFromRepository.setAttribute2(catalogEntityFromXlsx.getAttribute2());
                    catalogEntityFromRepository.setAttribute3(catalogEntityFromXlsx.getAttribute3());
                    catalogEntityFromRepository.setAttribute4(catalogEntityFromXlsx.getAttribute4());
                    catalogEntityFromRepository.setAttribute5(catalogEntityFromXlsx.getAttribute5());
                    catalogEntityFromRepository.setAttribute6(catalogEntityFromXlsx.getAttribute6());
                    catalogEntityFromRepository.setAttribute7(catalogEntityFromXlsx.getAttribute7());
                    catalogEntityFromRepository.setUsuarioModificacion(requester.getUsuario());
                    catalogEntityFromRepository.setFechaModificacion(new Timestamp(new Date().getTime()));

                    finalCatalogEntries.add(catalogEntityFromRepository);
                }

                totalRows += finalCatalogEntries.size();

                this.repository.saveAll(finalCatalogEntries);

                catalogEntriesFromXlsx.clear();
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

    @Override
    public ResourceDto exportCsv(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException, IOException {
        return null;
    }

    @Override
    public ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        return null;
    }
}
