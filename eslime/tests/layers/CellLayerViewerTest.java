/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers;

import cells.Cell;
import cells.MockCell;
import control.identifiers.Coordinate;
import geometry.MockGeometry;
import layers.cell.CellIndex;
import layers.cell.CellLayerViewer;
import layers.cell.MockCellLayerContent;
import layers.cell.MockCellLayerIndices;
import test.EslimeTestCase;

public class CellLayerViewerTest extends EslimeTestCase {

    CellLayerViewer query;
    MockCellLayerContent content;
    MockCellLayerIndices indices;
    MockGeometry geom;
    Coordinate c1, c2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        indices = new MockCellLayerIndices();

        c1 = new Coordinate(1, 0, 0);
        c2 = new Coordinate(5, 0, 0);

        geom = new MockGeometry();
        Coordinate[] cc = new Coordinate[]{c1, c2};
        geom.setCanonicalSites(cc);
        content = new MockCellLayerContent(geom, indices);

        query = new CellLayerViewer(content);
    }

    public void testGetOccupiedSites() {
        CellIndex expected = new CellIndex();

        expected.add(c1);
        expected.add(c2);

        indices.setOccupiedSites(expected);

        assertEquals(query.getOccupiedSites().size(), expected.size());
        assertTrue(query.getOccupiedSites().contains(c1));
        assertTrue(query.getOccupiedSites().contains(c2));
    }

    public void testGetDivisibleSites() {
        CellIndex expected = new CellIndex();

        expected.add(c1);
        expected.add(c2);

        indices.setDivisibleSites(expected);

        assertEquals(query.getDivisibleSites().size(), expected.size());
        assertTrue(query.getDivisibleSites().contains(c1));
        assertTrue(query.getDivisibleSites().contains(c2));
    }


    public void testGetCell() throws Exception {

        Cell cell = new MockCell();
        content.put(c1, cell);
        assertEquals(cell, query.getCell(c1));
    }

    public void testGetHealthVector() {
        double[] healthes = new double[]{0.1, 0.2};
        content.setHealthVector(healthes);
        assertArraysEqual(healthes, query.getHealthVector(), false);
    }

    public void testGetStateVector() {
        int[] states = new int[]{1, 2};
        content.setStateVector(states);
        assertArraysEqual(states, query.getStateVector(), false);
    }

    public void testIsOccupied() {
        CellIndex occupiedSites = new CellIndex();
        Coordinate c1 = new Coordinate(1, 0, 0);
        Coordinate c2 = new Coordinate(5, 0, 0);

        occupiedSites.add(c1);

        indices.setOccupiedSites(occupiedSites);
        assertTrue(query.isOccupied(c1));
        assertFalse(query.isOccupied(c2));
    }

    public void testIsDivisible() {
        CellIndex divisibleSites = new CellIndex();
        Coordinate c1 = new Coordinate(1, 0, 0);
        Coordinate c2 = new Coordinate(5, 0, 0);

        divisibleSites.add(c1);

        indices.setDivisibleSites(divisibleSites);
        assertTrue(query.isDivisible(c1));
        assertFalse(query.isDivisible(c2));
    }

    public void getStateVacant() {
        fail("Not yet implemented");
    }

    public void getStateNonVacant() {
        fail("Not yet implemented");
    }
}
