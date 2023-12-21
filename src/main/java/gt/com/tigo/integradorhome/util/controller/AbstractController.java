package gt.com.tigo.integradorhome.util.controller;

import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.TigoResponseDto;
import gt.com.tigo.integradorhome.util.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractController<T, U extends AbstractService> {

    @Autowired
    protected U service;

    @GetMapping("/findAll")
    public abstract ResponseEntity<TigoResponseDto> findAll();

    @GetMapping("/findById")
    public abstract ResponseEntity<TigoResponseDto> findById(Long entityId);

    @PostMapping("/findByPage")
    public abstract ResponseEntity<TigoResponseDto> findByPage(DataTableRequestDto dataTableRequestDto);

    @PostMapping("/create/{requesterId}")
    public abstract ResponseEntity<TigoResponseDto> create(T entity, Long requesterId);

    @PutMapping("/update/{requesterId}")
    public abstract ResponseEntity<TigoResponseDto> update(T entity, Long requesterId);

    @DeleteMapping("/deleteById/{entityId}/{requesterId}")
    public abstract ResponseEntity<TigoResponseDto> deleteById(Long entityId, Long requesterId);

    @PostMapping("/export/xlsx")
    public abstract ModelAndView exportToXlsx(DataTableRequestDto dataTableRequestDto);

    @PostMapping("/export/csv")
    public abstract ResponseEntity<ByteArrayResource> exportToCsv(DataTableRequestDto dataTableRequestDto);

    @PostMapping("/count")
    public abstract ResponseEntity<TigoResponseDto> count(DataTableRequestDto dataTableRequestDto);

}
