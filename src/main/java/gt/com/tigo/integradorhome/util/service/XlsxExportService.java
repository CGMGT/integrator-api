package gt.com.tigo.integradorhome.util.service;

import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.util.exception.InvalidFilterException;
import gt.com.tigo.integradorhome.util.exception.ResourcesNotFoundException;
import org.springframework.web.servlet.ModelAndView;

public interface XlsxExportService {

    ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException;

}
