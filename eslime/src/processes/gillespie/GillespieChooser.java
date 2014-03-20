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

package processes.gillespie;

import structural.RangeMap;

/**
 * @author dbborens
 * @tested GillespieTest.java
 */
public class GillespieChooser {

    private RangeMap<Integer> chooser;

    public GillespieChooser(GillespieState state) {
        chooser = new RangeMap<>();

        for (Integer processId : state.getKeys()) {
            double weight = state.getWeight(processId);
            chooser.add(processId, weight);
        }
    }

    /**
     * Returns a particular process based on an input between 0
     * and the total weight, with processes sorted numerically
     * by ID number.
     * <p/>
     * So if w_i is the weight of process i, then you'll get back
     * <p/>
     * 0 <= x < w_1         --> return process 1's id
     * w_1 <= x < w_1 + w_2 --> return process 2's id
     * ...
     * w_n-1 <= x < sum(w)  --> return process n's id
     *
     * @return
     */
    public Integer selectTarget(double x) {
        return chooser.selectTarget(x);
    }
}