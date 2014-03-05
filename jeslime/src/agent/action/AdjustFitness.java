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

package agent.action;

import cells.BehaviorCell;
import cells.Cell;
import layers.LayerManager;
import structural.EpsilonUtil;
import structural.identifiers.Coordinate;

/**
 * Adjusts the agent's fitness by a predefined delta.
 *
 * Created by David B Borenstein on 2/5/14.
 */
public class AdjustFitness extends Action {

    private double delta;

    public AdjustFitness(BehaviorCell callback, LayerManager layerManager, double delta) {
        super(callback, layerManager);
        this.delta = delta;
    }

    @Override
    public void run(Coordinate caller) {
        BehaviorCell cell = getCallback();
        double curFitness = cell.getFitness();
        double adjFitness = curFitness + delta;
        cell.setFitness(adjFitness);

        // DEBUG CODE
        Coordinate self = getLayerManager().getCellLayer().getLookupManager().getCellLocation(cell);
        System.out.println("Adjusted cell at " + self + " from " + curFitness + " to " + adjFitness);
        System.out.println("   Is this cell now divisible? " + cell.isDivisible());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  AdjustFitness)) {
            return false;
        }

        AdjustFitness other = (AdjustFitness) obj;
        if (!EpsilonUtil.epsilonEquals(other.delta, this.delta)) {
            return false;
        }

        return true;
    }

    @Override
    public Action clone(BehaviorCell child) {
        AdjustFitness clone = new AdjustFitness(child, getLayerManager(), delta);
        return clone;
    }
}