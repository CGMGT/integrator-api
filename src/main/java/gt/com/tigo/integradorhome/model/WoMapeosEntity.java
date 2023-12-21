package gt.com.tigo.integradorhome.model;

import gt.com.tigo.integradorhome.util.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "WO_MAPEOS", schema = "", catalog = "")
@SequenceGenerator(name="SEQ_WO_MAPEOS", sequenceName = "SEQ_WO_MAPEOS", initialValue=1, allocationSize=1)
public class WoMapeosEntity extends AbstractEntity {
    private Long idMapeo;
    private String tipo;
    private Integer idOrigen;
    private Integer idDestino;
    private String sistemaOrigen;
    private String sistemaDestino;
    private String valorOrigen;
    private String valorDestino;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String estado;
    private Timestamp fechaCreacion;
    private String usuarioCreacion;
    private Timestamp fechaModificacion;
    private String usuarioModificacion;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WO_MAPEOS")
    @Column(name = "ID_MAPEO", nullable = false, precision = 0)
    public Long getIdMapeo() {
        return idMapeo;
    }

    public void setIdMapeo(Long idMapeo) {
        this.idMapeo = idMapeo;
    }

    @Basic
    @Column(name = "TIPO", nullable = false, length = 50)
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Basic
    @Column(name = "ID_ORIGEN", nullable = false, precision = 0)
    public Integer getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(Integer idOrigen) {
        this.idOrigen = idOrigen;
    }

    @Basic
    @Column(name = "ID_DESTINO", nullable = false, precision = 0)
    public Integer getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(Integer idDestino) {
        this.idDestino = idDestino;
    }

    @Basic
    @Column(name = "SISTEMA_ORIGEN", nullable = false, length = 20)
    public String getSistemaOrigen() {
        return sistemaOrigen;
    }

    public void setSistemaOrigen(String sistemaOrigen) {
        this.sistemaOrigen = sistemaOrigen;
    }

    @Basic
    @Column(name = "SISTEMA_DESTINO", nullable = false, length = 20)
    public String getSistemaDestino() {
        return sistemaDestino;
    }

    public void setSistemaDestino(String sistemaDestino) {
        this.sistemaDestino = sistemaDestino;
    }

    @Basic
    @Column(name = "VALOR_ORIGEN", nullable = false, length = 50)
    public String getValorOrigen() {
        return valorOrigen;
    }

    public void setValorOrigen(String valorOrigen) {
        this.valorOrigen = valorOrigen;
    }

    @Basic
    @Column(name = "VALOR_DESTINO", nullable = false, length = 50)
    public String getValorDestino() {
        return valorDestino;
    }

    public void setValorDestino(String valorDestino) {
        this.valorDestino = valorDestino;
    }

    @Basic
    @Column(name = "ATTRIBUTE1", nullable = true, length = 150)
    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    @Basic
    @Column(name = "ATTRIBUTE2", nullable = true, length = 150)
    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    @Basic
    @Column(name = "ATTRIBUTE3", nullable = true, length = 150)
    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    @Basic
    @Column(name = "ESTADO", nullable = true, length = 10)
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
    @Column(name = "USUARIO_CREACION", nullable = true, length = 50)
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
    @Column(name = "USUARIO_MODIFICACION", nullable = true, length = 50)
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
        WoMapeosEntity that = (WoMapeosEntity) o;
        return Objects.equals(idMapeo, that.idMapeo) && Objects.equals(tipo, that.tipo) && Objects.equals(idOrigen, that.idOrigen) && Objects.equals(idDestino, that.idDestino) && Objects.equals(sistemaOrigen, that.sistemaOrigen) && Objects.equals(sistemaDestino, that.sistemaDestino) && Objects.equals(valorOrigen, that.valorOrigen) && Objects.equals(valorDestino, that.valorDestino) && Objects.equals(attribute1, that.attribute1) && Objects.equals(attribute2, that.attribute2) && Objects.equals(attribute3, that.attribute3) && Objects.equals(estado, that.estado) && Objects.equals(fechaCreacion, that.fechaCreacion) && Objects.equals(usuarioCreacion, that.usuarioCreacion) && Objects.equals(fechaModificacion, that.fechaModificacion) && Objects.equals(usuarioModificacion, that.usuarioModificacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMapeo, tipo, idOrigen, idDestino, sistemaOrigen, sistemaDestino, valorOrigen, valorDestino, attribute1, attribute2, attribute3, estado, fechaCreacion, usuarioCreacion, fechaModificacion, usuarioModificacion);
    }
}
