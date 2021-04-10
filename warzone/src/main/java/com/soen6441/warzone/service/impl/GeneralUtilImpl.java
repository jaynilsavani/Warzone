package com.soen6441.warzone.service.impl;

import com.soen6441.warzone.adapterpattern.ConquestMapReader;
import com.soen6441.warzone.adapterpattern.DominationMapReader;
import com.soen6441.warzone.adapterpattern.FileReaderAdapter;
import static com.soen6441.warzone.config.WarzoneConstants.*;
import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.model.WarMap;
import com.soen6441.warzone.service.GeneralUtil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * This Class is used for General Service Utility
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Service
public class GeneralUtilImpl implements GeneralUtil {

    /**
     * This is used to set and get Command Response facility
     */
    @Autowired
    private CommandResponse d_commandResponse;

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isNullOrEmpty(String p_str) {
        if (p_str != null && !p_str.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean validateIOString(String p_string, String p_regex) {
        if (!p_string.isEmpty()) {
            Pattern l_pattern = Pattern.compile(p_regex);
            Matcher l_matcher = l_pattern.matcher(p_string);
            return l_matcher.matches();
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void prepareResponse(boolean p_isValid, String p_responeMessage) {
        d_commandResponse.setD_isValid(p_isValid);
        d_commandResponse.setD_responseString(p_responeMessage);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CommandResponse getResponse() {
        return d_commandResponse;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isConnected(int[][] p_metricesOfMap, int p_noOfCountries) {
        //created visited array
        boolean[] l_visited = new boolean[p_noOfCountries];

        //check if all the vertices are visited, if yes then graph is connected
        for (int l_i = 0; l_i < p_noOfCountries; l_i++) {
            for (int l_j = 0; l_j < p_noOfCountries; l_j++) {
                l_visited[l_j] = false;
            }
            traverse(l_i, l_visited, p_noOfCountries, p_metricesOfMap);
            for (int l_x = 0; l_x < p_noOfCountries; l_x++) {
                //if there is a node, not visited by traversal, graph is not connected
                if (!l_visited[l_x]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void traverse(int p_source, boolean[] p_visited, int p_noOfCountries, int[][] p_metricesOfMap) {
        p_visited[p_source] = true; //mark v as visited
        for (int l_i = 0; l_i < p_noOfCountries; l_i++) {
            if (p_metricesOfMap[p_source][l_i] == 1) {
                if (!p_visited[l_i]) {
                    traverse(l_i, p_visited, p_noOfCountries, p_metricesOfMap);
                }
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getAvailableMapFiles() throws IOException {
        List<String> l_maps = new ArrayList<>();

        l_maps = getListOfAllFiles(Paths.get(MAP_DEF_PATH), ".map");

        return l_maps;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getListOfAllFiles(Path p_path, String p_fileExtension)
            throws IOException {
        List<String> l_files;
        try (Stream<Path> l_walk = Files.walk(p_path)) {
            l_files = l_walk.map(filePath -> filePath.toFile().getName())
                    .filter(fileName -> fileName.endsWith(p_fileExtension))
                    .collect(Collectors.toList());
        }

        return l_files;
    }

    /**
     *
     * {@inheritDoc }
     */
    @Override
    public String toTitleCase(String p_input) {
        StringBuilder l_titleCase = new StringBuilder(p_input.length());
        boolean l_nextTitleCase = true;

        for (char p_c : p_input.toCharArray()) {
            if (Character.isSpaceChar(p_c)) {
                l_nextTitleCase = true;
            } else if (l_nextTitleCase) {
                p_c = Character.toTitleCase(p_c);
                l_nextTitleCase = false;
            }

            l_titleCase.append(p_c);
        }

        return l_titleCase.toString();
    }

    @Override
    public WarMap readMapByType(String p_fileName) throws IOException {
        boolean l_isConquestMap = false;
        DominationMapReader l_dominationMapReader;

        String l_fileLine = "";
        BufferedReader l_bufferedreader = new BufferedReader(new FileReader(MAP_DEF_PATH + p_fileName));
        //while loop read each line from file and process accordingly
        while ((l_fileLine = l_bufferedreader.readLine()) != null) {
            if (l_fileLine != null && !l_fileLine.isEmpty()) {
                if (l_fileLine.equalsIgnoreCase(MAP)) {
                    l_isConquestMap = true;
                    break;
                }
                if (l_fileLine.equalsIgnoreCase(FILES)) {
                    l_isConquestMap = false;
                    break;
                }
            }
        }

        if (l_isConquestMap) {
            l_dominationMapReader = new FileReaderAdapter(new ConquestMapReader());
        } else {
            l_dominationMapReader = new DominationMapReader();
        }

        return l_dominationMapReader.readMap(p_fileName);
    }

    @Override
    public boolean writeMapByType(WarMap p_warMap, boolean p_isConquest) throws IOException {
        DominationMapReader l_dominationMapReader;
        if (p_isConquest) {
            l_dominationMapReader = new FileReaderAdapter(new ConquestMapReader());
        } else {
            l_dominationMapReader = new DominationMapReader();
        }
        //save the map in system
        return l_dominationMapReader.writeMap(p_warMap);
    }

    @Override
    public int uniqueRandomNumberGenerate(int p_startNumber, int p_endNumber) {
        ArrayList<Integer> l_numbers = new ArrayList<Integer>();
        for (int l_number = p_startNumber; l_number < p_endNumber; l_number++) {
            l_numbers.add(l_number);
        }
        Collections.shuffle(l_numbers);
        return l_numbers.get(0);
    }

}
