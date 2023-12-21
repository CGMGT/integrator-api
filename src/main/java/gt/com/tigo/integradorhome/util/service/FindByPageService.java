package gt.com.tigo.integradorhome.util.service;

import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.util.exception.InvalidFilterException;
import gt.com.tigo.integradorhome.util.exception.ResourcesNotFoundException;

public interface FindByPageService<T> {

    PaginatedDataDto<T> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException;

}
