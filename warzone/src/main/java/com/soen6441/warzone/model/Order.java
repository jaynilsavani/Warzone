package com.soen6441.warzone.model;

/**
 * This interface is used for and OrderImpl is the implementation of it. Three
 * annotations (Getter,Setter, toString), you can see on the top of the class
 * are lombok dependencies to automatically generate getter, setter and tostring
 * method in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
public interface Order {

    /**
     * @return Whether Order Executed or not
     */
    boolean executeOrder();

}
