package com.soen6441.warzone.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * RiskMap class is the main model for map management. From here the data
 * structure started. This Map is having Map of continents and continent will
 * have territories and those territories will have their adjacent territories.
 *
 * Three annotations (Getter, Setter, toString) you can see on the top of the class are
 * lombok dependencies to automatically generate getter, setter and tostring.
 * method in the code.
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">yashkumar vaghani</a>
 */
@Getter
@Setter
@ToString
public class WarMap {

    /**
     * It will represent the name of mapfile.
     */
    private String d_mapName;

    /**
     * This will store all the continents with index.
     */
    private Map<Integer, Continent> d_continents;

    /**
     * It'll represent the status of the map whether it's valid or not.
     */
    private String d_status;
}
