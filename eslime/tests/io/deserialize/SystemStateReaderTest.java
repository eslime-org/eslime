/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package io.deserialize;

import cells.MockCell;
import io.serialize.Serializer;
import io.serialize.binary.ContinuumStateWriter;
import io.serialize.binary.HighlightWriter;
import io.serialize.binary.TimeWriter;
import io.serialize.text.CoordinateIndexer;
import io.serialize.text.LegacyCellStateWriter;
import layers.LightweightSystemState;
import layers.MockSoluteLayer;
import no.uib.cipr.matrix.DenseVector;
import structural.MockGeneralParameters;
import structural.identifiers.Coordinate;
import structural.postprocess.SolutionViewer;
import test.EslimeLatticeTestCase;

/**
 * Test for the SystemStateReader. As an I/O orchestrator whose main function
 * is to open a bunch of files, this is an annoying class to mock. Ultimately,
 * I decided (for the time being) to use existing mocks, some of which are
 * incompatible. As a result, this is a rather cursory test. If I start running
 * into problems surrounding deserialization, I will rewrite the tests for this
 * class.
 * <p/>
 * Created by dbborens on 3/28/14.
 */
public class SystemStateReaderTest extends EslimeLatticeTestCase {
    private SystemStateReader query;
    private String[] soluteIds;
    private int[] channelIds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String path = fixturePath + "SystemStateReader/";

        soluteIds = new String[]{"0"};
        channelIds = new int[]{0};

        query = new SystemStateReader(soluteIds, channelIds, path);
    }

    /**
     * The hasNext() function determines its value from the time file. If
     * the the other files disagree with this file, an error will occur.
     *
     * @throws Exception
     */
    public void testHasNext() throws Exception {
        // There are two frames specified in the time fixture, so we expect
        // that hasNext() will return true twice, and then return false the
        // third time.
        assertTrue(query.hasNext());

        query.next();
        assertTrue(query.hasNext());

        query.next();
        assertFalse(query.hasNext());
    }

    public void testNext() throws Exception {
        LightweightSystemState state = query.next();

        // Check solute state
        assertEquals(1.0, state.getValue("0", origin), epsilon);

        // Check cell state
        assertEquals(5, state.getState(x));
        assertEquals(2.0, state.getFitness(x), epsilon);

        // Empty cells should be 0 and 0.0
        assertEquals(0, state.getState(origin));
        assertEquals(0.0, state.getFitness(origin), epsilon);

        // Check time and frame
        assertEquals(2, state.getFrame());
        assertEquals(1.7, state.getTime(), epsilon);

        // Check highlighting
        assertTrue(state.isHighlighted(0, x));
        assertFalse(state.isHighlighted(0, y));
    }

    /**
     * Create the fixture files used in this test.
     */
    @SuppressWarnings("unused")
    private void generateFixtures() {

        Serializer[] serializers = makeSerializerArray();

        /* Populate the system */
        MockSoluteLayer layer0 = initializeSoluteLayer("0");
        layerManager.addSoluteLayer("0", layer0);

        pushState(layer0, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});

        placeCell(x, 2.0, 5);
        placeCell(y, 1.0, 3);

        Coordinate[] highlights = new Coordinate[]{x};

        /* Initialize output and push first state */
        for (Serializer serializer : serializers) {
            serializer.init(layerManager);
            serializer.step(highlights, 1.7, 2);
        }

        /* Set up second state */
        layer.getUpdateManager().banish(x);
        highlights = new Coordinate[]{y};
        pushState(layer0, new double[]{0.1, 0.2, 0.3, 0.4, 0.5});

        /* Push second state and close fixture */
        for (Serializer serializer : serializers) {
            serializer.step(highlights, 4.8, 6);
            serializer.dispatchHalt(null);
        }
    }

    private Serializer[] makeSerializerArray() {
        MockGeneralParameters p = makeMockGeneralParameters();
        p.setIsFrameValue(true);
        Serializer[] ret = new Serializer[]{
                new CoordinateIndexer(p),
                new TimeWriter(p),
                new ContinuumStateWriter(p),
                new LegacyCellStateWriter(p),
                new HighlightWriter(p)
        };

        return ret;
    }

    private MockSoluteLayer initializeSoluteLayer(String id) {
        MockSoluteLayer ret = new MockSoluteLayer();
        ret.setGeometry(geom);
        ret.setId(id);
        return ret;
    }

    private MockCell placeCell(Coordinate coord, double fitness, int state) {
        MockCell cell = new MockCell();
        cell.setFitness(fitness);
        cell.setState(state);
        layer.getUpdateManager().place(cell, coord);

        return cell;
    }

    private void pushState(MockSoluteLayer layer, double[] state) {
        DenseVector vector = new DenseVector(state);
        SolutionViewer viewer = new SolutionViewer(vector, geom);
        layer.push(viewer);
    }
}