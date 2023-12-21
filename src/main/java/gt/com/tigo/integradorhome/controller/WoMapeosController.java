package gt.com.tigo.integradorhome.controller;

import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.dto.TigoResponseDto;
import gt.com.tigo.integradorhome.model.WoMapeosEntity;
import gt.com.tigo.integradorhome.security.Authorized;
import gt.com.tigo.integradorhome.service.WoMapeosService;
import gt.com.tigo.integradorhome.util.CsvViewBuilder;
import gt.com.tigo.integradorhome.util.controller.AbstractController;
import gt.com.tigo.integradorhome.util.exception.TigoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@RestController
@RequestMapping("/wo-mapeos")
public class WoMapeosController extends AbstractController<WoMapeosEntity, WoMapeosService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WoMapeosController.class);

    @Override
    public ResponseEntity<TigoResponseDto> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<TigoResponseDto> findById(Long entityId) {
        return null;
    }

    @Override
    public ResponseEntity<TigoResponseDto> findByPage(@RequestBody DataTableRequestDto dataTableRequestDto) {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.findByPage(dataTableRequestDto)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Override
    @Authorized
    public ResponseEntity<TigoResponseDto> create(@RequestBody(required = true) WoMapeosEntity entity, @PathVariable(required = true)  Long requesterId) {
        LOGGER.debug(String.format("@%s::create(%s, %d)", this.getClass().getName(), entity, requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.create(entity, requesterId)));
        } catch (TigoException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage()));
        }
    }

    @Override
    @Authorized
    public ResponseEntity<TigoResponseDto> update(@RequestBody(required = true) WoMapeosEntity entity, @PathVariable(required = true) Long requesterId) {
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
    @Authorized
    public ModelAndView exportToXlsx(@RequestBody(required = true) DataTableRequestDto dataTableRequestDto) {
        LOGGER.debug(String.format("@%s::exportToXlsx(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            return super.service.exportXlsx(dataTableRequestDto);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            return null;
        }
    }

    @Override
    @Authorized
    public ResponseEntity<ByteArrayResource> exportToCsv(@RequestBody(required = true) DataTableRequestDto dataTableRequestDto) {
        LOGGER.debug(String.format("@%s::exportToCsv(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            ResourceDto resourceDto = this.service.exportCsv(dataTableRequestDto);

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

    @Override
    public ResponseEntity<TigoResponseDto> count(DataTableRequestDto dataTableRequestDto) {
        return null;
    }
}
