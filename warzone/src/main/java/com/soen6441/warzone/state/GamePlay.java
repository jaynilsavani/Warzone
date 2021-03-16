package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Parent;

/**
 *
 * This Class is used for
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public abstract class GamePlay extends Phase {

    public GameData d_gameData;
    public List<CommandResponse> d_commandResponses = new ArrayList<>();

    public GamePlay(GameEngine p_gameEngine) {
        super(p_gameEngine);
    }

    @Override
    abstract public Parent execute();
}
