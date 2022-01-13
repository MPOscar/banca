package banca.uy.core.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import banca.uy.core.utils.interfaces.IS3FileManager;
import banca.uy.core.db.ParamsDAO;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import banca.uy.BancaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DefaultValue;

@Component
public class S3FileManager implements IS3FileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3FileManager.class);

    private String bucketName;

    @DefaultValue("")
    @Value("${despliegue.s3.s3Id}")
    private String keyId = "AKIAI34DEMSGZSIIC4SA";

    @DefaultValue("")
    @Value("${despliegue.s3.s3Key}")
    private String key = "IBVIXtM706zUPFqrgY5YRadvU59IRHpkVXXPTAGq";

    private AWSCredentials credentials;
    @SuppressWarnings("deprecation")
    private AmazonS3 s3Client;
    @Autowired
    private ParamsDAO paramsDAO;
    private BancaConfiguration configuration;

    public S3FileManager(ParamsDAO paramsDAO, BancaConfiguration configuration) {
        this.configuration = configuration;
        // this.paramsDAO = paramsDAO;
        this.bucketName = this.configuration.getConfiguracionDespliegue().getBucket();
        this.key = this.configuration.getConfiguracionDespliegue().getS3().getS3Key();
        this.keyId = this.configuration.getConfiguracionDespliegue().getS3().getS3Id();
        this.credentials = new BasicAWSCredentials(keyId, key);
        this.s3Client = new AmazonS3Client(credentials);

    }

    @Override
    public InputStream getFile(String url) {
        try {
            S3Object object = this.s3Client.getObject(bucketName, url);
            InputStream file = object.getObjectContent();
            return file;
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadFile(String contentType, ByteArrayInputStream byteArray, long contentLength, String url) {
        try {
            this.s3Client.deleteObject(new DeleteObjectRequest(bucketName, url));

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(contentType);
            objectMetaData.setContentLength(contentLength);
            s3Client.putObject(new PutObjectRequest(bucketName, url, byteArray, objectMetaData)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            // TODO
        } catch (SdkClientException e) {
            // TODO
            e.printStackTrace();
        }
        return bucketName + "/" + url;

    }

}