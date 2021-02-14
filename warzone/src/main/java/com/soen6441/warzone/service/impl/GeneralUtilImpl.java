package com.soen6441.warzone.service.impl;

import com.soen6441.warzone.model.CommandResponse;
import com.soen6441.warzone.service.GeneralUtil;
import static com.soen6441.warzone.service.impl.MapHandlingImpl.MAP_DEF_PATH;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    @Autowired
    private CommandResponse d_commandResponse;

    @Override
    public boolean isNullOrEmpty(String p_str) {
        if (p_str != null && !p_str.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validateIOString(String p_string, String p_regex) {
        if (!p_string.isEmpty()) {
            Pattern l_pattern = Pattern.compile(p_regex);
            Matcher l_matcher = l_pattern.matcher(p_string);
            return l_matcher.find();
        } else {
            return false;
        }
    }

    @Override
    public void prepareResponse(boolean p_isValid, String p_responeMessage) {
        d_commandResponse.setD_isValid(p_isValid);
        d_commandResponse.setD_responseString(p_responeMessage);
    }

    @Override
    public CommandResponse getResponse() {
        return d_commandResponse;
    }

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
                if (!l_visited[l_x]) //if there is a node, not visited by traversal, graph is not connected
                {
                    return false;
                }
            }
        }

        return true;
    }

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

    @Override
    public List<String> getAvailableMapFiles() throws IOException {
        List<String> l_maps = new ArrayList<>();

        l_maps = getListOfAllFiles(Paths.get(MAP_DEF_PATH), ".map");

        return l_maps;
    }

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
}
