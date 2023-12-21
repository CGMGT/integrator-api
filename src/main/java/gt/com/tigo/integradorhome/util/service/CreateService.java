package gt.com.tigo.integradorhome.util.service;

import gt.com.tigo.integradorhome.util.exception.RequesterNotFoundException;
import gt.com.tigo.integradorhome.util.exception.ResourceCreateException;

public interface CreateService<T> {

    T create(T entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException;

}
