/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

package processes.discrete;

import junit.framework.TestCase;
import processes.MockStepState;
import test.EslimeLatticeTestCase;

/**
 * Created by dbborens on 4/24/14.
 */
public class RecordTest extends EslimeLatticeTestCase {

    public void testLifeCycle() throws Exception {
        MockStepState stepState = new MockStepState();
        Record query = new Record(null, layerManager, 0, null);
        query.target(null);
        query.fire(stepState);
        assertTrue(stepState.isRecord());
    }
}