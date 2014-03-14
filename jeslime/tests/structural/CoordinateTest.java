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

package structural;

import junit.framework.TestCase;
import structural.identifiers.Coordinate;

import java.util.HashSet;


public class CoordinateTest extends TestCase {

    public void testConstructors2D() {
        // Construct a 3D coordinate.
        Coordinate first = new Coordinate(2, 4, 6, 0);

        // Check values and dimensionality flag.
        assertEquals(first.x(), 2);
        assertEquals(first.y(), 4);
        assertEquals(first.z(), 6);
        assertEquals(first.flags(), 0);

        // Copy that coordinate into another coordinate.
        Coordinate second = first;

        // Check overloaded equality operator.
        assertEquals(first, second);

        // Replace one of the coordinates with something different.
        second = new Coordinate(3, 6, 9, 0);

        // Check values of new coordinate.
        assertEquals(second.x(), 3);
        assertEquals(second.y(), 6);
        assertEquals(second.z(), 9);

        // Verify non-equality.
        assertFalse(first.equals(second));
    }

    public void testConstructors3D() {
        // Construct a 2D coordinate.
        Coordinate first = new Coordinate(2, 4, 0);

        // Check values and dimensionality flag.
        assertEquals(first.x(), 2);
        assertEquals(first.y(), 4);
        assertEquals(Flags.PLANAR, first.flags());

        // Copy that coordinate into another coordinate.
        Coordinate second = first;

        // Check overloaded equality operator.
        assertEquals(first, second);

        // Replace one of the coordinates with something different.
        second = new Coordinate(3, 6, 0);

        // Check values of new coordinate.
        assertEquals(second.x(), 3);
        assertEquals(second.y(), 6);
        assertEquals(second.flags(), Flags.PLANAR);

        // Verify non-equality.
        assertFalse(first.equals(second));
    }

    public void testFlags() {
        // Create a coordinate with a couple of flags.
        Coordinate first = new Coordinate(2, 4, Flags.BOUNDARY_APPLIED | Flags.BEYOND_BOUNDS);

        // Verify that hasFlag(...) works for each.
        assertEquals(first.flags(), Flags.BOUNDARY_APPLIED | Flags.BEYOND_BOUNDS | Flags.PLANAR);
        assertTrue(first.hasFlag(Flags.BOUNDARY_APPLIED));
        assertTrue(first.hasFlag(Flags.BEYOND_BOUNDS));
        assertTrue(first.hasFlag(Flags.PLANAR));

        // Create a second coordinate at same location but without flags.
        Coordinate second = new Coordinate(2, 4, 0);
        assertEquals(second.flags(), Flags.PLANAR);

        // Verify non-equality.
        assertFalse(first.equals(second));
        ;
    }

    public void testStrings() {
        // Create a 2D coordinate.
        Coordinate first = new Coordinate(2, 4, 0);

        // EXPECT_STREQ, but it was acting weird -- done is better than perfect
        // Verify expected string form.

        assertEquals("(2, 4 | 1)", first.stringForm());

        // Verify expected vector form.
        Coordinate firstVector = first.addFlags(Flags.VECTOR);
        assertEquals("<2, 4>", firstVector.stringForm());

        // Create a 3D coordinate.
        Coordinate second = new Coordinate(2, 4, 6, 0);

        // Verify expected string form.
        assertEquals("(2, 4, 6 | 0)", second.stringForm());

        // Verify expected vector form.
        Coordinate secondVector = second.addFlags(Flags.VECTOR);
        assertEquals("<2, 4, 6>", secondVector.stringForm());

    }

    public void testHashing() {
        // Create two logically equivalent coordinates.
        Coordinate first = new Coordinate(2, 4, 0);
        Coordinate second = new Coordinate(2, 4, 0);

        // Note that GoogleTest's assertEquals does not use the equality operator,
        // which is weird.

        assertEquals(first, second);

        // Create a logically different coordinate.
        Coordinate third = new Coordinate(3, 6, 0);
        assertFalse(second.equals(third));

        // Adding two logically different coordinates to a set should be fine.
        HashSet<Coordinate> coords = new HashSet<Coordinate>();
        ;
        assertEquals(coords.size(), 0);

        coords.add(first);
        assertEquals(coords.size(), 1);

        coords.add(third);
        assertEquals(coords.size(), 2);


        // Adding a coordinate that is logically identical to one already present
        // shouldn't add anything
        coords.add(second);
        assertEquals(coords.size(), 2);
    }

    public void testAddFlags() {
        Coordinate c = new Coordinate(1, 2, 3, Flags.END_OF_WORLD);
        Coordinate d = c.addFlags(Flags.BOUNDARY_APPLIED);

        assertFalse(c.hasFlag(Flags.BOUNDARY_APPLIED));
        assertEquals(c.flags() | Flags.BOUNDARY_APPLIED, d.flags());
    }

    public void testNorm() {
        Coordinate c = new Coordinate(0, 0, 0, 0);
        assertEquals(0, c.norm());

        c = new Coordinate(1, 0, 0);
        assertEquals(1, c.norm());

        c = new Coordinate(-2, 2, 5);
        assertEquals(4, c.norm());
    }

    public void testClone() {
        Coordinate c = new Coordinate(1, 2, 3, 4);
        Coordinate d = c.clone();

        // Memory addresses should be different
        assertFalse(c == d);

        // Objects should be identical
        assertEquals(c, d);
    }

    public void testCanonicalize() {
        int flags = Flags.BEYOND_BOUNDS | Flags.BOUNDARY_APPLIED | Flags.BOUNDARY_IGNORED | Flags.END_OF_WORLD;

        Coordinate a = new Coordinate(0, 0, flags);
        Coordinate b = new Coordinate(0, 0, 0, flags);

        Coordinate a1 = a.canonicalize();
        Coordinate b1 = b.canonicalize();

        assertEquals(Flags.PLANAR, a1.flags());
        assertEquals(0, b1.flags());

    }
}
