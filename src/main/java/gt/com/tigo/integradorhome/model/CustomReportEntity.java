package gt.com.tigo.integradorhome.model;

import gt.com.tigo.integradorhome.dto.CustomReportDto;
import gt.com.tigo.integradorhome.util.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "custom_report", schema = "", catalog = "")
public class CustomReportEntity extends AbstractEntity {
    private Long id;
    private String nombre;
    private String descripcion;
    private String columnas;
    private String reporte;
    private Timestamp fechaCreacion;
    private String usuarioCreacion;
    private Timestamp fechaModificacion;
    private String usuarioModificacion;

    public CustomReportEntity() {
        // not implemented
    }

    public CustomReportEntity(CustomReportDto customReportDto) {
        this.id = customReportDto.getId();
        this.nombre = customReportDto.getNombre();
        this.descripcion = customReportDto.getDescripcion();
        this.columnas = customReportDto.getSelectedColumns();
        this.reporte = customReportDto.getReporte();
        this.fechaCreacion = customReportDto.getFechaCreacion();
        this.usuarioCreacion = customReportDto.getUsuarioCreacion();
        this.fechaModificacion = customReportDto.getFechaModificacion();
        this.usuarioModificacion = customReportDto.getUsuarioModificacion();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombre", nullable = false, length = 150)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "descripcion", nullable = false, length = 150)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "columnas", nullable = false, length = 5000)
    public String getColumnas() {
        return columnas;
    }

    public void setColumnas(String columnas) {
        this.columnas = columnas;
    }

    @Basic
    @Column(name = "reporte", nullable = false, length = 150)
    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    @Basic
    @Column(name = "fecha_creacion", nullable = false)
    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Basic
    @Column(name = "usuario_creacion", nullable = false, length = 150)
    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    @Basic
    @Column(name = "fecha_modificacion", nullable = true)
    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Basic
    @Column(name = "usuario_modificacion", nullable = true, length = 150)
    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomReportEntity that = (CustomReportEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre) && Objects.equals(descripcion, that.descripcion) && Objects.equals(columnas, that.columnas) && Objects.equals(reporte, that.reporte) && Objects.equals(fechaCreacion, that.fechaCreacion) && Objects.equals(usuarioCreacion, that.usuarioCreacion) && Objects.equals(fechaModificacion, that.fechaModificacion) && Objects.equals(usuarioModificacion, that.usuarioModificacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion, columnas, reporte, fechaCreacion, usuarioCreacion, fechaModificacion, usuarioModificacion);
    }
}
