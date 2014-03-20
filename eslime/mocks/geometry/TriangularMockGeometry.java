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

package geometry;

import structural.Flags;
import structural.identifiers.Coordinate;

import java.util.ArrayList;

public class TriangularMockGeometry extends MockGeometry {
    // Used to emulate a boundary condition. Note that the mock BC only
    // is defined along one edge, so use with caution.
    private int radius;

    public TriangularMockGeometry() {
        super();
        setConnectivity(3);
        setDimensionality(2);
    }

    @Override
    public Coordinate[] getNeighbors(Coordinate coord, int mode) {
        if (!coord.hasFlag(Flags.PLANAR))
            throw new IllegalStateException();

        ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();

        int x = coord.x();
        int y = coord.y();

        consider(neighbors, x, y + 1);
        consider(neighbors, x + 1, y + 1);
        consider(neighbors, x + 1, y);
        consider(neighbors, x, y - 1);
        consider(neighbors, x - 1, y - 1);
        consider(neighbors, x - 1, y);

        return neighbors.toArray(new Coordinate[0]);
    }

    public Coordinate rel2abs(Coordinate coord, Coordinate displacement, int mode) {
        // Only works in x direction
        int x1 = coord.x() + displacement.x();
        if (radius > 0 && x1 >= radius) {
            return null;
        }
        int y1 = coord.y();
        int f1 = coord.flags();

        return new Coordinate(x1, y1, f1);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}