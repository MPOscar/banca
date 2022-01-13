package banca.uy.core.utils;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;

public class DespliegueConfiguration {

    @DefaultValue("true")
    @Value("${despliegue.endpoints.frontend}")
    private Boolean apiFrontend;

    @DefaultValue("true")
    @Value("${despliegue.endpoints.terceros}")
    private Boolean apiTerceros;

    @NotEmpty
    @DefaultValue("*")
    @Value("${despliegue.cors}")
    private String cors;

    @NotEmpty
    @DefaultValue("rondanet.recursos.desarrollo")
    @Value("${despliegue.bucket}")
    private String bucket;

    @NotNull
    private S3Config s3 = new S3Config();

    public DespliegueConfiguration() {
    }

    public DespliegueConfiguration(final Boolean apiFrontend, final Boolean apiTerceros, final String cors) {
        this.apiFrontend = apiFrontend;
        this.apiTerceros = apiTerceros;
        this.cors = cors;
    }

    /**
     * Define si se van a desplegar las APIs para el frontend o no.
     * 
     * @return Valor Booleano. Verdadero si se debe publicar la API para frontend
     */
    @JsonProperty("endpoints.frontend")
    public Boolean isApiFrontend() {
        return this.apiFrontend;
    }

    /**
     * Define si se van a desplegar las APIs para el frontend o no.
     * 
     * @return Valor Booleano. Verdadero si se debe publicar la API para frontend
     */
    @JsonProperty("endpoints.frontend")
    public Boolean getApiFrontend() {
        return this.apiFrontend;
    }

    /**
     * Define si se van a desplegar las APIs para frontend o no.
     * 
     * @return Valor Booleano
     */
    @JsonProperty("endpoints.frontend")
    public void setApiFrontend(final Boolean apiFrontend) {
        this.apiFrontend = apiFrontend;
    }

    /**
     * Define si se van a desplegar las APIs para terceros o no.
     * 
     * @return Valor Booleano. Verdadero si se debe publicar la API para terceros
     */
    @JsonProperty("endpoints.terceros")
    public Boolean isApiTerceros() {
        return this.apiTerceros;
    }

    /**
     * Define si se van a desplegar las APIs para terceros o no.
     * 
     * @return Valor Booleano. Verdadero si se debe publicar la API para terceros
     */
    @JsonProperty("endpoints.terceros")
    public Boolean getApiTerceros() {
        return this.apiTerceros;
    }

    /**
     * Define si se van a desplegar las APIs para terceros o no.
     * 
     * @return Valor Booleano
     */
    @JsonProperty("endpoints.terceros")
    public void setApiTerceros(final Boolean apiTerceros) {
        this.apiTerceros = apiTerceros;
    }

    public String getCors() {
        return this.cors;
    }

    public void setCors(final String cors) {
        this.cors = cors;
    }

    public DespliegueConfiguration apiFrontend(final Boolean apiFrontend) {
        this.apiFrontend = apiFrontend;
        return this;
    }

    public DespliegueConfiguration apiTerceros(final Boolean apiTerceros) {
        this.apiTerceros = apiTerceros;
        return this;
    }

    public DespliegueConfiguration cors(final String cors) {
        this.cors = cors;
        return this;
    }

    @DefaultValue("rondanet.com")
    @Value("${despliegue.bucket}")
    @JsonProperty("bucket")
    public String getBucket() {
        return this.bucket = "rondanet";
    }

    @JsonProperty("bucket")
    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }

    @JsonProperty("bucket")
    public DespliegueConfiguration bucket(final String bucket) {
        this.bucket = bucket;
        return this;
    }

    public S3Config getS3() {
        return this.s3;
    }

    public void setS3(final S3Config s3) {
        this.s3 = s3;
    }

}