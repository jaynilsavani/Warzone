package com.soen6441.warzone.adapterpattern;

import com.soen6441.warzone.model.WarMap;

import java.io.IOException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This Class is used for Adapting ConquestMapReader to Existing map System.
 * Three annotations (Getter,Setter, NoArgsConstructor), you can see on the
 * top of the class are lombok dependencies to automatically generate getter,
 * setter method and default Constructor in the code.
 *
 * @author <a href="mailto:g_dobari@encs.concordia.ca">Gaurang Dobariya</a>
 */
@Getter
@Setter
@NoArgsConstructor
public class FileReaderAdapter extends DominationMapReader {

    /**
     * This is the Object of the Adaptee Class
     */
    private ConquestMapReader d_conquestMapReader;

    /**
     * This is parameterized Constructor
     *
     * @param d_conquestMapReader ConquestMap Reader Object to Construct Adapter
     *                            Object
     */
    public FileReaderAdapter(ConquestMapReader d_conquestMapReader) {
        this.d_conquestMapReader = d_conquestMapReader;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean writeMap(WarMap p_warMap) {
        return d_conquestMapReader.writeConquestMap( p_warMap );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public WarMap readMap(String p_fileName) throws IOException {
        return d_conquestMapReader.readConquestMap( p_fileName );
    }

}
