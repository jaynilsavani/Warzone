package com.soen6441.warzone.service.impl;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GamePlay;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.MapHandlingInterface;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
    public WarMap loadMap(String p_fileName) throws IOException {
        return d_mapHandlingImpl.readMap(p_fileName);
    }

    /**
     * This function is used to update layer list
     *
     * @param p_currentGamePlay
     * @param p_command updation command
     * @return Current Updated Gameplay
     */
    @Override
    public GamePlay updatePlayer(GamePlay p_currentGamePlay, String p_command) {
        List<String> l_commandSegments = Arrays.asList(p_command.split(" "));
        String l_playerName;
        for (int i = 0; i < l_commandSegments.size(); i++) {
            String l_playerCommand = l_commandSegments.get(i);
            if (l_playerCommand.equalsIgnoreCase("-add") || l_playerCommand.equalsIgnoreCase("-remove")) {
//                if (l_playerCommand.equalsIgnoreCase("-add")) {
//                    l_playerName = l_commandSegments.get(i + 1);
//                    if (d_generalUtil.validateIOString(l_playerName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {
//                        {
//                        }
//                    }
//                }else{
//                    
//                }
            }
        }
        return null;
    }
}
