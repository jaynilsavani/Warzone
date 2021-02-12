package com.soen6441.warzone.model;

import java.util.Map;
import java.util.Objects;
import lombok.EqualsAndHashCode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * WarMap class is the main model for map management. From here the data
 * structure started. This Map is having Map of continents and continent will
 * have countries and those countries will have their adjacent countries.
 *
 * Three annotations (Getter, Setter, toString), you can see on the top of the
 * class are lombok dependencies to automatically generate getter, setter and
 * tostring method in the code.
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">yashkumar vaghani</a>
 */
@Getter
@Setter
@ToString
@Component
@EqualsAndHashCode
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
     * It will represent the status of map object.
     */
    private boolean d_status;

    /**
     * It will represent the status of the map whether it is valid or not.
     */
    private boolean d_isValid;
}
