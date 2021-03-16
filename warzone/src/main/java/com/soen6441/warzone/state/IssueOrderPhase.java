package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import javafx.scene.Parent;

/**
 *
 * This Class is used for
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class IssueOrderPhase extends GamePlay {


    public IssueOrderPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    @Override
    public Parent execute() {
        this.printInvalidCommandMessage();
        return null;
    }

    @Override
    public void next(Object p_nextObject) {
        ExecuteOrderPhase executeOrderPhase = new ExecuteOrderPhase(d_gameEngine);
        executeOrderPhase.d_gameData = this.d_gameData;
        d_gameEngine.setPhase(executeOrderPhase);
        d_gameEngine.getPhase().executeOrder();
    }

    @Override
    public void executeOrder() {
        this.printInvalidCommandMessage();
    }

}
