package gt.com.tigo.integradorhome.model;

import gt.com.tigo.integradorhome.util.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "catalogos", schema = "", catalog = "")
@SequenceGenerator(name="SEQ_CATALOGOS", sequenceName = "SEQ_CATALOGOS", initialValue=1, allocationSize=1)
public class CatalogosEntity extends AbstractEntity {
    private Long id;
    private String grupo;
    private String llave;
    private String nombre;
    private String descripcion;
    private String valor;
    private String estado;
    private Timestamp fechaCreacion;
    private String usuarioCreacion;
    private Timestamp fechaModificacion;
    private String usuarioModificacion;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private String attribute6;
    private String attribute7;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CATALOGOS")
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "grupo", nullable = false, length = 150)
    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    @Basic
    @Column(name = "llave", nullable = false, length = 150)
    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    @Basic
    @Column(name = "valor", nullable = false, length = 150)
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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
    @Column(name = "descripcion", nullable = false, length = 250)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "estado", nullable = false, length = 10)
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    @Basic
    @Column(name = "attribute1", nullable = true, length = 150)
    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    @Basic
    @Column(name = "attribute2", nullable = true, length = 150)
    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    @Basic
    @Column(name = "attribute3", nullable = true, length = 150)
    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    @Basic
    @Column(name = "attribute4", nullable = true, length = 150)
    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    @Basic
    @Column(name = "attribute5", nullable = true, length = 150)
    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    @Basic
    @Column(name = "attribute6", nullable = true, length = 150)
    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    @Basic
    @Column(name = "attribute7", nullable = true, length = 150)
    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatalogosEntity that = (CatalogosEntity) o;
        return id == that.id &&
                Objects.equals(grupo, that.grupo) &&
                Objects.equals(llave, that.llave) &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(estado, that.estado) &&
                Objects.equals(fechaCreacion, that.fechaCreacion) &&
                Objects.equals(usuarioCreacion, that.usuarioCreacion) &&
                Objects.equals(fechaModificacion, that.fechaModificacion) &&
                Objects.equals(usuarioModificacion, that.usuarioModificacion) &&
                Objects.equals(attribute1, that.attribute1) &&
                Objects.equals(attribute2, that.attribute2) &&
                Objects.equals(attribute3, that.attribute3) &&
                Objects.equals(attribute4, that.attribute4) &&
                Objects.equals(attribute5, that.attribute5) &&
                Objects.equals(attribute6, that.attribute6) &&
                Objects.equals(attribute7, that.attribute7);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grupo, llave, nombre, descripcion, estado, fechaCreacion, usuarioCreacion, fechaModificacion, usuarioModificacion, attribute1, attribute2, attribute3, attribute4, attribute5, attribute6, attribute7);
    }
}
