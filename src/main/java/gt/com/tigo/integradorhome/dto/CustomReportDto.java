package gt.com.tigo.integradorhome.dto;

import gt.com.tigo.integradorhome.model.CustomReportEntity;

import java.sql.Timestamp;
import java.util.List;

public class CustomReportDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private String reporte;
    private String selectedColumns;
    private List<ColumnDto> columns;
    private Timestamp fechaCreacion;
    private String usuarioCreacion;
    private Timestamp fechaModificacion;
    private String usuarioModificacion;

    public CustomReportDto() {
        // not implemented
    }

    public CustomReportDto(CustomReportEntity entity) {
        this.id = entity.getId();
        this.nombre = entity.getNombre();
        this.descripcion = entity.getDescripcion();
        this.selectedColumns = entity.getColumnas();
        this.reporte = entity.getReporte();
        this.fechaCreacion = entity.getFechaCreacion();
        this.usuarioCreacion = entity.getUsuarioCreacion();
        this.fechaModificacion = entity.getFechaModificacion();
        this.usuarioModificacion = entity.getUsuarioModificacion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    public String getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(String selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public List<ColumnDto> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDto> columns) {
        this.columns = columns;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public String toString() {
        return String.format(
                "{id: %d, nombre: %s, descripcion: %s, reporte: %s, selectedColumns: %s, columns: %s, fechaCreacion: %s, usuarioCreacion: %s, fechModificacion: %s, usuarioModificacion: %s}",
                this.id,
                this.nombre,
                this.descripcion,
                this.reporte,
                this.selectedColumns,
                this.columns,
                this.fechaCreacion,
                this.usuarioCreacion,
                this.fechaModificacion,
                this.usuarioModificacion
        );
    }
}
