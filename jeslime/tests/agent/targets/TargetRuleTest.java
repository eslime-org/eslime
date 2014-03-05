/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package agent.targets;

import cells.MockCell;
import geometry.MockGeometry;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

import java.util.Random;

/**
 * Created by dbborens on 2/10/14.
 */
public class TargetRuleTest extends EslimeTestCase {

    private MockGeometry geom;
    private MockLayerManager layerManager;
    private CellLayer cellLayer;
    private MockCell self, occupiedNeighbor;
    private Coordinate[] cc, neighbors;
    private Coordinate left, right, center;
    private Random random;
    @Override
    public void setUp() {
        // Restart RN generator
        random = new Random(RANDOM_SEED);

        geom = new MockGeometry();

        center = new Coordinate(1, 0, 0);
        left = new Coordinate(0, 0, 0);
        right = new Coordinate(2, 0, 0);

        cc = new Coordinate[] { center, left, right };

        geom.setCanonicalSites(cc);

        layerManager = new MockLayerManager();
        cellLayer = new CellLayer(geom, 0);
        layerManager.setCellLayer(cellLayer);

        occupiedNeighbor = new MockCell();
        self = new MockCell();

        // Only one neighbor is occupied; the other is not.
        cellLayer.getUpdateManager().place(self, center);
        cellLayer.getUpdateManager().place(occupiedNeighbor, left);

        // Associate the neighborhood with the coordinate
        neighbors = new Coordinate[] {left, right};
        geom.setCellNeighbors(center, neighbors);
    }

    public void testTargetAllNeighbors() {
        TargetRule query = new TargetAllNeighbors(self, layerManager, -1, random);

        // Get target list
        Coordinate[] actual = query.report(null);
        Coordinate[] expected = neighbors;

        // Should contain all neighbors
        assertArraysEqual(expected, actual, true);
    }

    public void testTargetVacantNeighbors() {
        TargetRule query = new TargetVacantNeighbors(self, layerManager, -1, random);

        // Get target list
        Coordinate[] actual = query.report(null);
        Coordinate[] expected = new Coordinate[] {right};

        // Should contain all neighbors
        assertArraysEqual(expected, actual, true);
    }

    public void testTargetOccupiedNeighbors() {
        TargetRule query = new TargetOccupiedNeighbors(self, layerManager, -1, random);

        // Get target list
        Coordinate[] actual = query.report(null);
        Coordinate[] expected = new Coordinate[] {left};

        // Should contain all neighbors
        assertArraysEqual(expected, actual, true);
    }

    public void testTargetSelf() {
        TargetRule query = new TargetSelf(self, layerManager, -1, random);

        // Get target list
        Coordinate[] actual = query.report(null);
        Coordinate[] expected = new Coordinate[] {center};

        // Should contain all neighbors
        assertArraysEqual(expected, actual, true);
    }

    public void testTargetCaller() {
        // Left caller
        TargetRule query = new TargetCaller(self, layerManager, -1, random);
        Coordinate[] actual = query.report(occupiedNeighbor);
        Coordinate[] expected = new Coordinate[] {left};
        assertArraysEqual(expected, actual, true);

    }

    // Null caller: should blow up
    public void testTargetCallerNull() {
        TargetRule query = new TargetCaller(self, layerManager, -1, random);
        boolean thrown = false;
        try {
            query.report(null);
        } catch (IllegalStateException ex) {
            thrown = true;
        } catch (NullPointerException ex) {
            fail();
        }

        assertTrue(thrown);
    }

    public void testEquality() {
        // Equality is defined at the superclass level, so one test is sufficient.

        TargetRule p, q, r;

        // Make two targeters of the same class, but with different callbacks
        p = new TargetSelf(new MockCell(), layerManager, -1, random);
        q = new TargetSelf(new MockCell(), layerManager, -1, random);

        // Make one targeter of a different class
        r = new TargetCaller(new MockCell(), layerManager, -1, random);

        // Test that the two of the same class are equal
        assertEquals(p, q);

        // Test that the two of different classes are not equal
        assertNotEquals(p, r);
    }

    public void testClone()  {
        MockCell parent = new MockCell();
        TargetRule[] rules = new TargetRule[] {
            new TargetAllNeighbors(parent, layerManager, -1, random),
            new TargetCaller(parent, layerManager, -1, random),
            new TargetOccupiedNeighbors(parent, layerManager, -1, random),
            new TargetSelf(parent, layerManager, -1, random),
            new TargetVacantNeighbors(parent, layerManager, -1, random)
        };

        for (TargetRule rule : rules) {
            doCloneTest(rule, parent);
        }
    }

    /*
      All other tests are based on a no-maximum scheme, so an additional
      testNoMaximum method is not necessary.
     */
    public void testMaximum() {
        TargetRule query = new TargetAllNeighbors(self, layerManager, 1, random);

        // Get target list
        Coordinate[] actual = query.report(null);
        Coordinate[] expected = new Coordinate[] {left};

        // Should contain all neighbors
        assertArraysEqual(expected, actual, true);
    }

    private void doCloneTest(TargetRule original, MockCell parent) {
        MockCell child = new MockCell();
        TargetRule cloned = original.clone(child);
        assertEquals(original, cloned);
        assertEquals(parent, original.getCallback());
        assertEquals(child, cloned.getCallback());
    }

}