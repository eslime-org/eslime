/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.glyph;

import control.identifiers.Coordinate;

import java.awt.*;

/**
 * Created by dbborens on 4/3/14.
 */
public class CrosshairsGlyph extends Glyph {

    private int radius;
    private int cross;
    private Color color;
    private double circleSize;
    private double crossSize;

    /**
     * @param color      The color of the crosshairs.
     * @param circleSize The relative size of the circle, specified as a multiple of the edge size.
     * @param crossSize  The relative size of the cross, specified as a multiple of the edge size.
     */
    public CrosshairsGlyph(Color color, double circleSize, double crossSize) {
        this.color = color;
        this.circleSize = circleSize;
        this.crossSize = crossSize;
    }

    @Override
    protected void internalInit() {
        radius = calcProportionalSize(circleSize);
        cross = calcProportionalSize(circleSize * crossSize * 2);
    }

    @Override
    public void overlay(Coordinate target, int frame, double time) {
        // First, get the center of the cell (in pixels).
        Coordinate center = translator.resolve(target, frame, time);

        graphics.setColor(color);

        drawCircle(center);
        drawHorizontalLine(center);
        drawVerticalLine(center);
    }

    private void drawHorizontalLine(Coordinate center) {
        int x = center.x() - (cross / 2);
        int y = center.y();

        graphics.drawLine(x, y, x + cross, y);
    }

    private void drawVerticalLine(Coordinate center) {
        int y = center.y() - (cross / 2);
        int x = center.x();

        graphics.drawLine(x, y, x, y + cross);
    }

    private void drawCircle(Coordinate center) {
        int x = center.x() - radius;
        int y = center.y() - radius;

        graphics.drawOval(x, y, radius * 2, radius * 2);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CrosshairsGlyph)) {
            return false;
        }

        CrosshairsGlyph other = (CrosshairsGlyph) obj;
        if (radius != other.radius) {
            return false;
        }

        if (!color.equals(other.color)) {
            return false;
        }

        return true;
    }
}

