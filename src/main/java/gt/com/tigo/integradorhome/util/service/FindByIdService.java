package gt.com.tigo.integradorhome.util.service;

import gt.com.tigo.integradorhome.util.exception.ResourceNotFoundException;

public interface FindByIdService<T> {

    T findById(Long id) throws ResourceNotFoundException;

}
