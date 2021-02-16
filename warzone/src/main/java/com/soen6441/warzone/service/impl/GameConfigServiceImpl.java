package com.soen6441.warzone.service.impl;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.MapHandlingInterface;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * this is an implementation of GameConfigService for configuration utility
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 */
@Service
public class GameConfigServiceImpl implements GameConfigService {

    @Autowired
    private MapHandlingInterface d_mapHandlingImpl;

    @Autowired
    private GeneralUtil d_generalUtil;

    @Override
    public CommandResponse showPlayerMap(Player p_player) {
        return null;
    }

    /**
     *
     * {@inheritDoc }
     */
    @Override
    public WarMap loadMap(String p_fileName) throws IOException{
        return d_mapHandlingImpl.readMap(p_fileName);
    }
}
