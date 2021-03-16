package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * This Class is used as State Class in State Pattern
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public abstract class Phase {

    /**
     * Contains a reference to the State of the GameEngine so that the state
     * object can change the state of the GameEngine to transition between
     * states.
     */
    @Autowired
    public GameEngine d_gameEngine;

    /**
     * Constructor to set the reference variable to GameEngine object for the
     * state transition
     * @param p_gameEngine This is the reference variable to set state.
     */
    public Phase(GameEngine p_gameEngine) {
        d_gameEngine = p_gameEngine;
    }

    /**
     * This is used to to initialize the Current Phase
     *
     * @return Root object of current Phase screen
     */
    abstract public Parent execute();

    /**
     * This is used to switch to next phase
     *
     * @param p_nextObject Object that is being passed to next phase
     */
    abstract public void next(Object p_nextObject);

    /**
     * This is used for Execution of order in Execute Order Phase
     */
    abstract public void executeOrder();

    /**
     * Common method to all Phases.
     */
    public void printInvalidCommandMessage() {
        System.out.println("Invalid command in state " + this.getClass().getSimpleName());
    }
}
