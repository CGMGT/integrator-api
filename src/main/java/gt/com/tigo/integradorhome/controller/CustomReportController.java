package gt.com.tigo.integradorhome.controller;

import gt.com.tigo.integradorhome.dto.CustomReportDto;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.dto.TigoResponseDto;
import gt.com.tigo.integradorhome.model.CustomReportEntity;
import gt.com.tigo.integradorhome.service.CustomReportService;
import gt.com.tigo.integradorhome.util.CsvViewBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@RestController
@RequestMapping("/custom-report")
public class CustomReportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomReportController.class);

    @Autowired
    private CustomReportService customReportService;

    @PostMapping("/findByPage")
    public ResponseEntity<TigoResponseDto> findByPage(@RequestBody DataTableRequestDto dataTableRequestDto) {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.customReportService.findByPage(dataTableRequestDto)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @PostMapping("/create/{requesterId}")
    public ResponseEntity<TigoResponseDto> create(@RequestBody CustomReportDto customReportDto, @PathVariable Long requesterId) {
        LOGGER.debug(String.format("@%s::create(%s, %d)", this.getClass().getName(), customReportDto, requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.customReportService.create(new CustomReportEntity(customReportDto), requesterId)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @PutMapping("/update/{requesterId}")
    public ResponseEntity<TigoResponseDto> update(@RequestBody CustomReportDto customReportDto, @PathVariable Long requesterId) {
        LOGGER.debug(String.format("@%s::update(%s, %d)", this.getClass().getName(), customReportDto, requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.customReportService.update(new CustomReportEntity(customReportDto), requesterId)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @DeleteMapping("/deleteById/{entityId}/{requesterId}")
    public ResponseEntity<TigoResponseDto> deleteById(@PathVariable Long entityId, @PathVariable Long requesterId) {
        LOGGER.debug(String.format("@%s::delete(%d, %d)", this.getClass().getName(), entityId, requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.customReportService.deleteById(entityId)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @PostMapping("/export/xlsx")
    public ModelAndView exportToXlsx(@RequestBody DataTableRequestDto dataTableRequestDto) {
        LOGGER.debug(String.format("@%s::exportToXlsx(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            return this.customReportService.exportXlsx(dataTableRequestDto);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            return null;
        }
    }

    @PostMapping("/export/csv")
    public ResponseEntity<ByteArrayResource> exportToCsv(@RequestBody DataTableRequestDto dataTableRequestDto) {
        LOGGER.debug(String.format("@%s::exportToCsv(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            ResourceDto resourceDto = this.customReportService.exportCsv(dataTableRequestDto);

            return ResponseEntity.ok()
                    .headers(CsvViewBuilder.HEADERS)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + resourceDto.getFilename())
                    .contentType(resourceDto.getMediaType())
                    .contentLength(resourceDto.getLength())
                    .body(resourceDto.getResource());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            return null;
        }
    }

    @GetMapping("/resumenWO/{periodo}")
    public ResponseEntity<TigoResponseDto> getResumenWO(@PathVariable String periodo) {
        LOGGER.debug(String.format("%s::getResumenWO()", this.getClass().getName()));

        try {
            return ResponseEntity.ok(new TigoResponseDto(customReportService.getResumenWO(periodo)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @GetMapping("/resByTaskType")
    public ResponseEntity<TigoResponseDto> getResByTaskType() {
        LOGGER.debug(String.format("%s::getResByTaskType()", this.getClass().getName()));

        try {
            return ResponseEntity.ok(new TigoResponseDto(customReportService.getResByTaskType()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @GetMapping("/resByDistrict")
    public ResponseEntity<TigoResponseDto> getResByDistrict(@RequestParam(required = true) Long period) {
        LOGGER.debug(String.format("%s::getResByDistrict()", this.getClass().getName()));

        try {
            return ResponseEntity.ok(new TigoResponseDto(customReportService.getResByDistrict()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @GetMapping("/last10errors/{periodo}")
    public ResponseEntity<TigoResponseDto> getLast10Errors(@PathVariable String periodo) {
        LOGGER.debug(String.format("%s::getLast10Errors()", this.getClass().getName()));

        try {
            return ResponseEntity.ok(new TigoResponseDto(customReportService.getLast10Errors(periodo)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @GetMapping("/getperiods")
    public ResponseEntity<TigoResponseDto> getPeriods() {
        LOGGER.debug(String.format("%s::getPeriods()", this.getClass().getName()));

        try {
            return ResponseEntity.ok(new TigoResponseDto(customReportService.getPeriods()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

}
