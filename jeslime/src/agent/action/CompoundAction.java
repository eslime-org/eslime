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
import layers.LayerManager;
import structural.identifiers.Coordinate;

/**
 * An action that calls other actions on run.
 *
 * @see agent.Behavior
 */
public class CompoundAction extends Action {

    protected final BehaviorCell callback;
    protected final LayerManager layerManager;

    // Each action in the actionSequence array is fired,
// in order, when the trigger(...) method is invoked.
    protected final Action[] actionSequence;

    protected LayerManager getLayerManager() {
        return layerManager;
    }

    public BehaviorCell getCallback() {
        return callback;
    }

    public CompoundAction(BehaviorCell callback, LayerManager layerManager, Action[] actionSequence) {
        super(callback, layerManager);
        this.callback = callback;
        this.layerManager = layerManager;
        this.actionSequence = actionSequence;
    }

    public void run(Coordinate caller) {
        for (Action action : actionSequence) {
            action.run(caller);
        }
    }

    /**
     * Behaviors are equal if and only if their action sequences
     * consist of an equivalent list of actions.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        // The object should be a behavior.
        if (!(obj instanceof CompoundAction)) {
            return false;
        }

        CompoundAction other = (CompoundAction) obj;

        // The object should have the same number of actions.
        if (other.getActionSequence().length != this.getActionSequence().length) {
            return false;
        }

        // Each action should be equivalent, and in the correct order.
        for (int i = 0; i < getActionSequence().length; i++) {
            Action p = this.getActionSequence()[i];
            Action q = other.getActionSequence()[i];
            if (!actionsEqual(p, q)) {
                return false;
            }
        }

        // The behaviors are equivalent.
        return true;
    }

    private boolean actionsEqual(Action p, Action q) {
        if (p == null && q == null) {
            return true;
        }

        if (p == null && q != null) {
            return false;
        }

        if (q == null && p != null) {
            return false;
        }

        if (!p.equals(q)) {
            return false;
        }

        return true;
    }

    protected Action[] getActionSequence() {
        return actionSequence;
    }

    public CompoundAction clone(BehaviorCell child) {
        Action[] clonedActionSequence = cloneActionSequence(child);
        CompoundAction clone = new CompoundAction(child, layerManager, clonedActionSequence);
        return clone;
    }

    protected Action[] cloneActionSequence(BehaviorCell child) {
        int n = actionSequence.length;
        Action[] clonedActionSequence = new Action[n];
        for (int i = 0; i < n; i++) {
            Action action = actionSequence[i];
            Action clonedAction = action.clone(child);
            clonedActionSequence[i] = clonedAction;
        }

        return clonedActionSequence;
    }
}
