/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.deserialize;

import control.identifiers.Coordinate;
import structural.utilities.FileConventions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dbborens on 12/10/13.
 */
public class CoordinateDeindexer {

    // This file specifies the relationship between vector index and coordinate.

    protected HashMap<Integer, Coordinate> indexToCoord;
    protected HashMap<Coordinate, Integer> coordToIndex;
    private String path;

    /**
     * Default constructor for testing only!
     */
    public CoordinateDeindexer() {
    }

    public CoordinateDeindexer(String path) {

        this.path = path;

        try {
            deindex();
        } catch (FileNotFoundException e) {
            String msg = "Coordinate index file not found in output directory "
                    + path + ". Note that any serializers that depend on " +
                    "coordinate indices (such as visualizations) need to be " +
                    "specified after a <coordinate-indexer> tag in the " +
                    "<serializers> section.";

            throw new IllegalArgumentException(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Coordinate getCoordinate(Integer index) {
        return indexToCoord.get(index);
    }

    public Integer getIndex(Coordinate coord) {
        return coordToIndex.get(coord);
    }

    protected void deindex() throws IOException {
        indexToCoord = new HashMap<>();
        coordToIndex = new HashMap<>();

        String fn = path + '/' + FileConventions.COORDINATE_FILENAME;
        FileReader mfr = new FileReader(fn);
        BufferedReader mbr = new BufferedReader(mfr);
        String next = mbr.readLine();

        while (next != null) {
            // Lines are in the form
            // 		1	(0, 1 | 1)
            // 		2	(0, 2 | 1)
            next = next.trim();
            String[] mapping = next.split("\t");
            Integer key = Integer.valueOf(mapping[0]);

            String token = mapping[1];
            Coordinate c = parseCoordinate(token);

            indexToCoord.put(key, c);
            coordToIndex.put(c, key);

            next = mbr.readLine();

        }

    }

    protected Coordinate parseCoordinate(String token) {
        String pStr = ("\\((\\d+), (\\d+)(, (\\d+))? \\| (\\d+)\\)");

        Coordinate c;
        Pattern pattern = Pattern.compile(pStr);
        Matcher matcher = pattern.matcher(token);
        matcher.find();

        int x = Integer.valueOf(matcher.group(1));
        int y = Integer.valueOf(matcher.group(2));
        int flags = Integer.valueOf(matcher.group(5));

        if (matcher.group(4) != null) {
            // (1, 2, 3 | 4)
            int z = Integer.valueOf(matcher.group(4));
            c = new Coordinate(x, y, z, flags);
        } else {
            // (1, 2 | 3)
            c = new Coordinate(x, y, flags);
        }

        return c;
    }

    public int getNumSites() {
        return indexToCoord.size();
    }
}
