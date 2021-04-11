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
    public CommandResponse d_orderResponse = new CommandResponse();

    /**
     * This method is used to for order execution
     * @return Whether Order Executed or not
     */
    abstract public boolean executeOrder();

    /**
     * This method is used to get GameData from player
     *
     * @return Updated GameData By the player
     */
    public GameData getGameData() {
        return d_gameData;
    }

    /**
     * This method is used to get an order from command response
     *
     * @return OrderResponse in CommandResponse Form
     */
    public CommandResponse getOrderResponse() {
        return d_orderResponse;
    }

    /**
     * This method is used to set any order in command response
     * @param p_orderResponse  OrderResponse in CommandResponse Form
     */
    public void setOrderResponse(CommandResponse p_orderResponse) {
        d_orderResponse = p_orderResponse;
    }

}
