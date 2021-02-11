package com.soen6441.warzone.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.model.WarMap;
/**
 *
 * @author <a href="mailto:manthan.p.moradiya@gmail.com">Manthan Moradiya</a>
 */
@SpringBootTest
public class MapHandlingImpltests {

    @Autowired
    MapHandlingInterface map;

    @Test
    void contextLoads() {
    }

    @Test
    void testForReadMapFromFile() {
        WarMap warMap;
    }

}
