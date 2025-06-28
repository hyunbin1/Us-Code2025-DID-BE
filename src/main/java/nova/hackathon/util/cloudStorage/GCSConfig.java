package nova.hackathon.util.cloudStorage;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Objects;

@Configuration
public class GCSConfig {

    @Bean
    public Storage googleCloudStorage() throws IOException {
        return StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(
                        Objects.requireNonNull(getClass().getResourceAsStream("/gcp-key.json"))
                ))
                .setProjectId("dynamic-heading-464104-n8")
                .build()
                .getService();
    }
}
