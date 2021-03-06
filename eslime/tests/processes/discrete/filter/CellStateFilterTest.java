/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete.filter;

import cells.Cell;
import cells.MockCell;
import control.arguments.ConstantInteger;
import control.identifiers.Coordinate;
import layers.cell.CellUpdateManager;
import test.EslimeLatticeTestCase;

import java.util.*;

public class CellStateFilterTest extends EslimeLatticeTestCase {
    private Cell yes, no;
    private CellStateFilter query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        yes = new MockCell(1);
        no = new MockCell(2);

        CellUpdateManager u = cellLayer.getUpdateManager();

        u.place(yes, x);
        u.place(no, y);

        query = new CellStateFilter(cellLayer, new ConstantInteger(1));
    }

    public void testLifeCycle() throws Exception {
        List<Coordinate> cc = Arrays.asList(geom.getCanonicalSites());
        List<Coordinate> ccCopy = new ArrayList<>(cc);

        // Apply filter.
        List<Coordinate> actual = query.apply(cc);

        // Only "x" should be retained.
        List<Coordinate> expected = Arrays.asList(new Coordinate[] {x});
        assertTrue(collectionsEqual(expected, actual));

        // Original list should be unmodified
        assertTrue(collectionsEqual(cc, ccCopy));
    }

    private boolean collectionsEqual(Collection<Coordinate> p, Collection<Coordinate> q) {
        if (p.size() != q.size()) {
            return false;
        }

        Iterator<Coordinate> qIter = q.iterator();

        for (Coordinate pCoord : p) {
            Coordinate qCoord = qIter.next();

            if (pCoord != qCoord) {
                return false;
            }
        }

        return true;
    }
}