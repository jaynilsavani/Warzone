package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.view.FxmlView;
import javafx.scene.Parent;

/**
 *
 * This Class is used for
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class MapPhase extends Phase {


    public MapPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    @Override
    public Parent execute() {
        return d_gameEngine.getStageManager().loadViewNodeHierarchy(FxmlView.MAPMANAGER.getFxmlFile(), null, "");
    }

    @Override
    public void next(Object p_nextObject) {
        this.printInvalidCommandMessage();
    }

    @Override
    public void executeOrder() {
        this.printInvalidCommandMessage();
    }

}
