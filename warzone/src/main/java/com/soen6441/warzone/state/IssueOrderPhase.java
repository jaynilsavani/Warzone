package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.view.FxmlView;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Parent;

/**
 *
 * This Class is used for
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class IssueOrderPhase extends GamePlay {

    GameEngine d_gameEngine;

    public IssueOrderPhase(GameEngine p_ge) {
        super(p_ge);
    }

    @Override
    public Parent execute() {
        return null;
    }

    @Override
    public void next(Object p_nextObject) {
        ExecuteOrderPhase executeOrderPhase = new ExecuteOrderPhase(ge);
        executeOrderPhase.d_gameData = this.d_gameData;
        ge.setPhase(executeOrderPhase);
        ge.getPhase().executeOrder(p_nextObject);
    }

    @Override
    public void executeOrder(Object p_gameData) {
       
    }

}
