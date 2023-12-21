package gt.com.tigo.integradorhome.model;

import gt.com.tigo.integradorhome.util.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "WO_CONTROL_STATUS", schema = "", catalog = "")
public class WoControlStatusEntity extends AbstractEntity {
    private Long idStatus;
    private String woIdTivoli;
    private String woTivoliStatus;
    private String woTivoliStatusFecha;
    private String woIdFs;
    private String woFsStatus;
    private String woFsStatusFecha;
    private Integer finalizada;
    private Timestamp fechaFinalizada;
    private String comentarios;
    private Timestamp fechaCreacion;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;

    @Id
    @Column(name = "ID_STATUS", nullable = false, precision = 0)
    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    @Basic
    @Column(name = "WO_ID_TIVOLI", nullable = true, length = 100)
    public String getWoIdTivoli() {
        return woIdTivoli;
    }

    public void setWoIdTivoli(String woIdTivoli) {
        this.woIdTivoli = woIdTivoli;
    }

    @Basic
    @Column(name = "WO_TIVOLI_STATUS", nullable = true, length = 50)
    public String getWoTivoliStatus() {
        return woTivoliStatus;
    }

    public void setWoTivoliStatus(String woTivoliStatus) {
        this.woTivoliStatus = woTivoliStatus;
    }

    @Basic
    @Column(name = "WO_TIVOLI_STATUS_FECHA", nullable = true, length = 100)
    public String getWoTivoliStatusFecha() {
        return woTivoliStatusFecha;
    }

    public void setWoTivoliStatusFecha(String woTivoliStatusFecha) {
        this.woTivoliStatusFecha = woTivoliStatusFecha;
    }

    @Basic
    @Column(name = "WO_ID_FS", nullable = true, length = 100)
    public String getWoIdFs() {
        return woIdFs;
    }

    public void setWoIdFs(String woIdFs) {
        this.woIdFs = woIdFs;
    }

    @Basic
    @Column(name = "WO_FS_STATUS", nullable = true, length = 50)
    public String getWoFsStatus() {
        return woFsStatus;
    }

    public void setWoFsStatus(String woFsStatus) {
        this.woFsStatus = woFsStatus;
    }

    @Basic
    @Column(name = "WO_FS_STATUS_FECHA", nullable = true, length = 100)
    public String getWoFsStatusFecha() {
        return woFsStatusFecha;
    }

    public void setWoFsStatusFecha(String woFsStatusFecha) {
        this.woFsStatusFecha = woFsStatusFecha;
    }

    @Basic
    @Column(name = "FINALIZADA", nullable = true, precision = 0)
    public Integer getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(Integer finalizada) {
        this.finalizada = finalizada;
    }

    @Basic
    @Column(name = "FECHA_FINALIZADA", nullable = true)
    public Timestamp getFechaFinalizada() {
        return fechaFinalizada;
    }

    public void setFechaFinalizada(Timestamp fechaFinalizada) {
        this.fechaFinalizada = fechaFinalizada;
    }

    @Basic
    @Column(name = "COMENTARIOS", nullable = true, length = 500)
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

    @Override
    public void setUsuarioCreacion(String usuarioCreacion) {
        // no-op
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
    @Column(name = "ATTRIBUTE4", nullable = true, length = 150)
    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WoControlStatusEntity that = (WoControlStatusEntity) o;
        return Objects.equals(idStatus, that.idStatus) && Objects.equals(woIdTivoli, that.woIdTivoli) && Objects.equals(woTivoliStatus, that.woTivoliStatus) && Objects.equals(woTivoliStatusFecha, that.woTivoliStatusFecha) && Objects.equals(woIdFs, that.woIdFs) && Objects.equals(woFsStatus, that.woFsStatus) && Objects.equals(woFsStatusFecha, that.woFsStatusFecha) && Objects.equals(finalizada, that.finalizada) && Objects.equals(fechaFinalizada, that.fechaFinalizada) && Objects.equals(comentarios, that.comentarios) && Objects.equals(fechaCreacion, that.fechaCreacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idStatus, woIdTivoli, woTivoliStatus, woTivoliStatusFecha, woIdFs, woFsStatus, woFsStatusFecha, finalizada, fechaFinalizada, comentarios, fechaCreacion);
    }
}
