package gt.com.tigo.integradorhome.util.service;

import gt.com.tigo.integradorhome.dto.CatalogDto;
import gt.com.tigo.integradorhome.util.AbstractEntity;
import gt.com.tigo.integradorhome.util.exception.ResourcesNotFoundException;
import gt.com.tigo.integradorhome.util.repository.DefaultCatalogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCatalogService<T extends AbstractEntity, U extends JpaRepository & JpaSpecificationExecutor & DefaultCatalogRepository>
        extends AbstractService<T, U>
        implements CatalogService<String, String> {

    /**
     * The internal logger for this abstract catalog service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCatalogService.class);

    /**
     * A reference to the main repository for this service.
     */
    @Autowired
    protected U repository;

    protected final List<CatalogDto<String, String>> _getCatalog() throws ResourcesNotFoundException {
        LOGGER.debug(String.format("%s::_getCatalog()", this.getClass().getName()));

        List<Object> entities = this.repository.getCatalog();

        if (entities == null) {
            throw new ResourcesNotFoundException();
        }

        return entities.stream()
                .map(item -> (Object[]) item)
                .map(item -> new CatalogDto<String, String>(item[0].toString(), item[1].toString()))
                .collect(Collectors.toList());
    }

}
