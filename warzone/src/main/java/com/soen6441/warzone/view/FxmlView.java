package com.soen6441.warzone.view;


/**
 * This is views of application
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
import java.util.ResourceBundle;

public enum FxmlView {

    HOME {
        @Override
        public String getSceneTitle() {
            return getTitleFromResourceBundle("home");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Home.fxml";

        }
    },
    MAPMANAGER {
        @Override
        public String getSceneTitle() {
            return getTitleFromResourceBundle("mapmanager");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/MapManager.fxml";
        }
    },
    GAMECONFIG {

        @Override
        public String getSceneTitle() {
            return getTitleFromResourceBundle("gameconfig");
        }

        @Override
        public String getFxmlFile() {

            return "/fxml/GameConfig.fxml";
        }

    },
    GAMEENGINE {

        @Override
        public String getSceneTitle() {

            return getTitleFromResourceBundle("gameengine");
        }

        @Override
        public String getFxmlFile() {

            return "/fxml/GameEngine.fxml";
        }

    };

    /**
     * This is used for retrieving scene title
     *
     * @return title of the scene
     */
    public abstract String getSceneTitle();

    /**
     * This is used for retrieving fxml file
     *
     * @return fxml file path
     */
    public abstract String getFxmlFile();

    /**
     *
     * This is used for extracting title from bundle resource
     * @param p_key : key to extract title
     * @return title  
     */
    public String getTitleFromResourceBundle(String p_key) {
        if (!p_key.isEmpty()) {
            p_key = p_key.concat(".title");
        }
        return ResourceBundle.getBundle("Bundle").getString(p_key);
    }

}
