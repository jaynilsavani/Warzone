package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Parent;

/**
 * This abstract class is used as State Class in State Pattern and defines the
 * behavior that is common to all the states in its group (StartUpPhase,
 * IssueOrderPhase and ExecuteOrderPhase). All the states in its group need to
 * extend this class.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public abstract class GamePlay extends Phase {

    /**
     * GameData object which store information like player list,name of game
     * phase, name of map file
     */
    public GameData d_gameData;
    /**
     * It'll manage list of command response in that particular phase.
     */
    public List<CommandResponse> d_commandResponses = new ArrayList<>();
    public CommandResponse d_issueResponse;

    /**
     * This parameterized constructor is used to invoke Phase Constructor and
     * set the reference variable to GameEngine object for the state transition
     *
     * @param p_gameEngine Object of GameEngine
     */
    public GamePlay(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    abstract public Parent execute();
}
