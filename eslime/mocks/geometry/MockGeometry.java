/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry;

import control.identifiers.Coordinate;
import control.identifiers.Flags;

import java.util.ArrayList;
import java.util.HashMap;

public class MockGeometry extends Geometry {

	/* infinite */

    protected Coordinate[] canonicalSites;
    private boolean infinite;
    private HashMap<Coordinate, Coordinate[]> cellNeighbors = new HashMap<Coordinate, Coordinate[]>();
    private int connectivity;

    /* canonicalSites */
    private int dimensionality;
    private Coordinate center;
    private boolean reportEquals;
    private double lastRequestedScale = 0;


    private Class[] componentClasses;

    public MockGeometry() {
        super(null, null, null);

        componentClasses = new Class[] {
                Object.class,
                Object.class,
                Object.class
        };
    }

    @Override
    public boolean isInfinite() {
        return infinite;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    @Override
    public Coordinate[] getCanonicalSites() {
        return canonicalSites;
    }

    public void setCanonicalSites(Coordinate[] canonicalSites) {
        // Note that the MockGeometry override of setCanonicalSites
        // does not canonicalize coordinates -- in particular, 2D
        // coordinates will not receive the PLANAR flag.
        this.canonicalSites = canonicalSites;
        rebuildIndex();
    }

    public void setCellNeighbors(Coordinate coord, Coordinate[] neighbors) {
        cellNeighbors.put(coord, neighbors);
    }

    @Override
    public Coordinate[] getNeighbors(Coordinate coord, int mode) {
        return cellNeighbors.get(coord);
    }

    protected void consider(ArrayList<Coordinate> neighbors, int x, int y) {
        Coordinate candidate = new Coordinate(x, y, 0);

        for (int i = 0; i < canonicalSites.length; i++) {
            if (canonicalSites[i].equals(candidate)) {
                neighbors.add(candidate);
                return;
            }
        }

    }

    protected void consider(ArrayList<Coordinate> neighbors, int x, int y, int z) {
        Coordinate candidate = new Coordinate(x, y, z, 0);

        for (int i = 0; i < canonicalSites.length; i++) {
            if (canonicalSites[i].equals(candidate)) {
                neighbors.add(candidate);
                return;
            }
        }
    }

    public int getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(int connectivity) {
        this.connectivity = connectivity;
    }

    public int getDimensionality() {
        return dimensionality;
    }

    public void setDimensionality(int dimensionality) {
        this.dimensionality = dimensionality;
    }

    /* rel2abs -- overriden as needed in subclass mocks */
    public Coordinate rel2abs(Coordinate origin, Coordinate displacement, int mode) {
        return origin.addFlags(Flags.UNDEFINED);
    }

    public Coordinate getCenter() {
        return center;
    }

    public void setCenter(Coordinate center) {
        this.center = center;
    }

    /**
     * Causes the equality operator of this mock object to return
     * the specified value in all cases.
     */
    public void setEquals(boolean reportEquals) {
        this.reportEquals = reportEquals;
    }

    @Override
    public boolean equals(Object obj) {
        return reportEquals;
    }

    public double getLastRequestedScale() {
        return lastRequestedScale;
    }

    @Override
    public Geometry cloneAtScale(double rangeScale) {
        lastRequestedScale = rangeScale;
        return null;
    }

    @Override
    public Class[] getComponentClasses() {
        return componentClasses;
    }

    public void setComponentClasses(Class[] componentClasses) {
        this.componentClasses = componentClasses;
    }

}
