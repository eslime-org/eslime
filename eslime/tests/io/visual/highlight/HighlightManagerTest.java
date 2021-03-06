/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.highlight;

import io.visual.glyph.MockGlyph;
import layers.MockSystemState;
import test.EslimeLatticeTestCase;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by dbborens on 4/2/14.
 */
public class HighlightManagerTest extends EslimeLatticeTestCase {

    MockGlyph glyph;
    HighlightManager query;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        glyph = new MockGlyph();
        query = new HighlightManager();
        query.setGlyph(0, glyph);
    }

    public void testSetGraphics() throws Exception {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        query.setGraphics(graphics);
        assertEquals(graphics, glyph.getGraphics());
    }


    public void testOverlayGlyphs() throws Exception {
        MockSystemState systemState = new MockSystemState();
        systemState.setHighlighted(true);
        query.render(origin, systemState);
        assertEquals(origin, glyph.getLastOverlaid());
    }

    public void testGetHighlightChannels() throws Exception {
        query.setGlyph(2, new MockGlyph());
        int[] expected = new int[]{0, 2};
        int[] actual = query.getHighlightChannels();
        assertArraysEqual(expected, actual, true);
    }
}
