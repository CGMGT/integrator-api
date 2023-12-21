package gt.com.tigo.integradorhome.controller;

import gt.com.tigo.integradorhome.util.controller.AbstractController;
import gt.com.tigo.integradorhome.util.exception.TigoException;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.TigoResponseDto;
import gt.com.tigo.integradorhome.model.AdmGrupoEntity;
import gt.com.tigo.integradorhome.security.Authorized;
import gt.com.tigo.integradorhome.service.GroupService;
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
@RequestMapping("/group")
public class GroupController extends AbstractController<AdmGrupoEntity, GroupService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @Override
    public ResponseEntity<TigoResponseDto> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<TigoResponseDto> findById(Long id) {
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
    public ResponseEntity<TigoResponseDto> create(@RequestBody(required = true) AdmGrupoEntity entity, @PathVariable(required = true)  Long requesterId) {
        LOGGER.debug(String.format("@%s::create(%s, %d)", this.getClass().getName(), entity, requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.create(entity, requesterId)));
        } catch (TigoException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage()));
        }
    }

    @Override
    @Authorized
    public ResponseEntity<TigoResponseDto> update(@RequestBody(required = true) AdmGrupoEntity entity, @PathVariable(required = true) Long requesterId) {
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

    @Authorized
    @GetMapping("/findByName")
    public ResponseEntity<TigoResponseDto> findByname(@RequestParam String name) {
        LOGGER.debug(String.format("%s::findByname(%s)", this.getClass().getName(), name));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.groupService.findByNombre(name)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Authorized
    @GetMapping("/findDefault")
    public ResponseEntity<TigoResponseDto> findDefault() {
        LOGGER.debug(String.format("%s::findDefault()", this.getClass().getName()));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.groupService.findDefault()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
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
