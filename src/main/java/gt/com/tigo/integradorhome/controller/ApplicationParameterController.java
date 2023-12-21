package gt.com.tigo.integradorhome.controller;

import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.util.controller.AbstractController;
import gt.com.tigo.integradorhome.util.exception.TigoException;
import gt.com.tigo.integradorhome.dto.TigoResponseDto;
import gt.com.tigo.integradorhome.model.AdmParametroEntity;
import gt.com.tigo.integradorhome.security.Authorized;
import gt.com.tigo.integradorhome.service.ApplicationParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@RestController
@RequestMapping("/parameter")
public class ApplicationParameterController extends AbstractController<AdmParametroEntity, ApplicationParameterService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationParameterController.class);

    @Autowired
    private ApplicationParameterService applicationParameterService;

    @Authorized
    @GetMapping("/findByName")
    public ResponseEntity<TigoResponseDto> findByName(@RequestParam(name = "name", required = true) String name) {
        LOGGER.debug(String.format("@%s::findByName(%s)", this.getClass().getName(), name));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.applicationParameterService.findByNombre(name)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Authorized
    @GetMapping("/findByCode")
    public ResponseEntity<TigoResponseDto> findByCode(@RequestParam(name = "code", required = true) String code) {
        LOGGER.debug(String.format("@%s::findByCode(%s)", this.getClass().getName(), code));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.applicationParameterService.findByCodigo(code)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Authorized
    @GetMapping("/findAllowedValues")
    public ResponseEntity<TigoResponseDto> findAllowedValues(@RequestParam(name = "id", required = true) Long id) {
        LOGGER.debug(String.format("@%s::findAllowedValues(%d)", this.getClass().getName(), id));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.applicationParameterService.findAllowedValues(id)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Override
    @Authorized
    public ResponseEntity<TigoResponseDto> findAll() {
        LOGGER.debug(String.format("@%s::findAll()", this.getClass().getName()));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.findAll()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Override
    public ResponseEntity<TigoResponseDto> findById(Long entityId) {
        return null;
    }

    @Override
    public ResponseEntity<TigoResponseDto> findByPage(@RequestBody(required = true) DataTableRequestDto dataTableRequestDto) {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.findByPage(dataTableRequestDto)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Override
    public ResponseEntity<TigoResponseDto> create(AdmParametroEntity entity, Long requesterId) {
        return null;
    }

    @Override
    @Authorized
    public ResponseEntity<TigoResponseDto> update(@RequestBody(required = true) AdmParametroEntity entity, @PathVariable(required = true) Long requesterId) {
        LOGGER.debug(String.format("@%s::update(%s, %d)", this.getClass().getName(), entity, requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.update(entity, requesterId)));
        } catch (TigoException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage()));
        }
    }

    @Override
    public ResponseEntity<TigoResponseDto> deleteById(Long entityId, Long requesterId) {
        return null;
    }

    @Override
    public ModelAndView exportToXlsx(DataTableRequestDto dataTableRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToCsv(@RequestBody DataTableRequestDto dataTableRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<TigoResponseDto> count(DataTableRequestDto dataTableRequestDto) {
        return null;
    }

}
