/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.deserialize;

import control.identifiers.Coordinate;
import layers.LightweightSystemState;
import structural.utilities.FileConventions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Loads highlights from all channels and makes them available
 * as sets.
 * <p/>
 * Created by dbborens on 3/26/14.
 */
public class HighlightReader {

    private HashMap<Integer, DataInputStream> inputs;
    private CoordinateDeindexer deindexer;

    public HighlightReader(String root, int[] channels, CoordinateDeindexer deindexer) {
        this.deindexer = deindexer;
        inputs = new HashMap<>(channels.length);
        for (Integer channel : channels) {
            String filename = FileConventions.makeHighlightFilename(channel);
            String fullPath = root + filename;
            DataInputStream stream = FileConventions.makeDataInputStream(fullPath);
            inputs.put(channel, stream);
        }
    }

    public Map<Integer, Set<Coordinate>> next() {
        Map<Integer, Set<Coordinate>> ret = new HashMap<>(inputs.size());
        for (Integer channel : inputs.keySet()) {
            DataInputStream channelInput = inputs.get(channel);
            Set<Coordinate> coordinates = loadNext(channelInput);
            ret.put(channel, coordinates);
        }

        return ret;
    }

    private Set<Coordinate> loadNext(DataInputStream channelInput) {
        try {
            return doLoadNext(channelInput);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Set<Coordinate> doLoadNext(DataInputStream input) throws IOException {
        Coordinate[] coordinates = PrimitiveDeserializer.readCoordinateVector(input, deindexer);

        Set<Coordinate> ret = new HashSet<Coordinate>(coordinates.length);
        for (Coordinate c : coordinates) {
            ret.add(c);
        }

        return ret;
    }

    /**
     * Load highlight data into a SystemState container.
     */
    public void populate(LightweightSystemState state) {
        Map<Integer, Set<Coordinate>> highlightsMap = next();

        for (Integer channel : highlightsMap.keySet()) {
            Set<Coordinate> highlights = highlightsMap.get(channel);
            state.setHighlights(channel, highlights);
        }
    }
}
