package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.view.FxmlView;
import javafx.scene.Parent;

/**
 *
 * ConcreteState of the State pattern.This Phase is used for Map Editing commands
 * i.e create, edit, validate, save and load map which are valid in this state.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class MapPhase extends Phase {

    /**
     *  This parameterized constructor is used to invoke Phase Constructor and
     *  set the reference variable to GameEngine object for the state transition
     *  @param p_gameEngine Object of GameEngine
     */
    public MapPhase(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc }
     *
     */
    @Override
    public Parent execute() {
        return d_gameEngine.getStageManager().loadViewNodeHierarchy(FxmlView.MAPMANAGER.getFxmlFile(), null, "");
    }

    /**
     * {@inheritDoc }
     *
     */
    @Override
    public void next(Object p_nextObject) {
        this.printInvalidCommandMessage();
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
