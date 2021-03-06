/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package geometry.shape;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import junit.framework.TestCase;
import test.EslimeTestCase;

public class LineTest extends EslimeTestCase {

    private Shape odd;
    private Shape even;

    private Lattice evenLattice;

    @Override
    public void setUp() {
        Lattice oddLattice = new LinearLattice();
        evenLattice = new LinearLattice();

        even = new Line(evenLattice, 4);
        odd = new Line(oddLattice, 5);
    }

    public void testGetCenter() {
        Coordinate expected, actual;

        // Even -- we round down
        expected = new Coordinate(0, 1, 0);
        actual = even.getCenter();
        assertEquals(expected, actual);

        // Odd
        expected = new Coordinate(0, 2, 0);
        actual = odd.getCenter();
        assertEquals(expected, actual);
    }

    public void testGetBoundaries() {
        Coordinate[] expected, actual;

        // Even
        expected = new Coordinate[]{
                new Coordinate(0, 0, 0),
                new Coordinate(0, 3, 0),
        };
        actual = even.getBoundaries();
        assertArraysEqual(expected, actual, true);

        // Odd
        expected = new Coordinate[]{
                new Coordinate(0, 0, 0),
                new Coordinate(0, 4, 0),
        };
        actual = odd.getBoundaries();
        assertArraysEqual(expected, actual, true);
    }

    public void testCanonicalSites() {
        Coordinate[] expected, actual;

        expected = new Coordinate[]{
                new Coordinate(0, 0, 0),
                new Coordinate(0, 1, 0),
                new Coordinate(0, 2, 0),
                new Coordinate(0, 3, 0),
        };

        actual = even.getCanonicalSites();
        assertArraysEqual(expected, actual, true);
    }

    public void testOverbounds() {
        Coordinate expected, actual;

        // Test coordinates -- in bounds
        Coordinate a, b;

        a = new Coordinate(0, 0, 0);
        b = new Coordinate(0, 2, 0);

        // Test coordinates -- out of bounds
        Coordinate q, r;
        q = new Coordinate(0, 6, 0);
        r = new Coordinate(0, -1, 0);

        // Even
        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = even.getOverbounds(a);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = even.getOverbounds(b);
        assertEquals(expected, actual);


        expected = new Coordinate(0, 3, Flags.VECTOR);
        actual = even.getOverbounds(q);
        assertEquals(expected, actual);

        expected = new Coordinate(0, -1, Flags.VECTOR);
        actual = even.getOverbounds(r);
        assertEquals(expected, actual);

        // Odd
        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = odd.getOverbounds(a);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = odd.getOverbounds(b);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 2, Flags.VECTOR);
        actual = odd.getOverbounds(q);
        assertEquals(expected, actual);

        expected = new Coordinate(0, -1, Flags.VECTOR);
        actual = odd.getOverbounds(r);
        assertEquals(expected, actual);
    }

    public void testDimensions() {
        int[] expected, actual;

        // Odd
        expected = new int[]{5};
        actual = odd.getDimensions();
        assertArraysEqual(expected, actual, false);

        // Even
        expected = new int[]{4};
        actual = even.getDimensions();
        assertArraysEqual(expected, actual, false);
    }

    public void testCloneAtScale() {
        Lattice clonedLattice = evenLattice.clone();
        Shape cloned = even.cloneAtScale(clonedLattice, 2.0);

        assertEquals(even.getClass(), cloned.getClass());
        assertEquals(4, even.getCanonicalSites().length);
        assertEquals(8, cloned.getCanonicalSites().length);
    }
}