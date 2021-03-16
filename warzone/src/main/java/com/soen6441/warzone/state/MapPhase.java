package com.soen6441.warzone.state;

import com.soen6441.warzone.config.StageManager;
import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.view.FxmlView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

/**
 *
 * This Class is used for
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class MapPhase extends Phase implements Initializable {

    GameEngine ge;

    public MapPhase(GameEngine p_ge) {
        super(p_ge);
        ge = p_ge;
    }

    @Override
    public Parent execute() {
        return ge.getStageManager().loadViewNodeHierarchy(FxmlView.MAPMANAGER.getFxmlFile(), null, "");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void next(Object p_nextObject) {
    }

    @Override
    public void executeOrder(Object p_gameData) {
      
    }

}
