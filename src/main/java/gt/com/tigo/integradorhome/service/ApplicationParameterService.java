package gt.com.tigo.integradorhome.service;

import gt.com.tigo.integradorhome.dao.ApplicationParameterInternalRepository;
import gt.com.tigo.integradorhome.dao.NativeQueryRepository;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.model.AdmParametroEntity;
import gt.com.tigo.integradorhome.util.SpecFactory;
import gt.com.tigo.integradorhome.util.exception.*;
import gt.com.tigo.integradorhome.dao.ApplicationParameterRepository;
import gt.com.tigo.integradorhome.model.AdmParametroInternalEntity;
import gt.com.tigo.integradorhome.util.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static gt.com.tigo.integradorhome.util.SpecFactory.*;

@Service
public class ApplicationParameterService extends AbstractService<AdmParametroEntity, ApplicationParameterRepository> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationParameterService.class);
    public static final String NOMBRE = "nombre";

    @Autowired
    private ApplicationParameterRepository applicationParameterRepository;

    @Autowired
    private ApplicationParameterInternalRepository applicationParameterInternalRepository;

    @Autowired
    private NativeQueryRepository<String> nativeQueryRepository;

    public AdmParametroEntity findByNombre(String name) throws ResourceNotFoundException {
        LOGGER.debug(String.format("%s::findByNombre(%s)", this.getClass().getName(), name));

        try {
            return this.applicationParameterRepository.findByNombre(name);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new ResourceNotFoundException();
        }
    }

    public AdmParametroEntity findByCodigo(String code) throws ResourceNotFoundException {
        LOGGER.debug(String.format("%s::findByCodigo(%s)", this.getClass().getName(), code));

        try {
            return this.applicationParameterRepository.findByCodigo(code);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new ResourceNotFoundException();
        }
    }

    public List<String> findAllowedValues(Long parameterId) throws ResourceNotFoundException, ResourcesNotFoundException {
        LOGGER.debug(String.format("%s::findAllowedValues(%d)", this.getClass().getName(), parameterId));

        AdmParametroInternalEntity parameter = this.applicationParameterInternalRepository.findById(parameterId).get();

        if (parameter == null) {
            throw new ResourceNotFoundException();
        }

        try {
            return this.nativeQueryRepository.executeNativeQuery(parameter.getValoresDisponibles());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new ResourcesNotFoundException();
        }
    }

    @Override
    public List<AdmParametroEntity> findAll() throws ResourcesNotFoundException {
        LOGGER.debug(String.format("%s::findAll()", this.getClass().getName()));

        return super._findAll(NOMBRE);
    }

    @Override
    public AdmParametroEntity findById(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public PaginatedDataDto<AdmParametroEntity> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        return super._findByPage(dataTableRequestDto, "id");
    }

    @Override
    protected Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        try {
            Map<String, FilterInfoDto> filtersInfo = super.buildFiltersInfo(dataTableRequestDto);

            // create filter specifications
            Specification idSpec = SpecFactory.<AdmParametroEntity>getEqualSpecification("id", getValueAsLong(filtersInfo, "id"));
            Specification categorySpec = SpecFactory.<AdmParametroEntity>getLikeSpecification("categoria", getValueAsString(filtersInfo, "categoria"));
            Specification nameSpec = SpecFactory.<AdmParametroEntity>getLikeSpecification(NOMBRE, getValueAsString(filtersInfo, NOMBRE));
            Specification descriptionSpec = SpecFactory.<AdmParametroEntity>getLikeSpecification("descripcion", getValueAsString(filtersInfo, "descripcion"));
            Specification valueSpec = SpecFactory.<AdmParametroEntity>getLikeSpecification("valor", getValueAsString(filtersInfo, "valor"));

            // assemble filter specifications
            return Specification
                    .where(getDefaultSpecification())
                    .and(idSpec)
                    .and(categorySpec)
                    .and(nameSpec)
                    .and(descriptionSpec)
                    .and(valueSpec)
                    ;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new InvalidFilterException();
        }
    }

    @Override
    public AdmParametroEntity create(AdmParametroEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        return null;
    }

    @Override
    public AdmParametroEntity update(AdmParametroEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
        LOGGER.debug(String.format("@%s::update(%s, %d)", this.getClass().getName(), entity, requesterId));

        return super._update(entity, requesterId);
    }

    @Override
    public boolean deleteById(Long entityId) throws ResourceDeleteException {
        return false;
    }

    @Override
    public AdmParametroEntity deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        return null;
    }

    @Override
    public ResourceDto exportCsv(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException, IOException {
        return null;
    }
}
