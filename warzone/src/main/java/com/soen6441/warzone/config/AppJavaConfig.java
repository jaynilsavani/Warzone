package com.soen6441.warzone.config;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Configuration of JavaFx with springBoot
 *
 * @author gaurang
 */
@Configuration
public class AppJavaConfig {

    /**
     * To initialize Object of SpringFXMLLoader
     */
    @Autowired
    SpringFXMLLoader d_springFXMLLoader;

    /**
     * It'll take data from bundle properties
     *
     * @return bundle data properties
     */
    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("Bundle");
    }

    /**
     * Stage only created after Spring context bootstrap
     *
     * @param p_stage Given FXML scene
     * @return object of stageManager
     * @throws IOException throws input/output exception
     */
    @Bean
    @Lazy(value = true)
    public StageManager stageManager(Stage p_stage) throws IOException {
        return new StageManager(d_springFXMLLoader, p_stage);
    }

}
