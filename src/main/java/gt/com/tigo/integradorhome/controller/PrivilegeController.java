package gt.com.tigo.integradorhome.controller;

import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.TigoResponseDto;
import gt.com.tigo.integradorhome.model.AdmPermisoEntity;
import gt.com.tigo.integradorhome.security.Authorized;
import gt.com.tigo.integradorhome.service.PrivilegeService;
import gt.com.tigo.integradorhome.util.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@RestController
@RequestMapping("/privilege")
public class PrivilegeController extends AbstractController<AdmPermisoEntity, PrivilegeService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeController.class);

    @Override
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
    public ResponseEntity<TigoResponseDto> create(AdmPermisoEntity entity, Long requesterId) {
        return null;
    }

    @Override
    public ResponseEntity<TigoResponseDto> update(AdmPermisoEntity entity, Long requesterId) {
        return null;
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
