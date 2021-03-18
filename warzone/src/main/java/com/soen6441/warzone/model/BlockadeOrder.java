package com.soen6441.warzone.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * This Class is used for The blockade order Command Three annotations
 * (Getter,Setter, toString), you can see on the top of the class are lombok
 * dependencies to automatically generate getter, setter and tostring method in
 * the code.
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@Getter
@Setter
@ToString
public class BlockadeOrder implements Order{

    /**
     * Country in this order
     */
    private String d_countryName;
    /**
     * Player in this order
     */
    private Player d_player;

    @Override
    public boolean executeOrder() {
        return false;
    }
}
