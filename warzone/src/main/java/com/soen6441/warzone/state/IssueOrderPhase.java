package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import javafx.scene.Parent;

/**
 *
 * ConcreteState of the State pattern.This Phase is used to take order from
 * each players in round robin manner.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class IssueOrderPhase extends GamePlay {

    /**
     * This parameterized constructor is used to invoke GameEngine Constructor and set the
     * reference variable to GameEngine object for the state transition
     * @param p_gameEngine Object of GameEngine
     *
     */
    public IssueOrderPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }
    /**
     * {@inheritDoc }
     *
     */
    @Override
    public Parent execute() {
        this.printInvalidCommandMessage();
        return null;
    }
    /**
     * {@inheritDoc }
     *
     */
    @Override
    public void next(Object p_nextObject) {
        ExecuteOrderPhase executeOrderPhase = new ExecuteOrderPhase(d_gameEngine);
        executeOrderPhase.d_gameData = this.d_gameData;
        d_gameEngine.setPhase(executeOrderPhase);
        d_gameEngine.getPhase().executeOrder();
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
