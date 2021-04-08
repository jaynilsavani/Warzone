package com.soen6441.warzone.model;

/**
 * This interface is used for and OrderImpl is the implementation of it. Three
 * annotations (Getter,Setter, toString), you can see on the top of the class
 * are lombok dependencies to automatically generate getter, setter and tostring
 * method in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public abstract class Order {

    /**
     * Player For Which Player is being created
     */
    public Player d_player;
    /**
     * GameData in current Game
     */
    public GameData d_gameData;
    /**
     * Order response of the Executed Order
     */
    public CommandResponse d_orderResponse=new CommandResponse();

    /**
     * @return Whether Order Executed or not
     */
    abstract public boolean executeOrder();

    /**
     *
     * @return Updated GameData
     */
    public GameData getGameData() {
        return d_gameData;
    }

    /**
     *
     * @return OrderResponse in CommandResponse Form
     */
    public CommandResponse getOrderResponse() {
        return d_orderResponse;
    }

}
