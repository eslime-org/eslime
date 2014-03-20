/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package io.serialize;

import geometry.Geometry;
import layers.LayerManager;
import layers.cell.CellLayer;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CoordinateIndexer extends Serializer {

    // This file specifies the relationship between vector index and coordinate.
    private final String COORDMAP_FILENAME = "coordmap.txt";

    public CoordinateIndexer(GeneralParameters p) {
        super(p);
    }

    protected void makeCoordinateMap() {
        CellLayer layer = layerManager.getCellLayer();
        try {

            String coordMapFileStr = p.getInstancePath() + '/' + COORDMAP_FILENAME;
            File coordMapFile = new File(coordMapFileStr);
            FileWriter fw = new FileWriter(coordMapFile);
            BufferedWriter bwp = new BufferedWriter(fw);

            Geometry geom = layer.getGeometry();
            for (Coordinate c : geom.getCanonicalSites()) {
                StringBuilder sb = new StringBuilder();
                sb.append(layer.getGeometry().coordToIndex(c));
                sb.append("\t");
                sb.append(c.toString());
                sb.append("\n");
                bwp.append(sb.toString());
            }

            bwp.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void init(LayerManager lm) {
        super.init(lm);
    }

    @Override
    public void step(Coordinate[] highlights, double gillespie, int frame) {
        // Doesn't do anything
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        // Writes coordinate map of model to disk
        makeCoordinateMap();
    }

    @Override
    public void close() {
        // Doesn't do anything
    }
}