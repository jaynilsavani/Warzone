package com.soen6441.warzone.service;

import com.soen6441.warzone.model.GamePlay;

/**
 *
 * This interface is used for Function for Game playing and
 * GameEngineServiceImpl is the implementation of it.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public interface GameEngineService {

    /**
     * This method used to apply assign Reinforcement on provided GamePlay Object
     *
     * @param p_gamePlay Current Game Play object on which assign Reinforcement
     * need to apply
     * @return updated Gameplay Which has assigned reinforcement army to
     * countries
     */
    public GamePlay assignReinforcements(GamePlay p_gamePlay);

}
