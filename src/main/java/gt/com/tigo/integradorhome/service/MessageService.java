package gt.com.tigo.integradorhome.service;

import gt.com.tigo.integradorhome.dao.MessageRepository;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.model.AdmUsuarioEntity;
import gt.com.tigo.integradorhome.model.MensajesEntity;
import gt.com.tigo.integradorhome.util.SpecFactory;
import gt.com.tigo.integradorhome.util.exception.*;
import gt.com.tigo.integradorhome.util.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static gt.com.tigo.integradorhome.util.SpecFactory.*;
import static gt.com.tigo.integradorhome.util.SpecFactory.getDefaultSpecification;

@Service
public class MessageService extends AbstractService<MensajesEntity, MessageRepository> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @Override
    protected Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        LOGGER.debug(String.format("@%s::buildFilterSpecification(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            Map<String, FilterInfoDto> filtersInfo = super.buildFiltersInfo(dataTableRequestDto);

            // create filter specifications
            Specification usuarioSpec = SpecFactory.<MensajesEntity, AdmUsuarioEntity>getEqualChildSpecification("usuario", "id", getValueAsLong(filtersInfo, "usuario"));

            // assemble filter specifications
            return Specification
                    .where(getDefaultSpecification())
                    .and(usuarioSpec);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new InvalidFilterException();
        }
    }

    @Override
    public MensajesEntity create(MensajesEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        LOGGER.debug(String.format("@%s::create(%s, %d)", this.getClass().getName(), entity, requesterId));

        entity.setEstado("A");

        return super._create(entity, requesterId);
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
    public MensajesEntity deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public List<MensajesEntity> findAll() throws ResourcesNotFoundException {
        return null;
    }

    @Override
    public MensajesEntity findById(Long id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public PaginatedDataDto<MensajesEntity> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        return super._findByPage(dataTableRequestDto, "id", Sort.Direction.DESC);
    }

    @Override
    public MensajesEntity update(MensajesEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
        return null;
    }

    @Override
    public ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        return null;
    }
}
