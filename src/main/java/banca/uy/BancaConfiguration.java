package banca.uy;

import java.lang.annotation.Annotation;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import banca.uy.core.utils.DespliegueConfiguration;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

@Configuration
public class BancaConfiguration implements Configuration {
    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Catálogo";

    @Valid
    @NotNull
    private DespliegueConfiguration configuracionDespliegue = new DespliegueConfiguration();

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }

    
    @JsonProperty("despliegue")
    public DespliegueConfiguration getConfiguracionDespliegue() {
        return this.configuracionDespliegue;
    }

    @JsonProperty("despliegue")
    public void setConfiguracionDespliegue(DespliegueConfiguration despliegue) {
        this.configuracionDespliegue = despliegue;
    }

    @Override
    public String value() {
        return null;
    }

    @Override
    public boolean proxyBeanMethods() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BancaConfiguration)) return false;
        BancaConfiguration that = (BancaConfiguration) o;
        return Objects.equals(getTemplate(), that.getTemplate()) &&
                Objects.equals(getDefaultName(), that.getDefaultName()) &&
                Objects.equals(getConfiguracionDespliegue(), that.getConfiguracionDespliegue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTemplate(), getDefaultName(), getConfiguracionDespliegue());
    }

    @Override
    public String toString() {
        return "CatalogoConfiguration{" +
                "template='" + template + '\'' +
                ", defaultName='" + defaultName + '\'' +
                ", configuracionDespliegue=" + configuracionDespliegue +
                '}';
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
