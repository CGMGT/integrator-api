package gt.com.tigo.integradorhome.util.service;

import gt.com.tigo.integradorhome.util.exception.ResourcesNotFoundException;

import java.util.List;

public interface FindAllService<T> {

    List<T> findAll() throws ResourcesNotFoundException;

}
