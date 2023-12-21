package gt.com.tigo.integradorhome.util.service;

import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.util.exception.InvalidFilterException;
import gt.com.tigo.integradorhome.util.exception.ResourcesNotFoundException;

import java.io.IOException;

public interface CsvExportService {

    ResourceDto exportCsv(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException, IOException;

}
