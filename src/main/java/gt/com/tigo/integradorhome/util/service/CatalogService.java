package gt.com.tigo.integradorhome.util.service;

import gt.com.tigo.integradorhome.dto.CatalogDto;
import gt.com.tigo.integradorhome.util.exception.ResourcesNotFoundException;

import java.util.List;

public interface CatalogService<K, V> {

    List<CatalogDto<K,V>> getDefaultCatalog() throws ResourcesNotFoundException;

}
