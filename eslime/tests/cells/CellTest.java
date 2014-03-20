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

package cells;

import test.EslimeTestCase;

/**
 * Created by dbborens on 1/2/14.
 */
public abstract class CellTest extends EslimeTestCase {
    public abstract void testGetState() throws Exception;

    public abstract void testGetFitness() throws Exception;

    public abstract void testIsDivisible() throws Exception;

    public abstract void testFeedConsiderApply() throws Exception;

    public abstract void testDivide() throws Exception;

    public abstract void testClone() throws Exception;

    public abstract void testGetProduction() throws Exception;
}