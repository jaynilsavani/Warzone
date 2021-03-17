package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.view.FxmlView;
import javafx.scene.Parent;

/**
 *
 * This Class is used for
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class StartUpPhase extends GamePlay {

    public StartUpPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    @Override
    public Parent execute() {
        return d_gameEngine.getStageManager().loadViewNodeHierarchy(FxmlView.GAMECONFIG.getFxmlFile(), d_gameEngine, "");
    }

    @Override
    public void next(Object p_nextObject) {
        IssueOrderPhase l_isueOrderPhase = new IssueOrderPhase(d_gameEngine);
        l_isueOrderPhase.d_gameData = (GameData) p_nextObject;
        d_gameEngine.setPhase(l_isueOrderPhase);
        d_gameEngine.getStageManager().switchScene(FxmlView.GAMEENGINE, null, "");

    }

    @Override
    public void executeOrder() {
        this.printInvalidCommandMessage();
    }

    @Override
    public void issueOrder(String p_command) {
        this.printInvalidCommandMessage();
    }

    @Override
    public void assignReinforcements() {
        this.printInvalidCommandMessage();
    }


}
