package gt.com.tigo.integradorhome.util.service;

import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.util.exception.InvalidFilterException;

public interface CountService {

    Long count(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException;

}
