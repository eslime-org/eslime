/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.glyph;

import factory.io.visual.glyph.GlyphFactory;
import org.dom4j.Element;
import test.EslimeTestCase;

import java.awt.*;

/**
 * Created by dbborens on 4/4/14.
 */
public class GlyphFactoryTest extends EslimeTestCase {
    private Element root;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/GlyphFactoryTest.xml");
    }

    public void testMockCase() throws Exception {
        Element e = root.element("mock-case");
        Glyph actual = GlyphFactory.instantiate(e);
        Glyph expected = new MockGlyph();

        assertEquals(expected, actual);
    }

    public void testDotCompleteCase() throws Exception {
        Element e = root.element("dot-case-complete");
        Glyph actual = GlyphFactory.instantiate(e);

        Color color = Color.decode("0xFF00AA");
        double size = 0.4;
        Glyph expected = new DotGlyph(color, size);

        assertEquals(expected, actual);
    }

    /**
     * We test one class of glyph for default handling
     */
    public void testDotDefaultCase() throws Exception {
        Element e = root.element("dot-case-default");
        Glyph actual = GlyphFactory.instantiate(e);

        Glyph expected = new DotGlyph(Color.WHITE,
                0.1);

        assertEquals(expected, actual);
    }

    public void testBullseyeGlyphCase() throws Exception {
        Element e = root.element("bullseye-case");

        Glyph actual = GlyphFactory.instantiate(e);

        Color primary = Color.decode("0x001122");
        Color secondary = Color.decode("0x334455");
        Glyph expected = new BullseyeGlyph(primary,
                secondary, 0.3);

        assertEquals(expected, actual);
    }

    public void testCrosshairsCase() throws Exception {
        Element e = root.element("crosshairs-case");

        Glyph actual = GlyphFactory.instantiate(e);

        Color color = Color.decode("0xAABBCC");
        Glyph expected = new CrosshairsGlyph(color, 0.2, 2.0);

        assertEquals(expected, actual);
    }
}
