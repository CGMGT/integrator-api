package gt.com.tigo.integradorhome.util.service;

import gt.com.tigo.integradorhome.util.exception.RequesterNotFoundException;
import gt.com.tigo.integradorhome.util.exception.ResourceUpdateException;

public interface UpdateService<T> {

    T update(T entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException;

}
