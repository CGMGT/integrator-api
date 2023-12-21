package gt.com.tigo.integradorhome.model;

import gt.com.tigo.integradorhome.util.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "ADM_GRUPO", schema = "", catalog = "")
@SequenceGenerator(name="SEQ_ADM_GRUPO", sequenceName = "SEQ_ADM_GRUPO", initialValue=1, allocationSize=1)
public class AdmGrupoEntity extends AbstractEntity {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String activo;
    private String eliminable;
    private String modificable;
    private String restringido;
    private Timestamp fechaCreacion;
    private String usuarioCreacion;
    private Timestamp fechaModificacion;
    private String usuarioModificacion;
    private List<AdmPermisoEntity> permisos;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ADM_GRUPO")
    @Column(name = "ID", nullable = false, precision = 0)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CODIGO", nullable = false, length = 250)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "NOMBRE", nullable = false, length = 250)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "DESCRIPCION", nullable = false, length = 1000)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "ACTIVO", nullable = false)
    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    @Basic
    @Column(name = "ELIMINABLE", nullable = false)
    public String getEliminable() {
        return eliminable;
    }

    public void setEliminable(String eliminable) {
        this.eliminable = eliminable;
    }

    @Basic
    @Column(name = "MODIFICABLE", nullable = false)
    public String getModificable() {
        return modificable;
    }

    public void setModificable(String modificable) {
        this.modificable = modificable;
    }

    @Basic
    @Column(name = "RESTRINGIDO", nullable = false)
    public String getRestringido() {
        return restringido;
    }

    public void setRestringido(String restringido) {
        this.restringido = restringido;
    }

    @Basic
    @Column(name = "FECHA_CREACION", nullable = false)
    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Basic
    @Column(name = "USUARIO_CREACION", nullable = false, length = 250)
    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    @Basic
    @Column(name = "FECHA_MODIFICACION", nullable = true)
    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Basic
    @Column(name = "USUARIO_MODIFICACION", nullable = true, length = 250)
    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    @ManyToMany
    @JoinTable(
            name = "ADM_GRUPO_REL_PERMISO",
            joinColumns = @JoinColumn(name = "GRUPO"),
            inverseJoinColumns = @JoinColumn(name = "PERMISO")
    )
    public List<AdmPermisoEntity> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<AdmPermisoEntity> permisos) {
        this.permisos = permisos;
    }
}
