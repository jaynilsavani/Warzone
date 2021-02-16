package com.soen6441.warzone.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 *
 * This Class is used for The deploy order Command 
 * Three annotations (Getter,Setter, toString), you can see on the top of the class are lombok
 * dependencies to automatically generate getter, setter and tostring method in
 * the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@ToString
@Component
public class DeployOrder implements Order {

    private int d_noOfArmies;
    private int d_CountryIndex;

    /**
     * {@inheritDoc }
     *
     * This method deploy armies to the country
     */
    @Override
    public boolean executeOrder() {
        // logic of the Execute order
        // move armies here
        // and decrease the number of armies
        return true;
    }

}
