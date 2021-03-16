package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.view.FxmlView;
import javafx.scene.Parent;

/**
 *
 * This Class is used for start up phase in state pattern to execute
 * load map, add and remove players and populate countries between
 * those players commands before the actual game start.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class StartUpPhase extends GamePlay {

    /**
     *  This is a constructor which is used to invoke GamePlay Constructor
     * @param p_gameEngine Object of GameEngine
     */
    public StartUpPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc }
     *
     */
    @Override
    public Parent execute() {
        return d_gameEngine.getStageManager().loadViewNodeHierarchy(FxmlView.GAMECONFIG.getFxmlFile(), d_gameEngine, "");
    }

    /**
     * {@inheritDoc }
     *
     */
    @Override
    public void next(Object p_nextObject) {
        IssueOrderPhase l_isueOrderPhase = new IssueOrderPhase(d_gameEngine);
        l_isueOrderPhase.d_gameData = (GameData) p_nextObject;
        d_gameEngine.setPhase(l_isueOrderPhase);
        d_gameEngine.getStageManager().switchScene(FxmlView.GAMEENGINE, null, "");

    }

    /**
     * {@inheritDoc }
     *
     */
    @Override
    public void executeOrder() {
        this.printInvalidCommandMessage();
    }

}
