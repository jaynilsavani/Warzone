package com.soen6441.warzone.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class represents Territory model and property of territory from the map
 * file. It's also having list of neighbourTerritories.
 *
 * @author <a href="mailto:y_vaghan">yashkumar vaghani</a>
 * @see com.soen6441.warzone.model.Continent
 */
@Getter
@Setter
@ToString
public class Territory {

    /**
     * It'll store index of territory
     */
    private int d_territoryIndex;

    /**
     * It'll store Name of territory
     */
    private String d_territoryName;

    /**
     * It'll store index of continent in which this territory located
     */
    private int d_continentIndex;

    /**
     * List of neighbor territory of this territory
     */
    private List<String> d_neighbourTerritories;

}
