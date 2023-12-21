package gt.com.tigo.integradorhome.model;

import gt.com.tigo.integradorhome.util.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "V_BITACORA_SISTEMA", schema = "", catalog = "")
public class VBitacoraSistemaEntity extends AbstractEntity {
    private Long logId;
    private Timestamp logTimestamp;
    private String errorMessage;
    private String sourceType;
    private String sourceName;

    @Id
    @Basic
    @Column(name = "LOG_ID", nullable = true, precision = 0)
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    @Basic
    @Column(name = "LOG_TIMESTAMP", nullable = true)
    public Timestamp getLogTimestamp() {
        return logTimestamp;
    }

    public void setLogTimestamp(Timestamp logTimestamp) {
        this.logTimestamp = logTimestamp;
    }

    @Basic
    @Column(name = "ERROR_MESSAGE", nullable = true, length = 4000)
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Basic
    @Column(name = "SOURCE_TYPE", nullable = true, length = 150)
    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    @Basic
    @Column(name = "SOURCE_NAME", nullable = true, length = 200)
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VBitacoraSistemaEntity that = (VBitacoraSistemaEntity) o;
        return Objects.equals(logId, that.logId) && Objects.equals(logTimestamp, that.logTimestamp) && Objects.equals(errorMessage, that.errorMessage) && Objects.equals(sourceType, that.sourceType) && Objects.equals(sourceName, that.sourceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logId, logTimestamp, errorMessage, sourceType, sourceName);
    }

    @Override
    public void setFechaCreacion(Timestamp fechaCreacion) {
        // no-op
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
}
