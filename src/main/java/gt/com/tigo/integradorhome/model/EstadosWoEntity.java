package gt.com.tigo.integradorhome.model;

import gt.com.tigo.integradorhome.util.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "ESTADOS_WO", schema = "", catalog = "")
@SequenceGenerator(name="SEQ_ESTADOS_WO", sequenceName = "SEQ_ESTADOS_WO", initialValue=1, allocationSize=1)
public class EstadosWoEntity extends AbstractEntity {
    private Long id;
    private String ot;
    private String destino;
    private String estado;
    private String comentarios;
    private Timestamp fechaCreacion;
    private String usuarioCreacion;
    private String attribute1;
    private String attribute2;
    private String attribute3;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ESTADOS_WO")
    @Column(name = "ID", nullable = true, precision = 0)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "OT", nullable = false, length = 150)
    public String getOt() {
        return ot;
    }

    public void setOt(String ot) {
        this.ot = ot;
    }

    @Basic
    @Column(name = "DESTINO", nullable = false, length = 150)
    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    @Basic
    @Column(name = "ESTADO", nullable = false, length = 150)
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Basic
    @Column(name = "COMENTARIOS", nullable = false, length = 1000)
    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
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

    @Override
    public void setFechaModificacion(Timestamp fechaModificacion) {
        // no-op
    }

    @Override
    public void setUsuarioModificacion(String usuarioModificacion) {
        // no-op
    }

    @Basic
    @Column(name = "ATTRIBUTE1", nullable = false, length = 250)
    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    @Basic
    @Column(name = "ATTRIBUTE2", nullable = false, length = 250)
    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    @Basic
    @Column(name = "ATTRIBUTE3", nullable = false, length = 250)
    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadosWoEntity that = (EstadosWoEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(ot, that.ot) && Objects.equals(destino, that.destino) && Objects.equals(estado, that.estado) && Objects.equals(comentarios, that.comentarios) && Objects.equals(fechaCreacion, that.fechaCreacion) && Objects.equals(usuarioCreacion, that.usuarioCreacion) && Objects.equals(attribute1, that.attribute1) && Objects.equals(attribute2, that.attribute2) && Objects.equals(attribute3, that.attribute3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ot, destino, estado, comentarios, fechaCreacion, usuarioCreacion, attribute1, attribute2, attribute3);
    }
}
