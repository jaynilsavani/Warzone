package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.view.FxmlView;
import javafx.scene.Parent;

/**
 * ConcreteState of the State pattern.This Phase is used for start up phase
 * commands i.e. load map, add and remove players and populate countries between
 * those players in state pattern before the actual game start.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class StartUpPhase extends GamePlay {

    /**
     * This parameterized constructor is used to invoke Phase Constructor and
     * set the reference variable to GameEngine object for the state transition.
     *
     * @param p_gameEngine Object of GameEngine
     */
    public StartUpPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * used for testing where fxml can not be used
     */
    public static int d_testPurpose=0;

    /**
     * {@inheritDoc }
     */
    @Override
    public Parent execute() {
        return d_gameEngine.getStageManager().loadViewNodeHierarchy(FxmlView.GAMECONFIG.getFxmlFile(), d_gameEngine, "");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void next(Object p_nextObject) {
        IssueOrderPhase l_isueOrderPhase = new IssueOrderPhase(d_gameEngine);
        l_isueOrderPhase.d_gameData = (GameData) p_nextObject;
        d_gameEngine.setPhase(l_isueOrderPhase);
        if(d_testPurpose==0) {
            d_gameEngine.getStageManager().switchScene(FxmlView.GAMEENGINE, null, "");
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void executeOrder() {
        this.printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void issueOrder(String p_command) {
        this.printInvalidCommandMessage();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void assignReinforcements() {
        this.printInvalidCommandMessage();
    }

}
