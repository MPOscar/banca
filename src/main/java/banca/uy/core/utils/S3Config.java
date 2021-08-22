package banca.uy.core.utils;

import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.DefaultValue;

public class S3Config {

    @DefaultValue("")
    @Value("${despliegue.s3.s3Id}")
    private String s3Id = "AKIAI34DEMSGZSIIC4SA";

    @DefaultValue("")
    @Value("${despliegue.s3.s3Key}")
    private String s3Key = "IBVIXtM706zUPFqrgY5YRadvU59IRHpkVXXPTAGq";

    public S3Config() {

    }

    public String getS3Id() {
        return s3Id;
    }

    public void setS3Id(String s3Id) {
        this.s3Id = s3Id;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public S3Config s3Key(String s3Key) {
        this.s3Key = s3Key;
        return this;
    }

    public S3Config s3Id(String s3Id) {
        this.s3Id = s3Id;
        return this;
    }

}