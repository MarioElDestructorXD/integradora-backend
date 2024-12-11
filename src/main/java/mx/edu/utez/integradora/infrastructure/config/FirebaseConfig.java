package mx.edu.utez.integradora.infrastructure.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            // Carga el archivo directamente desde el classpath
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");

            if (serviceAccount == null) {
                throw new FileNotFoundException("No se encontró el archivo serviceAccountKey.json en el classpath");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://fixy-pro-c83ab-default-rtdb.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
