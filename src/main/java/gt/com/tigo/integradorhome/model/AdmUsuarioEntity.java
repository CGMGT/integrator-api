package gt.com.tigo.integradorhome.model;

import gt.com.tigo.integradorhome.util.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "ADM_USUARIO", schema = "", catalog = "")
@SequenceGenerator(name="SEQ_ADM_USUARIO", sequenceName = "SEQ_ADM_USUARIO", initialValue=1, allocationSize=1)
public class AdmUsuarioEntity extends AbstractEntity {
    private Long id;
    private String uuid;
    private String usuario;
    private String nombres;
    private String apellidos;
    private String correoElectronico;
    private String nombreMostrado;
    private String puesto;
    private String telefonoCelular;
    private String telefonoOficina;
    private String ubicacion;
    private String idiomaPreferido;
    private String foto;
    private String token;
    private String paginaInicio;
    private Boolean activo;
    private Timestamp fechaCreacion;
    private String usuarioCreacion;
    private Timestamp fechaModificacion;
    private String usuarioModificacion;
    private List<AdmGrupoEntity> grupos;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ADM_USUARIO")
    @Column(name = "ID", nullable = false, precision = 0)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "UUID", nullable = true, length = 250)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "USUARIO", nullable = false, length = 250)
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Basic
    @Column(name = "NOMBRES", nullable = false, length = 250)
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    @Basic
    @Column(name = "APELLIDOS", nullable = false, length = 250)
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @Basic
    @Column(name = "CORREO_ELECTRONICO", nullable = false, length = 250)
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @Basic
    @Column(name = "NOMBRE_MOSTRADO", nullable = false, length = 250)
    public String getNombreMostrado() {
        return nombreMostrado;
    }

    public void setNombreMostrado(String nombreMostrado) {
        this.nombreMostrado = nombreMostrado;
    }

    @Basic
    @Column(name = "PUESTO", nullable = true, length = 250)
    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    @Basic
    @Column(name = "TELEFONO_CELULAR", nullable = true, length = 250)
    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    @Basic
    @Column(name = "TELEFONO_OFICINA", nullable = true, length = 250)
    public String getTelefonoOficina() {
        return telefonoOficina;
    }

    public void setTelefonoOficina(String telefonoOficina) {
        this.telefonoOficina = telefonoOficina;
    }

    @Basic
    @Column(name = "UBICACION", nullable = true, length = 250)
    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Basic
    @Column(name = "IDIOMA_PREFERIDO", nullable = true, length = 250)
    public String getIdiomaPreferido() {
        return idiomaPreferido;
    }

    public void setIdiomaPreferido(String idiomaPreferido) {
        this.idiomaPreferido = idiomaPreferido;
    }

    @Basic
    @Column(name = "ACTIVO", nullable = false)
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    @Basic
    @Column(name = "PAGINA_INICIO", nullable = true, length = 500)
    public String getPaginaInicio() {
        return paginaInicio;
    }

    public void setPaginaInicio(String paginaInicio) {
        this.paginaInicio = paginaInicio;
    }

    @Basic
    @Column(name = "TOKEN", nullable = true, length = 500)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "FOTO", nullable = true)
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @ManyToMany
    @JoinTable(
            name = "ADM_USUARIO_REL_GRUPO",
            joinColumns = @JoinColumn(name = "USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "GRUPO")
    )
    public List<AdmGrupoEntity> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<AdmGrupoEntity> grupos) {
        this.grupos = grupos;
    }
}
