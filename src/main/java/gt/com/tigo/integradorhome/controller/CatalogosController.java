package gt.com.tigo.integradorhome.controller;

import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.dto.TigoResponseDto;
import gt.com.tigo.integradorhome.model.CatalogosEntity;
import gt.com.tigo.integradorhome.security.Authorized;
import gt.com.tigo.integradorhome.service.CatalogosService;
import gt.com.tigo.integradorhome.util.CsvViewBuilder;
import gt.com.tigo.integradorhome.util.controller.AbstractController;
import gt.com.tigo.integradorhome.util.exception.TigoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@RestController
@RequestMapping("/catalogos")
public class CatalogosController extends AbstractController<CatalogosEntity, CatalogosService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosController.class);

    @Autowired
    private CatalogosService catalogosService;


    @Override
    public ResponseEntity<TigoResponseDto> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<TigoResponseDto> findById(Long entityId) {
        return null;
    }

    @Override
    @Authorized
    public ResponseEntity<TigoResponseDto> findByPage(@RequestBody(required = true) DataTableRequestDto dataTableRequestDto) {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.findByPage(dataTableRequestDto)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Override
    @Authorized
    public ResponseEntity<TigoResponseDto> create(@RequestBody(required = true) CatalogosEntity entity, @PathVariable(required = true) Long requesterId) {
        LOGGER.debug(String.format("@%s::create(TestEntity, %d)", this.getClass().getName(), requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.create(entity, requesterId)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Override
    @Authorized
    public ResponseEntity<TigoResponseDto> update(@RequestBody(required = true) CatalogosEntity entity, @PathVariable(required = true) Long requesterId) {
        LOGGER.debug(String.format("@%s::update(%s, %d)", this.getClass().getName(), entity, requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.update(entity, requesterId)));
        } catch (TigoException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage()));
        }
    }

    @Override
    @Authorized
    public ResponseEntity<TigoResponseDto> deleteById(@PathVariable(name = "entityId", required = true) Long entityId, @PathVariable(name = "requesterId", required = false) Long requesterId) {
        LOGGER.debug(String.format("@%s::delete(%d, %d)", this.getClass().getName(), entityId, requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.deleteById(entityId)));
        } catch (TigoException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage()));
        }
    }

    @Override
    public ModelAndView exportToXlsx(DataTableRequestDto dataTableRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToCsv(DataTableRequestDto dataTableRequestDto) {
        return null;
    }


    @Authorized
    @PostMapping("/export/xlsx/{grupo}")
    public ModelAndView exportToXlsx(@RequestBody(required = true) DataTableRequestDto dataTableRequestDto, @PathVariable String grupo) {
        LOGGER.debug(String.format("@%s::exportToXlsx(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            return super.service.exportXlsx(dataTableRequestDto, grupo);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            return null;
        }
    }


    @Authorized
    @PostMapping("/export/csv/{grupo}")
    public ResponseEntity<ByteArrayResource> exportToCsv(@RequestBody(required = true) DataTableRequestDto dataTableRequestDto, @PathVariable String grupo) {
        LOGGER.debug(String.format("@%s::exportToCsv(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            ResourceDto resourceDto = this.service.exportCsv(dataTableRequestDto, grupo);

            return ResponseEntity.ok()
                    .headers(CsvViewBuilder.HEADERS)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + resourceDto.getFilename())
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .contentLength(resourceDto.getLength())
                    .body(resourceDto.getResource());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            return null;
        }
    }

    @Override
    public ResponseEntity<TigoResponseDto> count(DataTableRequestDto dataTableRequestDto) {
        return null;
    }

    @Authorized
    @GetMapping("/getGroups")
    public ResponseEntity<TigoResponseDto> getGroups() {
        LOGGER.debug(String.format("@%s::getGroups()", this.getClass().getName()));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.catalogosService.getGroups()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Authorized
    @GetMapping("/getItemsByGroup")
    public ResponseEntity<TigoResponseDto> getItemsByGroup(@RequestParam String group) {
        LOGGER.debug(String.format("@%s::getItemsByGroup(%s)", this.getClass().getName(), group));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.catalogosService.findByGroup(group)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Authorized
    @PostMapping("/import/xlsx")
    public ResponseEntity importFromXlsx(@RequestParam(name = "file", required = true) MultipartFile file, @RequestParam(required = true) Long requesterId) {
        LOGGER.debug(String.format("@%s::upload(%s, %d)", this.getClass().getName(), file.getName(), requesterId));

        try {
            return ResponseEntity.ok(this.service.importXlsx(file, requesterId));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @Authorized
    @PostMapping("/export/template/{grupo}")
    public ModelAndView exportTemplate(@RequestBody DataTableRequestDto dataTableRequestDto, @PathVariable String grupo) {
        LOGGER.debug(String.format("@%s::exportTemplate(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            return super.service.exportTemplate(dataTableRequestDto, grupo);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            return null;
        }
    }

}
