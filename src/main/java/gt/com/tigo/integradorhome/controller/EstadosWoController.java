package gt.com.tigo.integradorhome.controller;

import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.TigoResponseDto;
import gt.com.tigo.integradorhome.model.EstadosWoEntity;
import gt.com.tigo.integradorhome.security.Authorized;
import gt.com.tigo.integradorhome.service.EstadosWoService;
import gt.com.tigo.integradorhome.util.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@RestController
@RequestMapping("/estados-wo")
public class EstadosWoController  extends AbstractController<EstadosWoEntity, EstadosWoService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosController.class);

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
    public ResponseEntity<TigoResponseDto> create(EstadosWoEntity entity, Long requesterId) {
        return null;
    }

    @Override
    public ResponseEntity<TigoResponseDto> update(EstadosWoEntity entity, Long requesterId) {
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
    public ResponseEntity<ByteArrayResource> exportToCsv(DataTableRequestDto dataTableRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<TigoResponseDto> count(DataTableRequestDto dataTableRequestDto) {
        return null;
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
    @PostMapping("/procesar/{requesterId}")
    public ResponseEntity procesar(@PathVariable(required = true) Long requesterId) {
        LOGGER.debug("@{}::procesar({})", this.getClass().getName(), requesterId);

        try {
            return ResponseEntity.ok(new TigoResponseDto(this.service.procesar(requesterId)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TigoResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @Authorized
    @PostMapping("/export/template")
    public ModelAndView exportTemplate(@RequestBody DataTableRequestDto dataTableRequestDto) {
        LOGGER.debug(String.format("@%s::exportTemplate(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            return super.service.exportTemplate(dataTableRequestDto);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            return null;
        }
    }
}
