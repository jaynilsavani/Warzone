package com.soen6441.warzone.config;

import com.soen6441.warzone.controller.GameConfigController;
import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.controller.GameX;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.view.FxmlView;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is used for managing different stages of application
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class StageManager {

    /**
     * This is used to set current Application view to user
     */
    private final Stage d_primaryStage;
    /**
     * This is used to load FXML View
     */
    private final SpringFXMLLoader d_springFXMLLoader;

    /**
     * This is a parameterize constructor of this class and it'll initialize
     * given values in arguments.
     *
     * @param p_springFXMLLoader will make object from XML file
     * @param p_stage stage to which user want to switch
     */
    public StageManager(SpringFXMLLoader p_springFXMLLoader, Stage p_stage) {
        this.d_springFXMLLoader = p_springFXMLLoader;
        this.d_primaryStage = p_stage;
    }

    /**
     * This method will help to switch the scene from one to another
     *
     * @param p_view is scene to show
     * @param p_object: object
     * @param p_msg : Phase Name
     */
    public void switchScene(final FxmlView p_view, Object p_object, String p_msg) {
        Parent l_viewRootNodeHierarchy = loadViewNodeHierarchy(p_view.getFxmlFile(), p_object, p_msg);
        show(l_viewRootNodeHierarchy, p_view.getSceneTitle());
    }

    /**
     * This method will show the view of the title given in arguments and set
     * the root node
     *
     * p_rootnode rootnode is a parent node of scene
     *
     * @param p_title is name of the view
     */
    private void show(final Parent p_rootnode, String p_title) {
        Scene l_scene = prepareScene(p_rootnode);

        d_primaryStage.setTitle(p_title);
        d_primaryStage.setScene(l_scene);
        d_primaryStage.sizeToScene();
        d_primaryStage.centerOnScreen();

        try {
            d_primaryStage.show();
        } catch (Exception exception) {
            logAndExit("Unable to show scene for title" + p_title, exception);
        }
    }

    /**
     * This method will prepare scene (view) before loading
     *
     * @param p_rootnode is parent Node of scene
     * @return prepared scene
     */
    private Scene prepareScene(Parent p_rootnode) {
        Scene l_scene = d_primaryStage.getScene();

        if (l_scene == null) {
            l_scene = new Scene(p_rootnode);
        }
        l_scene.setRoot(p_rootnode);
        return l_scene;
    }

    /**
     * Loads the object hierarchy from a FXML document and returns to root node
     * of that hierarchy.
     *
     * @return Parent root node of the FXML document hierarchy
     */
    public Parent loadViewNodeHierarchy(String p_fxmlFilePath, Object p_object, String p_phaseName) {
        Parent l_rootNode = null;
        try {
            FXMLLoader l_loader = d_springFXMLLoader.load(p_fxmlFilePath);
            if (p_fxmlFilePath.contains(FxmlView.GAMEENGINE.getFxmlFile())) {
                l_rootNode = l_loader.load();
                GameEngine l_gameEngine = l_loader.getController();
                return l_gameEngine.setSomeThing(p_phaseName);
            } else if (p_fxmlFilePath.contains(FxmlView.GAMECONFIG.getFxmlFile())) {
                l_rootNode = l_loader.load();
                GameConfigController l_gameConfig = l_loader.getController();
                l_gameConfig.setGameEngine((GameEngine) p_object);
            } else if (p_fxmlFilePath.contains(FxmlView.GAMEX.getFxmlFile())) {
                l_rootNode = l_loader.load();
                GameX l_gameConfig = l_loader.getController();
                l_gameConfig.setGamePlay((GameEngine) p_object);
            } else {
                l_rootNode = l_loader.load();
            }

        } catch (IOException exception) {
            logAndExit("Unable to load FXML view" + p_fxmlFilePath, exception);
        }
        return l_rootNode;
    }

    /**
     * This method will close the screen
     *
     * @param p_errorMsg
     * @param p_exception
     */
    private void logAndExit(String p_errorMsg, Exception p_exception) {
        Platform.exit();
    }

}
