package com.soen6441.warzone.model;

import static com.soen6441.warzone.model.GameCard.AIRLIFT;
import static com.soen6441.warzone.model.GameCard.BLOCKADE;
import static com.soen6441.warzone.model.GameCard.BOMB;
import static com.soen6441.warzone.model.GameCard.DIPLOMACY;
import com.soen6441.warzone.strategy.AggressiveStrategy;
import com.soen6441.warzone.strategy.BenevolentStrategy;
import com.soen6441.warzone.strategy.CheaterStrategy;
import com.soen6441.warzone.strategy.HumanStrategy;
import com.soen6441.warzone.strategy.RandomStrategy;
import com.soen6441.warzone.strategy.Strategy;

/**
 * This Enum is used for All Types of strategies
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public enum Strategies {
    /**
     * enum variable of order type used to select Strategy
     */
    AGGRESSIVE, BENEVOLENT, CHEATER, HUMAN, RANDOM;

    /**
     * used to add the mapping of card to command
     *
     * @param p_strategy order from the players order list
     * @param p_gameData GameData of the Strategy
     * @return returns the gamecard object with the card related to the order
     */
    public static Strategy strategyToObjectMapper(Strategies p_strategy, GameData p_gameData) {
        Strategy l_strategy = null;
        switch (p_strategy) {
            case AGGRESSIVE:
                l_strategy = new AggressiveStrategy();
                break;
            case BENEVOLENT:
                l_strategy = new BenevolentStrategy();
                break;
            case CHEATER:
                l_strategy = new CheaterStrategy();
                break;
            case HUMAN:
                l_strategy = new HumanStrategy();
                break;
            case RANDOM:
                l_strategy = new RandomStrategy();
                break;
        }
        if (l_strategy != null) {
            l_strategy.setD_gameData(p_gameData);
        }
        return l_strategy;
    }

    /**
     * used to add the mapping of card to command
     *
     * @param p_strategyName name of the strategy
     * @return returns the Strategies object with the card related to the order
     */
    public static Strategies stringToStrategyMapper(String p_strategyName) {
        switch (p_strategyName.toLowerCase()) {
            case "aggressive":
                return AGGRESSIVE;
            case "benevolent":
                return BENEVOLENT;
            case "random":
                return RANDOM;
            case "cheater":
                return CHEATER;
            case "human":
                return HUMAN;
        }
        return null;

    }
}
