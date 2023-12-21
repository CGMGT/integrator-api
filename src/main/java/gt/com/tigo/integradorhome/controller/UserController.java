package gt.com.tigo.integradorhome.controller;

import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.TigoResponseDto;
import gt.com.tigo.integradorhome.model.AdmUsuarioEntity;
import gt.com.tigo.integradorhome.security.Authorized;
import gt.com.tigo.integradorhome.service.UserService;
import gt.com.tigo.integradorhome.util.controller.AbstractController;
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
@RequestMapping("/user")
public class UserController extends AbstractController<AdmUsuarioEntity, UserService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<TigoResponseDto> me(@RequestHeader(name = "Authorization", required = true) String token) {
        LOGGER.debug(String.format("@%s::me(%s)", this.getClass().getName(), token));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.userService.getInfo(token)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }


    @GetMapping("/photo")
    public ResponseEntity<TigoResponseDto> photo(@RequestHeader("Authorization") String token) {
        LOGGER.debug(String.format("@%s::photo(%s)", this.getClass().getName(), token));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.userService.getPhoto(token)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @GetMapping("/findByUid")
    public ResponseEntity<TigoResponseDto> findByUid(@RequestHeader(name = "Authorization", required = true) String token) {
        LOGGER.debug(String.format("@%s::findByUid(********)", this.getClass().getName()));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.userService.findByUid(token)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Authorized
    @GetMapping("/findByEmail")
    public ResponseEntity<TigoResponseDto> findByEmail(@RequestParam(name = "email", required = true) String email) {
        LOGGER.debug(String.format("@%s::findByEmail(%s)", this.getClass().getName(), email));

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.userService.findByEmail(email)));
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
    @Authorized
    public ResponseEntity<TigoResponseDto> findById(@RequestParam(name = "id", required = true) Long id) {
        LOGGER.debug(String.format("@%s::findById(%d)", this.getClass().getName(), id));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.findById(id)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
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
    public ResponseEntity<TigoResponseDto> create(@RequestBody(required = true) AdmUsuarioEntity entity, @PathVariable(required = true) Long requesterId) {
        LOGGER.debug(String.format("@%s::create(%s, %s)", this.getClass().getName(), entity, requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.create(entity, requesterId)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Override
    @Authorized
    public ResponseEntity<TigoResponseDto> update(@RequestBody(required = true) AdmUsuarioEntity entity, @PathVariable(required = true) Long requesterId) {
        LOGGER.debug(String.format("@%s::update(%s, %s)", this.getClass().getName(), entity, requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.update(entity, requesterId)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Override
    public ResponseEntity<TigoResponseDto> deleteById(Long entityId, Long requesterId) {
        LOGGER.debug(String.format("@%s::delete(%s, %s)", this.getClass().getName(), entityId, requesterId));

        try {
            return ResponseEntity.ok(new TigoResponseDto(super.service.deleteById(entityId, requesterId)));
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
