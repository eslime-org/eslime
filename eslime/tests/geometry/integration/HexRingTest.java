/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.integration;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.Geometry;
import geometry.boundaries.Boundary;
import geometry.boundaries.PlaneRingHard;
import geometry.boundaries.PlaneRingReflecting;
import geometry.lattice.Lattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import test.EslimeTestCase;

import java.util.HashSet;

/**
 * Regression/integration tests from earliest version of geometry
 * model. These tests use reflecting and hard ring geometries.
 * Originally ported from C++.
 *
 * @author dbborens
 */
public class HexRingTest extends EslimeTestCase {

    // DONE
    public void testCanonicalSites() {
        // Produce 6x4 HexRing
        int height = 6;
        int width = 4;
        Lattice lattice = new TriangularLattice();
        Shape shape = new Rectangle(lattice, width, height);
        Boundary boundary = new PlaneRingHard(shape, lattice);
        Geometry hr = new Geometry(lattice, shape, boundary);

        // Create unordered set of all expected coordinates
        HashSet<Coordinate> s = new HashSet<Coordinate>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int yAdj = y + (x / 2);
                Coordinate c = new Coordinate(x, yAdj, 0);
                s.add(c);
            }
        }

        Coordinate[] sites = hr.getCanonicalSites();

        // Vector should contain all of the sites.
        assertEquals(s.size(), sites.length);
        for (int i = 0; i < sites.length; i++) {
            Coordinate coord = sites[i];
            assertTrue(s.contains(coord));
        }


    }

    // DONE
    // getL1Distance(...)
    // getDisplacement(...)
    public void testL1AndDisplacement() {
        //HexRing hr = new HexRing(4, 6);
        Lattice lattice = new TriangularLattice();
        Shape shape = new Rectangle(lattice, 4, 6);
        Boundary boundary = new PlaneRingHard(shape, lattice);
        Geometry hr = new Geometry(lattice, shape, boundary);

        Coordinate p = new Coordinate(1, 1, 0);
        Coordinate q = new Coordinate(2, 4, 0);

        Coordinate disp = hr.getDisplacement(p, q, Geometry.APPLY_BOUNDARIES);
        Coordinate expected = new Coordinate(0, 1, 2, Flags.VECTOR);
        assertEquals(expected, disp);

        assertEquals(3, hr.getL1Distance(p, q, Geometry.APPLY_BOUNDARIES));

        p = new Coordinate(1, 2, 0);
        q = new Coordinate(0, 0, 0);

        disp = hr.getDisplacement(p, q, Geometry.APPLY_BOUNDARIES);
        expected = new Coordinate(0, -1, -1, Flags.VECTOR);
        assertEquals(expected, disp);

        assertEquals(2, hr.getL1Distance(p, q, Geometry.APPLY_BOUNDARIES));

        p = new Coordinate(0, 0, 0);
        q = new Coordinate(0, 0, 0);

        disp = hr.getDisplacement(p, q, Geometry.APPLY_BOUNDARIES);
        expected = new Coordinate(0, 0, 0, Flags.VECTOR);
        assertEquals(expected, disp);

        assertEquals(0, hr.getL1Distance(p, q, Geometry.APPLY_BOUNDARIES));

    }

    // DONE
    // Test wrapping
    public void testWrap() {

        //HexRing hr = new HexRing(4, 4);
        Lattice lattice = new TriangularLattice();
        Shape shape = new Rectangle(lattice, 4, 4);
        Boundary boundary = new PlaneRingHard(shape, lattice);
        Geometry hr = new Geometry(lattice, shape, boundary);

        Coordinate actual, expected, initial;

        // Over right edge
        initial = new Coordinate(4, 2, 0);
        actual = hr.apply(initial, Geometry.APPLY_BOUNDARIES);
        expected = new Coordinate(0, 0, Flags.BOUNDARY_APPLIED);
        assertEquals(actual, expected);

        // Over left edge
        initial = new Coordinate(-1, 0, 0);
        actual = hr.apply(initial, Geometry.APPLY_BOUNDARIES);
        expected = new Coordinate(3, 2, Flags.BOUNDARY_APPLIED);
        assertEquals(actual, expected);


        // Out of bounds
        initial = new Coordinate(4, 0, 0);
        actual = hr.apply(initial, Geometry.APPLY_BOUNDARIES);
        assertNull(actual);
        //expected = new Coordinate(4, 0, Flags.UNDEFINED | Flags.BOUNDARY_APPLIED);
        //assertEquals(actual, expected);

        // No wrap (internal coordinate)
        initial = new Coordinate(2, 3, 0);
        actual = hr.apply(initial, Geometry.APPLY_BOUNDARIES);
        expected = new Coordinate(2, 3, 0);
        assertEquals(actual, expected);

        // Around twice
        initial = new Coordinate(9, 6, 0);
        actual = hr.apply(initial, Geometry.APPLY_BOUNDARIES);
        expected = new Coordinate(1, 2, Flags.BOUNDARY_APPLIED);
        assertEquals(actual, expected);

    }

    public void testCellNeighbors() {
        //HexRing hr = new HexRing(6, 6);
        Lattice lattice = new TriangularLattice();
        Shape shape = new Rectangle(lattice, 6, 6);
        Boundary boundary = new PlaneRingHard(shape, lattice);
        Geometry hr = new Geometry(lattice, shape, boundary);

        // Interior
        Coordinate initial = new Coordinate(3, 4, 0);
        Coordinate coord = hr.apply(initial, Geometry.APPLY_BOUNDARIES);

        HashSet<Coordinate> interior_exp = new HashSet<Coordinate>();
        interior_exp.add(new Coordinate(3, 5, 0));
        interior_exp.add(new Coordinate(4, 5, 0));
        interior_exp.add(new Coordinate(4, 4, 0));
        interior_exp.add(new Coordinate(3, 3, 0));
        interior_exp.add(new Coordinate(2, 3, 0));
        interior_exp.add(new Coordinate(2, 4, 0));

        Coordinate[] neighbors = hr.getNeighbors(coord, Geometry.APPLY_BOUNDARIES);

        assertEquals(neighbors.length, 6);
        for (int i = 0; i < neighbors.length; i++) {
            Coordinate neighbor = neighbors[i];

            assertTrue(interior_exp.contains(neighbor));
        }

        // Side -- check wrapped
        initial = new Coordinate(5, 5, 0);
        coord = hr.apply(initial, Geometry.APPLY_BOUNDARIES);

        Coordinate[] side_exp = new Coordinate[]{
                new Coordinate(5, 6, 0),
                new Coordinate(0, 3, Flags.BOUNDARY_APPLIED),
                new Coordinate(0, 2, Flags.BOUNDARY_APPLIED),
                new Coordinate(5, 4, 0),
                new Coordinate(4, 4, 0),
                new Coordinate(4, 5, 0)
        };

        neighbors = hr.getNeighbors(coord, Geometry.APPLY_BOUNDARIES);
        assertArraysEqual(side_exp, neighbors, true);

        // Bottom -- check south is missing
        initial = new Coordinate(2, 1, 0);
        coord = hr.apply(initial, Geometry.APPLY_BOUNDARIES);

        HashSet<Coordinate> bottom_exp = new HashSet<Coordinate>();
        bottom_exp.add(new Coordinate(1, 0, 0));
        bottom_exp.add(new Coordinate(1, 1, 0));
        bottom_exp.add(new Coordinate(2, 2, 0));
        bottom_exp.add(new Coordinate(3, 2, 0));
        bottom_exp.add(new Coordinate(3, 1, 0));

        neighbors = hr.getNeighbors(coord, Geometry.APPLY_BOUNDARIES);
        neighbors = clean(neighbors);

        assertEquals(5, neighbors.length);
        for (int i = 0; i < neighbors.length; i++) {
            Coordinate neighbor = neighbors[i];
            assertTrue(bottom_exp.contains(neighbor));
        }


    }

    // All cases but top/bottom should be same as getCellNeighbors(...)
    // for the HexTorus geometry.
    // getSoluteNeighbors(...)
    public void testSoluteNeighbors() {
        //HexRing hr = new HexRing(6, 6);
        Lattice lattice = new TriangularLattice();
        Shape shape = new Rectangle(lattice, 6, 6);
        Boundary boundary = new PlaneRingReflecting(shape, lattice);
        Geometry hr = new Geometry(lattice, shape, boundary);

        // Interior
        Coordinate coord = new Coordinate(3, 4, 0);

        HashSet<Coordinate> interior_exp = new HashSet<Coordinate>();
        interior_exp.add(new Coordinate(3, 5, 0));
        interior_exp.add(new Coordinate(4, 5, 0));
        interior_exp.add(new Coordinate(4, 4, 0));
        interior_exp.add(new Coordinate(3, 3, 0));
        interior_exp.add(new Coordinate(2, 3, 0));
        interior_exp.add(new Coordinate(2, 4, 0));

        Coordinate[] neighbors = hr.getNeighbors(coord, Geometry.APPLY_BOUNDARIES);

        assertEquals(neighbors.length, 6);
        for (int i = 0; i < neighbors.length; i++) {
            Coordinate neighbor = neighbors[i];
            assertTrue(interior_exp.contains(neighbor));
        }

        // Side -- check wrapped
        coord = new Coordinate(5, 5, 0);

        Coordinate[] side_exp = new Coordinate[]{
                new Coordinate(5, 6, 0),
                new Coordinate(0, 3, Flags.BOUNDARY_APPLIED),
                new Coordinate(0, 2, Flags.BOUNDARY_APPLIED),
                new Coordinate(5, 4, 0),
                new Coordinate(4, 4, 0),
                new Coordinate(4, 5, 0),
        };
        neighbors = hr.getNeighbors(coord, Geometry.APPLY_BOUNDARIES);
        assertArraysEqual(side_exp, neighbors, true);

        // Bottom -- check south is REFLECTED
        coord = new Coordinate(2, 1, 0);

        Coordinate[] bottom_exp = new Coordinate[]{
                new Coordinate(1, 0, 0),
                new Coordinate(1, 1, 0),
                new Coordinate(2, 2, 0),
                new Coordinate(3, 2, 0),
                new Coordinate(3, 1, 0),

                // The cell is counted as a "neighbor" because southerly-moving
                // solute is reflected back.
                new Coordinate(2, 1, Flags.BOUNDARY_APPLIED)
        };

        neighbors = hr.getNeighbors(coord, Geometry.APPLY_BOUNDARIES);
        assertArraysEqual(bottom_exp, neighbors, true);
    }

    // getAnnulus(...)
    public void testAnnulus() {
        Lattice lattice = new TriangularLattice();
        Shape shape = new Rectangle(lattice, 4, 4);
        Boundary boundary = new PlaneRingHard(shape, lattice);
        Geometry hr = new Geometry(lattice, shape, boundary);

        Coordinate coord = new Coordinate(0, 2, 0);

        Coordinate[] result;

        // Point
        result = hr.getAnnulus(coord, 0, Geometry.APPLY_BOUNDARIES);
        assertEquals(1, result.length);
        assertEquals(coord, result[0]);

        // r=1
        result = hr.getAnnulus(coord, 1, Geometry.APPLY_BOUNDARIES);
        assertEquals(6, result.length);

        // r=2 (big)--restrict circumnavigation. Note that I have some trouble
        // visualizing the smaller circles, so check this functionally as well.
        result = hr.getAnnulus(coord, 2, Geometry.EXCLUDE_BOUNDARIES);

        // Top and bottom are truncated, and wrapped cells that circumnavigate
        // are rejected, so 6:
        // (2, 4); (2, 3); (2, 2); (1, 1); (0, 0); (3, 2)
        //
        // Rejected circumanvigators are all double counts of existing cells.

        assertEquals(5, result.length);

        // r=2--don't restrict circumnavigation
        result = hr.getAnnulus(coord, 2, Geometry.APPLY_BOUNDARIES);

        // We count some of the positions twice, but top/bottom
        // are truncated, so 9:
        //
        // (0, 0); (1, 1); (3, 2) <-- each counted once
        // (2, 2); (2, 3); (2, 4) <-- each counted twice
        //result = clean(result);

		/*System.out.println("A");
        for (Coordinate a : result) {
			System.out.println("   " + a);
		}*/
        assertEquals(9, result.length);
    }


    // Tests for correct behavior in vicinity of origin when dimensions
    // are 6x6, as in the lattice tests.
    public void testOriginWrap() {
        // Explicitly test wrapping behavior in vicinity of origin
        //HexRing hr = new HexRing(6, 6);
        Lattice lattice = new TriangularLattice();
        Shape shape = new Rectangle(lattice, 6, 6);
        Boundary boundary = new PlaneRingHard(shape, lattice);
        Geometry hr = new Geometry(lattice, shape, boundary);

        // (1, 0) stays (1, 0)
        Coordinate initial, actual, expected;
        initial = new Coordinate(1, 0, 0);
        expected = new Coordinate(1, 0, 0);
        actual = hr.apply(initial, Geometry.APPLY_BOUNDARIES);
        assertEquals(expected, actual);

        // (1, 1) stays (1, 1)
        initial = new Coordinate(1, 1, 0);
        expected = new Coordinate(1, 1, 0);
        actual = hr.apply(initial, Geometry.APPLY_BOUNDARIES);
        assertEquals(expected, actual);

        // (0, 1) stays (0, 1)
        initial = new Coordinate(0, 1, 0);
        expected = new Coordinate(0, 1, 0);
        actual = hr.apply(initial, Geometry.APPLY_BOUNDARIES);
        assertEquals(expected, actual);


        // (-1, -1) becomes (5, 2)
        initial = new Coordinate(-1, -1, 0);
        expected = new Coordinate(5, 2, Flags.BOUNDARY_APPLIED);
        actual = hr.apply(initial, Geometry.APPLY_BOUNDARIES);
        assertEquals(expected, actual);

        // (-1, 0) becomes (5, 3)
        initial = new Coordinate(-1, 0, 0);
        expected = new Coordinate(5, 3, Flags.BOUNDARY_APPLIED);
        actual = hr.apply(initial, Geometry.APPLY_BOUNDARIES);
        assertEquals(expected, actual);

        // (0, -1) is out of bounds
        initial = new Coordinate(0, -1, 0);
        actual = hr.apply(initial, Geometry.APPLY_BOUNDARIES);
        assertNull(actual);
    }
}
