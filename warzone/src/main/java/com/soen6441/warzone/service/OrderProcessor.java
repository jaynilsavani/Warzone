package com.soen6441.warzone.service;

/**
 * This interface is used to Process order in specific hierarchy
 * MapHandlingImpl is the implementation of it
 *
 * @author <a href="mailto:patelvicky1995@gmail.com">Vicky Patel</a>
 */
public interface OrderProcessor {

    //load the Map
    public void loadMap();

    //show current Map
    public void showMap();

    //Save the current map
    public void saveMap();

    // Edit country
    public void editCountry();

    //Set players in the Game
    public void setPlayers();

    //Assign Countries to the Player
    void assignCountries();

    // reinforcement Armies to country
    public void reinforce();

    //Moving armies from one territory to another of same player else battle with another player
    public void advance();

    //Moving armies from one territory to another of same player else battle with another player
    public void airLift();

    //reduce the opponent's adjacent country army by half
    public void bomb();

    //block the order
    //not sure about this
    public void blockade();

    //Negotiate with opponent
    public void negotiate();


    // move to the next phase
    abstract public void next();

    // end game
    public void endGame();
}
