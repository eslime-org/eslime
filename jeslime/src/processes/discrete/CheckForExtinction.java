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

package processes.discrete;

import io.project.ProcessLoader;
import layers.LayerManager;
import layers.cell.StateMapViewer;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import structural.halt.ExtinctionEvent;
import structural.halt.HaltCondition;

/**
 * Checks for extinction or fixation events.
 * <p/>
 * Created by dbborens on 1/13/14.
 */
public class CheckForExtinction extends CellProcess {
    public CheckForExtinction(ProcessLoader loader, LayerManager layerManager, int id, GeneralParameters p) {
        super(loader, layerManager, id, p);
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There's only one event that can happen in this process.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        StateMapViewer smv = layer.getViewer().getStateMapViewer();

        if (smv.getStates().length == 0) {
            throw new ExtinctionEvent(state.getTime());
        }
    }
}