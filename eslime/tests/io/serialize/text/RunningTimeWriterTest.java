/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.text;

import control.GeneralParameters;
import processes.StepState;
import test.EslimeLatticeTestCase;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Since the running time writer actuall tracks wall clock time, it is
 * impossible to make a deterministic test. The only thing we can say
 * for certain is that a minimum time has elapsed, which we can control
 * by sleeping for a certain number of miliseconds.
 */
public class RunningTimeWriterTest extends EslimeLatticeTestCase {
    public void testLifeCycle() throws Exception {
        runWithPauses();
        verifyFile();
    }

    private void verifyFile() throws Exception {
        String fn = outputPath + '/' + "runtime.txt";
        FileReader mfr = new FileReader(fn);
        BufferedReader mbr = new BufferedReader(mfr);
        String next = mbr.readLine().trim();
        checkLine(next, 0, 5.0);
        next = mbr.readLine().trim();
        checkLine(next, 1, 10.0);
    }

    private void checkLine(String next, int expectedFrame, double minimumRuntime) {
        // Lines are in the form
        // 		1   11.0
        //      2   23.4
        String[] mapping = next.split("\t");
        int frame = Integer.valueOf(mapping[0]);
        double runtime = Double.valueOf(mapping[1]);
        assertEquals(expectedFrame, frame);
        assertTrue(runtime >= minimumRuntime);
    }

    private void runWithPauses() throws Exception {
        GeneralParameters p = makeMockGeneralParameters();
        RunningTimeWriter writer = new RunningTimeWriter(p, layerManager);
        writer.init();
        StepState state = new StepState(0.0, 0);

        Thread.sleep(5);
        writer.flush(state);

        state = new StepState(1.0, 1);
        Thread.sleep(10);
        writer.flush(state);

        writer.dispatchHalt(null);
        writer.close();
    }
}