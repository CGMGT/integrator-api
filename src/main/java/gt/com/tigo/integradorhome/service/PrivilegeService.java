package gt.com.tigo.integradorhome.service;

import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.util.exception.*;
import gt.com.tigo.integradorhome.dao.PrivilegeRepository;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.model.AdmPermisoEntity;
import gt.com.tigo.integradorhome.util.SpecFactory;
import gt.com.tigo.integradorhome.util.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static gt.com.tigo.integradorhome.util.SpecFactory.*;

@Service
public class PrivilegeService extends AbstractService<AdmPermisoEntity, PrivilegeRepository> {


    private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeService.class);
    public static final String CODIGO = "codigo";

    @Override
    public List<AdmPermisoEntity> findAll() throws ResourcesNotFoundException {
        LOGGER.debug(String.format("%s::findAll()", this.getClass().getName()));

        return super._findAll(CODIGO);
    }

    @Override
    protected Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        try {
            Map<String, FilterInfoDto> filtersInfo = super.buildFiltersInfo(dataTableRequestDto);

            // create filter specifications
            Specification idSpec = SpecFactory.<AdmPermisoEntity>getEqualSpecification("id", getValueAsLong(filtersInfo, "id"));
            Specification nameSpec = SpecFactory.<AdmPermisoEntity>getLikeSpecification("nombre", getValueAsString(filtersInfo, "nombre"));
            Specification descriptionSpec = SpecFactory.<AdmPermisoEntity>getLikeSpecification("descripcion", getValueAsString(filtersInfo, "descripcion"));
            Specification codigoSpec = SpecFactory.<AdmPermisoEntity>getLikeSpecification(CODIGO, getValueAsString(filtersInfo, CODIGO));


            // assemble filter specifications
            return Specification
                    .where(getDefaultSpecification())
                    .and(idSpec)
                    .and(nameSpec)
                    .and(descriptionSpec)
                    .and(codigoSpec)
                    ;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new InvalidFilterException();
        }
    }

    @Override
    public PaginatedDataDto<AdmPermisoEntity> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto.toString()));

        return super._findByPage(dataTableRequestDto, CODIGO);
    }

    @Override
    public AdmPermisoEntity create(AdmPermisoEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        return null;
    }

    @Override
    public boolean deleteById(Long entityId) throws ResourceDeleteException {
        return false;
    }

    @Override
    public AdmPermisoEntity deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public AdmPermisoEntity findById(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public AdmPermisoEntity update(AdmPermisoEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
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
