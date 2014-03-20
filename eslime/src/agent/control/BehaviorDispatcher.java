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

package agent.control;

import agent.Behavior;
import cells.BehaviorCell;
import io.project.BehaviorLoader;
import layers.LayerManager;
import org.dom4j.Element;
import structural.GeneralParameters;
import structural.identifiers.Coordinate;

import java.util.HashMap;

/**
 * BehaviorDispatcher is a map between behavior names and the
 * behaviors themselves. It is associated with a particular cell,
 * and can be used to trigger behaviors in that cell.
 * <p/>
 * Created by David B Borenstein on 1/21/14.
 */
public class BehaviorDispatcher {
    private BehaviorCell callback;
    private LayerManager layerManager;
    private HashMap<String, Behavior> behaviors;
    private GeneralParameters p;

    public BehaviorDispatcher() {
        behaviors = new HashMap<>();
    }

    public BehaviorDispatcher(BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        behaviors = new HashMap<>();
        this.layerManager = layerManager;
        this.callback = callback;
        this.p = p;
    }

    public BehaviorDispatcher(Element behaviorRoot, BehaviorCell callback, LayerManager layerManager, GeneralParameters p) {
        this.p = p;
        behaviors = new HashMap<>();
        BehaviorLoader loader = new BehaviorLoader(this, callback, layerManager, p);
        loader.loadAllBehaviors(behaviorRoot);

        this.layerManager = layerManager;
        this.callback = callback;
    }

    public void map(String name, Behavior behavior) {
        behaviors.put(name, behavior);
    }

    /**
     * Trigger a behavior associated with the cell.
     *
     * @param behaviorName
     * @param caller       The coordinate from which the call originated. If
     *                     the call originated with a top-down process, the
     *                     caller will be null.
     */
    public void trigger(String behaviorName, Coordinate caller) {
        if (!behaviors.containsKey(behaviorName)) {
            throw new IllegalArgumentException("Behavior '" + behaviorName + "' not found.");
        }

        Behavior behavior = behaviors.get(behaviorName);
        behavior.run(caller);
    }

    public BehaviorDispatcher clone(BehaviorCell child) {
        BehaviorDispatcher clone = new BehaviorDispatcher();

        // Clone the behavior catalog item for item.
        for (String behaviorName : behaviors.keySet()) {
            Behavior b = behaviors.get(behaviorName);
            Behavior bc = b.clone(child);
            clone.map(behaviorName, bc);
        }

        return clone;
    }

    /**
     * A BehaviorDispatcher is equal to another object only if:
     * (1) The other Object is a BehaviorDispatcher.
     * (2) Each Behavior in the other BehaviorDispatcher
     * has an equivalent Behavior mapped to the same name
     * as this BehaviorDispatcher.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BehaviorDispatcher)) {
            return false;
        }

        BehaviorDispatcher other = (BehaviorDispatcher) obj;

        if (other.behaviors.size() != this.behaviors.size()) {
            return false;
        }

        for (String behaviorName : behaviors.keySet()) {
            if (!other.behaviors.containsKey(behaviorName)) {
                return false;
            }

            Behavior otherBehavior = other.behaviors.get(behaviorName);
            Behavior thisBehavior = this.behaviors.get(behaviorName);

            if (!thisBehavior.equals(otherBehavior)) {
                return false;
            }
        }

        return true;
    }

    public Behavior getMappedBehavior(String behaviorName) {
        return behaviors.get(behaviorName);
    }

}