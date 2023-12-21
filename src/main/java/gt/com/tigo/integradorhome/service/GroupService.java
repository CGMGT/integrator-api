package gt.com.tigo.integradorhome.service;

import gt.com.tigo.integradorhome.dao.ApplicationParameterRepository;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.model.AdmParametroEntity;
import gt.com.tigo.integradorhome.util.exception.*;
import gt.com.tigo.integradorhome.dao.GroupRepository;
import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import gt.com.tigo.integradorhome.model.AdmGrupoEntity;
import gt.com.tigo.integradorhome.util.SpecFactory;
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
public class GroupService extends AbstractService<AdmGrupoEntity, GroupRepository> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
    public static final String NOMBRE = "nombre";


    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ApplicationParameterRepository applicationParameterRepository;

    @Override
    public List<AdmGrupoEntity> findAll() throws ResourcesNotFoundException {
        return null;
    }

    @Override
    public AdmGrupoEntity findById(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    protected Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        try {
            Map<String, FilterInfoDto> filtersInfo = super.buildFiltersInfo(dataTableRequestDto);

            // create filter specifications
            Specification idSpec = SpecFactory.<AdmGrupoEntity>getEqualSpecification("id", getValueAsLong(filtersInfo, "id"));
            Specification nameSpec = SpecFactory.<AdmGrupoEntity>getLikeSpecification(NOMBRE, getValueAsString(filtersInfo, NOMBRE));
            Specification descriptionSpec = SpecFactory.<AdmGrupoEntity>getLikeSpecification("descripcion", getValueAsString(filtersInfo, "descripcion"));

            // assemble filter specifications
            return Specification
                    .where(getDefaultSpecification())
                    .and(idSpec)
                    .and(nameSpec)
                    .and(descriptionSpec)
                    ;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new InvalidFilterException();
        }
    }

    @Override
    public PaginatedDataDto<AdmGrupoEntity> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        return super._findByPage(dataTableRequestDto, NOMBRE);
    }

    @Override
    public AdmGrupoEntity create(AdmGrupoEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        LOGGER.debug(String.format("@%s::create(%s, %d)", this.getClass().getName(), entity, requesterId));

        entity.setCodigo(entity.getNombre().toLowerCase().replace(" ", "."));
        entity.setActivo("S");
        entity.setEliminable("S");
        entity.setModificable("S");
        entity.setRestringido("N");

        return super._create(entity, requesterId);
    }

    @Override
    public AdmGrupoEntity update(AdmGrupoEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
        LOGGER.debug(String.format("@%s::update(%s, %d)", this.getClass().getName(), entity, requesterId));

        return super._update(entity, requesterId);
    }

    @Override
    public boolean deleteById(Long entityId) throws ResourceDeleteException {
        LOGGER.debug(String.format("@%s::deleteById(%d)", this.getClass().getName(), entityId));

        return super._deleteById(entityId);
    }

    @Override
    public AdmGrupoEntity deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException {
        LOGGER.debug(String.format("@%s::deleteById(%d, %d)", this.getClass().getName(), entityId, requesterId));

        AdmGrupoEntity entity = this.findById(entityId);

        entity.setActivo("N");

        return super._update(entity, requesterId);
    }

    @Override
    public ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        return null;
    }

    public AdmGrupoEntity findByNombre(String nombre) throws ResourceNotFoundException {
        LOGGER.debug(String.format("@%s::findByNombre(%s)", this.getClass().getName(), nombre));

        AdmGrupoEntity entity = this.groupRepository.findByNombreAndActivo(nombre, "S");

        if (entity == null) {
            throw new ResourceNotFoundException();
        }

        return entity;
    }

    public AdmGrupoEntity findDefault() throws ResourceNotFoundException {
        LOGGER.debug(String.format("%s::findDefault()", this.getClass().getName()));

        try {
            AdmParametroEntity parameter = this.applicationParameterRepository.findByCodigo("USER_DEFAULT_GROUP");

            return this.findByNombre(parameter.getValor());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new ResourceNotFoundException();
        }
    }

    @Override
    public ResourceDto exportCsv(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException, IOException {
        return null;
    }
}
