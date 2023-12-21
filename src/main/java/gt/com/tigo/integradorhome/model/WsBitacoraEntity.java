package gt.com.tigo.integradorhome.model;

import gt.com.tigo.integradorhome.util.AbstractEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "WS_BITACORA", schema = "", catalog = "")
public class WsBitacoraEntity extends AbstractEntity {
    private Long idBitacora;
    private String webService;
    private Timestamp requestDate;
    private String observations;
    private String request;
    private String response;
    private String responseType;
    private String summaryError;
    private String status;
    private String woNumber;

    @Id
    @Basic
    @Column(name = "ID_BITACORA", nullable = false, precision = 0)
    public Long getIdBitacora() {
        return idBitacora;
    }

    public void setIdBitacora(Long idBitacora) {
        this.idBitacora = idBitacora;
    }

    @Basic
    @Column(name = "WEB_SERVICE", nullable = true, length = 100)
    public String getWebService() {
        return webService;
    }

    public void setWebService(String webService) {
        this.webService = webService;
    }

    @Basic
    @Column(name = "REQUEST_DATE", nullable = true)
    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    @Basic
    @Column(name = "OBSERVATIONS", nullable = true, length = 500)
    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Basic
    @Column(name = "REQUEST", nullable = true)
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Basic
    @Column(name = "RESPONSE", nullable = true)
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Basic
    @Column(name = "RESPONSE_TYPE", nullable = true, length = 20)
    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    @Basic
    @Column(name = "SUMMARY_ERROR", nullable = true, length = 300)
    public String getSummaryError() {
        return summaryError;
    }

    public void setSummaryError(String summaryError) {
        this.summaryError = summaryError;
    }

    @Basic
    @Column(name = "STATUS", nullable = true, length = 10)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "WO_NUMBER", nullable = true, length = 50)
    public String getWoNumber() {
        return woNumber;
    }

    public void setWoNumber(String woNumber) {
        this.woNumber = woNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WsBitacoraEntity that = (WsBitacoraEntity) o;
        return Objects.equals(idBitacora, that.idBitacora) && Objects.equals(webService, that.webService) && Objects.equals(requestDate, that.requestDate) && Objects.equals(observations, that.observations) && Objects.equals(request, that.request) && Objects.equals(response, that.response) && Objects.equals(responseType, that.responseType) && Objects.equals(summaryError, that.summaryError) && Objects.equals(status, that.status) && Objects.equals(woNumber, that.woNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBitacora, webService, requestDate, observations, request, response, responseType, summaryError, status, woNumber);
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
