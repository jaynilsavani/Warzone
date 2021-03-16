package com.soen6441.warzone.state;

import com.soen6441.warzone.controller.GameEngine;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GameData;
import com.soen6441.warzone.view.FxmlView;
import java.util.List;
import javafx.scene.Parent;

/**
 *
 * This Class is used for
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public class StartUpPhase extends GamePlay {

    GameEngine d_gameEngine;

    public StartUpPhase(GameEngine p_ge) {
        super(p_ge);
    }

    @Override
    public Parent execute() {
        return ge.getStageManager().loadViewNodeHierarchy(FxmlView.GAMECONFIG.getFxmlFile(), ge, "");
    }

    @Override
    public void next(Object p_nextObject) {
        IssueOrderPhase sueOrderPhase = new IssueOrderPhase(ge);
        sueOrderPhase.d_gameData=(GameData) p_nextObject;
        ge.setPhase(sueOrderPhase);
        ge.getStageManager().switchScene(FxmlView.GAMEX, ge, "");

    }

    @Override
    public void executeOrder(Object p_gameData) {

    }

}
