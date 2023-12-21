package gt.com.tigo.integradorhome.util.service;

import gt.com.tigo.integradorhome.util.exception.RequesterNotFoundException;
import gt.com.tigo.integradorhome.util.exception.ResourceDeleteException;
import gt.com.tigo.integradorhome.util.exception.ResourceNotFoundException;
import gt.com.tigo.integradorhome.util.exception.ResourceUpdateException;

public interface DeleteByIdService<T> {

    boolean deleteById(Long entityId) throws ResourceDeleteException;

    T deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException;

}
