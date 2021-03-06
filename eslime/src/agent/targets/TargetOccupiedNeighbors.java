/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package agent.targets;

import cells.BehaviorCell;
import control.identifiers.Coordinate;
import geometry.Geometry;
import layers.LayerManager;
import layers.cell.CellLayerViewer;
import processes.discrete.filter.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Targets specify which cells should receive the consequences
 * of an Action.
 * Created by dbborens on 2/7/14.
 */
public class TargetOccupiedNeighbors extends TargetRule {
    public TargetOccupiedNeighbors(BehaviorCell callback, LayerManager layerManager, Filter filter, int maximum, Random random) {
        super(callback, layerManager, filter, maximum, random);
    }

    @Override
    protected List<Coordinate> getCandidates(BehaviorCell caller) {
        // Get geometry
        Geometry geom = layerManager.getCellLayer().getGeometry();

        // Get cell layer viewer
        CellLayerViewer viewer = layerManager.getCellLayer().getViewer();

        // Get self coordinate
        Coordinate self = layerManager.getCellLayer().getLookupManager().getCellLocation(callback);

        // Get coordinates of neighbors from geometry
        Coordinate[] neighbors = geom.getNeighbors(self, Geometry.APPLY_BOUNDARIES);

        // Create an array list of neighbors that are occupied
        ArrayList<Coordinate> occNeighbors = new ArrayList<>(neighbors.length);

        for (Coordinate neighbor : neighbors) {
            if (viewer.isOccupied(neighbor)) {
                occNeighbors.add(neighbor);
            }
        }

        // Return the array
        return occNeighbors;
    }

    @Override
    public TargetRule clone(BehaviorCell child) {
        return new TargetOccupiedNeighbors(child, layerManager, filter, maximum, random);
    }

}
