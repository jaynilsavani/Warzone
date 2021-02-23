package com.soen6441.warzone.config;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * This is used for loading FXML File
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Component
public class SpringFXMLLoader {

    /**
     * This is used to get resources from resource Bundle file
     */
    private final ResourceBundle d_resourceBundle;
    /**
     * Application Context
     */
    private final ApplicationContext d_context;

    /**
     * This is a constructor of this class and it initialize context and
     * resourceBundle
     *
     * @param p_context is a spring context
     * @param p_resourceBundle is properties file object
     */
    @Autowired
    public SpringFXMLLoader(ApplicationContext p_context, ResourceBundle p_resourceBundle) {
        this.d_resourceBundle = p_resourceBundle;
        this.d_context = p_context;
    }

    /**
     * This method will load FXML file
     *
     * @param p_fxmlPath is path of fxml file located
     * @return FXML loader
     * @throws IOException indicates input/output Exception
     */
    public FXMLLoader load(String p_fxmlPath) throws IOException {
        FXMLLoader l_loader = new FXMLLoader();
        l_loader.setControllerFactory(d_context::getBean);
        l_loader.setResources(d_resourceBundle);
        l_loader.setLocation(getClass().getResource(p_fxmlPath));
        return l_loader;
    }
}
