package com.soen6441.warzone;

import com.soen6441.warzone.view.FxmlView;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.soen6441.warzone.config.StageManager;

/**
 * This is the main entry point for WarzonegameApplication and it extends
 * Application (Which is JavaFx class), as we want it to load through JavaFx.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@SpringBootApplication
public class WarzoneApplication extends Application {

    /**
     * This is Configurable Springboot application context
     */
    protected ConfigurableApplicationContext d_springContext;
    /**
     *
     * This is used to switch to welcome screen on startup
     */
    protected StageManager d_stageManager;

    /**
     * This the main method and the entry point for the class
     *
     * @param args : command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * To assign main method to springBootApplicationContext
     *
     * @throws java.lang.Exception indicates this function may throw exception
     * @see javafx.application.Application#init()
     */
    @Override
    public void init() throws Exception {
        d_springContext = springBootApplicationContext();
    }

    /**
     * Initialization of d_stageManager
     *
     * @param p_stage JavaFx view page
     * @throws java.lang.Exception indicates this function may throw exception
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage p_stage) throws Exception {
        d_stageManager = d_springContext.getBean(StageManager.class, p_stage);
        displayInitialScene();
    }

    /**
     * This method will close d_springContext
     *
     * @see javafx.application.Application#stop()
     */
    @Override
    public void stop() throws Exception {
        d_springContext.close();
    }

    /**
     * Useful to override this method by sub-classes wishing to change the first
     * Scene to be displayed on startup. Example: Functional tests on main
     * window.
     */
    protected void displayInitialScene() {
        d_stageManager.switchScene(FxmlView.HOME, null,"");
    }

    /**
     * @return l_builder.run(args); It'll will return context of application
     * ConfigurableApplicationContext of springBootApplicationContext
     */
    private ConfigurableApplicationContext springBootApplicationContext() {
        SpringApplicationBuilder l_builder = new SpringApplicationBuilder(WarzoneApplication.class);
        String[] l_args = getParameters().getRaw().stream().toArray(String[]::new);
        return l_builder.run(l_args);
    }

}
