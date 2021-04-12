package com.soen6441.warzone.model;

/**
 * This Enum is used for
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public enum TournamentWinner {
    /**
     * enum variable of order type used to select Strategy
     */
    AGGRESSIVE, BENEVOLENT, CHEATER, RANDOM, DRAW;

    public static TournamentWinner StrategyToTournamentMapper(Strategies p_strategies) {
        switch (p_strategies) {
            case AGGRESSIVE:
                return AGGRESSIVE;
            case BENEVOLENT:
                return BENEVOLENT;
            case CHEATER:
                return CHEATER;
            case RANDOM:
                return RANDOM;
            default:
                return DRAW;
        }
    }
}
