package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.CommandResponse;
import java.util.List;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * This Class is used for
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
    public GameEngine ge;

    public Phase(GameEngine p_ge) {
        ge = p_ge;
    }

    abstract public void executeOrder(Object p_gameData);


    abstract public void next(Object p_nextObject);

    abstract public Parent execute();

    /**
     * Common method to all States.
     */
    public void printInvalidCommandMessage() {
        System.out.println("Invalid command in state " + this.getClass().getSimpleName());
    }
}
