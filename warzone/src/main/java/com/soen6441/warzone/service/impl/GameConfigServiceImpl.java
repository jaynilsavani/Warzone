package com.soen6441.warzone.service.impl;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.GamePlay;
import com.soen6441.warzone.model.Player;
import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.service.GameConfigService;
import com.soen6441.warzone.service.GeneralUtil;
import com.soen6441.warzone.service.MapHandlingInterface;
import com.soen6441.warzone.service.impl.MapHandlingImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.soen6441.warzone.model.*;
import java.util.Arrays;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is an implementation of GameConfigService for configuration utility
 *
 * @author <a href="mailto:y_vaghan@encs.concordia.ca">Yashkumar Vaghani</a>
 */
@Service
public class GameConfigServiceImpl implements GameConfigService {

    @Autowired
    private MapHandlingInterface d_mapHandlingImpl;

    @Autowired
    private GeneralUtil d_generalUtil;

    @Autowired
    MapHandlingImpl d_mapConfig;

    @Override
    public CommandResponse showPlayerMap(GamePlay p_gamePlay) {
        String l_mapStringName=p_gamePlay.getFileName();
        try {
            WarMap l_warMap=d_mapHandlingImpl.readMap(l_mapStringName);


        CommandResponse l_showCountris=d_mapConfig.showMap(l_warMap);
        if(p_gamePlay.getPlayerList()==null)
        {
            String l_showMapOfCountris=l_showCountris.getD_responseString();
            d_generalUtil.prepareResponse(true,l_showMapOfCountris);
        }
        else
        {

            String l_showMapOfCountris=l_showCountris.getD_responseString();
            List<Country> l_countryList=d_mapConfig.getAvailableCountries(l_warMap);
            int l_colSize=l_countryList.size()+1;
            int l_rowSize=p_gamePlay.getPlayerList().size()+1;
            String [][]l_playerToCountry=new String[l_rowSize][l_colSize];
            int l_maxLength=0;
            for(int l_i=0;l_i<l_rowSize;l_i++)
            {
                for(int l_j=0;l_j<l_colSize;l_j++)
                {
                    if(l_i==0 && l_j==0)
                    {

                        l_playerToCountry[l_i][l_j]="";
                        continue;
                    }
                    else if(l_i==0 && l_j!=0)
                    {
                        l_playerToCountry[l_i][l_j]=l_countryList.get(l_j-1).getD_countryName();
                        if (l_maxLength < l_playerToCountry[l_i][l_j].length()) {
                            l_maxLength = l_playerToCountry[l_i][l_j].length();
                        }
                    }
                    else if(l_i!=0 && l_j==0)
                    {
                        l_playerToCountry[l_i][l_j]=p_gamePlay.getPlayerList().get(l_i-1).getD_playerName();
                        if (l_maxLength < l_playerToCountry[l_i][l_j].length()) {
                            l_maxLength = l_playerToCountry[l_i][l_j].length();
                        }
                    }
                    else
                    {
                        if(p_gamePlay.getPlayerList().get(l_i-1).getD_ownedCountries().contains(l_countryList.get(l_j-1)))
                        {
                            //l_playerToCountry[l_i][l_j]= String.valueOf(l_countryList.get(l_j-1).getD_noOfArmies());
                            l_playerToCountry[l_i][l_j]="1";


                        }
                        else
                        {
                            l_playerToCountry[l_i][l_j]="0";
                        }
                    }
                }
            }
            l_showMapOfCountris=l_showMapOfCountris+"\n";
            for (int l_i=0;l_i<l_rowSize;l_i++)
            {
                for(int l_j=0;l_j<l_colSize;l_j++)
                {
                    String l_stringFrmat = String.format("%1$" + l_maxLength + "s", l_playerToCountry[l_i][l_j]);
                    l_showMapOfCountris=l_showMapOfCountris+l_stringFrmat+"\t";
                }
                l_showMapOfCountris=l_showMapOfCountris+"\n";
            }
            l_showCountris.setD_isValid(true);
            l_showCountris.setD_responseString(l_showMapOfCountris);

        }


        return l_showCountris;
        }
        catch (IOException e) {
            d_generalUtil.prepareResponse(false,"could not read the map");
            return d_generalUtil.getResponse();
        }
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
     * @param p_currentGamePlay : object of GamePlay model
     * @param p_command : updation command
     * @return Current Updated Gameplay
     */
    @Override
    public GamePlay updatePlayer(GamePlay p_currentGamePlay, String p_command) {
        List<String> l_commandSegments = Arrays.asList(p_command.split(" "));
        String l_playerName;

        for (int i = 0; i < l_commandSegments.size(); i++) {
            String l_playerCommand = l_commandSegments.get(i);
            if (l_playerCommand.equalsIgnoreCase("-add") || l_playerCommand.equalsIgnoreCase("-remove")) {
                if (l_playerCommand.equalsIgnoreCase("-add")) {
                    l_playerName = l_commandSegments.get(i + 1);
                    if (d_generalUtil.validateIOString(l_playerName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {
                        {
                            //To check if Exist Or not 
                            if (getPlayerByName(p_currentGamePlay, l_playerName).isEmpty()) {
                                Player l_player = new Player();
                                l_player.setD_playerName(l_playerName);
                                if (p_currentGamePlay.getPlayerList() == null) {
                                    p_currentGamePlay.setPlayerList(new ArrayList<>());
                                }
                                p_currentGamePlay.getPlayerList().add(l_player);
                            }

                        }
                    }
                } else if (l_playerCommand.equalsIgnoreCase("-remove")) {
                    l_playerName = l_commandSegments.get(i + 1);
                    if (d_generalUtil.validateIOString(l_playerName, "^([a-zA-Z]-+\\s)*[a-zA-Z-]+$")) {
                        {
                            if (getPlayerByName(p_currentGamePlay, l_playerName).size() > 0) {
                                Player l_removedPlayer = getPlayerByName(p_currentGamePlay, l_playerName).get(0);
                                if (p_currentGamePlay.getPlayerList() != null || p_currentGamePlay.getPlayerList().size() > 0) {
                                    p_currentGamePlay.getPlayerList().remove(l_removedPlayer);
                                    p_currentGamePlay.setPlayerList(p_currentGamePlay.getPlayerList());
                                }
                            }

                        }
                    }
                }
            }
        }
        return p_currentGamePlay;
    }

    public List<Player> getPlayerByName(GamePlay p_currentGamePlay, String p_playerName) {
        List<Player> l_players = new ArrayList<Player>();
        if (p_playerName != null && p_currentGamePlay.getPlayerList() != null) {
            l_players = p_currentGamePlay.getPlayerList().stream().filter(l_player -> l_player.getD_playerName().equalsIgnoreCase(p_playerName)).collect(Collectors.toList());
        }

        return l_players;
    }

    @Override
    public CommandResponse assignCountries(GamePlay p_gamePlay) throws IOException {
        String l_mapFileName=p_gamePlay.getFileName();
        WarMap l_warMap=d_mapConfig.readMap(l_mapFileName);
        if(p_gamePlay.getPlayerList()==null)
        {
            d_generalUtil.prepareResponse(false,"No player in the game");

        }
        else if(p_gamePlay.getPlayerList().get(0).getD_ownedCountries().size()!=0)
        {
            d_generalUtil.prepareResponse(false,"Countries are already assigned");
        }
        else {
            int l_noOfPlayers=p_gamePlay.getPlayerList().size();

            int l_noOfCountries=d_mapConfig.getAvailableCountries(l_warMap).size();
            List<Country> l_country_check=d_mapConfig.getAvailableCountries(l_warMap);
            List<Player> l_players=p_gamePlay.getPlayerList();
            int l_counter=l_noOfCountries/l_noOfPlayers;
            int l_modulus=l_noOfCountries%l_noOfPlayers;
            List<Country> l_checkCountryOnPlayer=new ArrayList<Country>();
            int l_i=0,l_j=0;
            for(l_i=0;l_i<l_counter;l_i++)
            {
                for(l_j=0;l_j<l_noOfPlayers;l_j++)
                {
                    if(p_gamePlay.getPlayerList().get(l_j).getD_ownedCountries()==null)
                    {
                        List<Country> l_country=new ArrayList<Country>();
                        while(true)
                        {Random rand = new Random();
                            int rand_int1 = rand.nextInt(l_noOfCountries);
                            if(!l_checkCountryOnPlayer.contains(l_country_check.get(rand_int1)))
                            {
                                l_country.add(l_country_check.get(rand_int1));
                                l_checkCountryOnPlayer.add(l_country_check.get(rand_int1));
                                p_gamePlay.getPlayerList().get(l_j).setD_ownedCountries(l_country);
                                break;

                            }

                        }
                    }
                    else
                    {
                        while(true)
                        {Random rand = new Random();
                            int rand_int1 = rand.nextInt(l_noOfCountries);
                            if(!l_checkCountryOnPlayer.contains(l_country_check.get(rand_int1)))
                            {
                                p_gamePlay.getPlayerList().get(l_j).getD_ownedCountries().add(l_country_check.get(rand_int1));
                                l_checkCountryOnPlayer.add(l_country_check.get(rand_int1));
                                break;

                            }

                        }
                    }

                }
            }
            for(l_i=0;l_i<l_modulus;l_i++)
            {
                while(true)
                {Random rand = new Random();
                    int rand_int1 = rand.nextInt(l_noOfCountries);
                    if(!l_checkCountryOnPlayer.contains(l_country_check.get(rand_int1)))
                    {
                        p_gamePlay.getPlayerList().get(l_i).getD_ownedCountries().add(l_country_check.get(rand_int1));
                        l_checkCountryOnPlayer.add(l_country_check.get(rand_int1));
                        break;

                    }

                }
            }
            String l_response="";
            for(l_i=0;l_i<l_noOfPlayers;l_i++)
            {
                l_response=l_response+p_gamePlay.getPlayerList().get(l_i).getD_playerName()+" owns [";
                for(l_j=0;l_j<p_gamePlay.getPlayerList().get(l_i).getD_ownedCountries().size();l_j++)
                {
                    if(l_j==(p_gamePlay.getPlayerList().get(l_i).getD_ownedCountries().size()-1))
                    {
                        l_response=l_response+p_gamePlay.getPlayerList().get(l_i).getD_ownedCountries().get(l_j).getD_countryName();
                    }
                    else
                    {
                        l_response=l_response+p_gamePlay.getPlayerList().get(l_i).getD_ownedCountries().get(l_j).getD_countryName()+",";
                    }

                }
                l_response=l_response+"]\n";
            }

            d_generalUtil.prepareResponse(true,l_response);
        }


        return d_generalUtil.getResponse();

    }
}
