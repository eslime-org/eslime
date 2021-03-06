/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.glyph;

import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Absorbing;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import io.deserialize.MockCoordinateDeindexer;
import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import io.visual.highlight.HighlightManager;
import io.visual.VisualizationProperties;
import io.visual.map.MapVisualization;
import layers.LightweightSystemState;
import layers.SystemState;
import test.EslimeTestCase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dbborens on 4/4/14.
 */
public class BullseyeGlyphTest extends EslimeTestCase {

    Geometry geometry;
    private HighlightManager highlightManager;
    private MapVisualization map;
    private SystemState systemState;

    @Override
    protected void setUp() throws Exception {
        geometry = makeGeometry();

        // Create 10x10 triangular lattice.
        ColorManager colorManager = new DefaultColorManager();

        // Create a 10 x 10 hexagonal map.
        VisualizationProperties mapState = new VisualizationProperties(colorManager, 50, 1);

        // Create highlight manager.
        highlightManager = new HighlightManager();
        mapState.setHighlightManager(highlightManager);

        // Channel 0 has a small glyph.
        Color offRed = Color.decode("0xFF2020");
        Glyph small = new BullseyeGlyph(offRed, Color.WHITE, 0.3);
        highlightManager.setGlyph(0, small);

        // Create map visualization.
        map = new MapVisualization(mapState);
        map.init(geometry, null, null);

        // Create system state
        systemState = makeSystemState();

    }

    private LightweightSystemState makeSystemState() {
        MockCoordinateDeindexer deindexer = new MockCoordinateDeindexer();
        deindexer.setUnderlying(geometry.getCanonicalSites());

        int n = makeGeometry().getCanonicalSites().length;

        double[] health = new double[n];
        int[] state = new int[n];

        for (int i = 0; i < n; i++) {
            health[i] = 0;
            state[i] = 0;
        }

        LightweightSystemState ret = new LightweightSystemState(geometry);
        ret.initCellLayer(state, health);

        Set<Coordinate> highlights = new HashSet<>();
        for (Coordinate c : geometry.getCanonicalSites()) {
            highlights.add(c);
        }
        ret.setHighlights(0, highlights);
        ret.setTime(0.0);
        ret.setFrame(0);

        return ret;
    }

    private Geometry makeGeometry() {
        Lattice lattice = new TriangularLattice();
        Shape shape = new Rectangle(lattice, 10, 10);
        Boundary boundary = new Absorbing(shape, lattice);
        Geometry geometry = new Geometry(lattice, shape, boundary);
        return geometry;
    }

    public void testOverlay() throws Exception {
        // Render the frame.
        BufferedImage result = map.render(systemState);

        File file = new File(outputPath + "bullseyeGlyph.png");
        ImageIO.write(result, "png", file);

        assertBinaryFilesEqual("glyphs/bullseyeGlyph.png", "bullseyeGlyph.png");
    }
}
