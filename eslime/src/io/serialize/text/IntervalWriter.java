/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.text;

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.serialize.Serializer;
import layers.LayerManager;
import processes.StepState;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IntervalWriter extends Serializer {

    private final String INTERVAL_FILENAME = "interval.txt";
    // I/O handle for the interval file (What changed at each time step, and how long it took)
    private BufferedWriter intervalWriter;
    private long prevTime;

    public IntervalWriter(GeneralParameters p, LayerManager lm) {
        super(p, lm);


    }

    public void init() {
        super.init();
        String intervalFileStr = p.getInstancePath() + '/' + INTERVAL_FILENAME;

        try {
            File intervalFile = new File(intervalFileStr);
            FileWriter ifw = new FileWriter(intervalFile);
            intervalWriter = new BufferedWriter(ifw, 1048576);
            intervalWriter.append("Step,Gillespie,Running time\n");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        prevTime = System.currentTimeMillis();
    }

    @Override
    public void flush(StepState stepState) {
        Long interval = System.currentTimeMillis() - prevTime;
        interval(stepState.getFrame(), stepState.getTime(), interval);

        prevTime = System.currentTimeMillis();
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        try {
            intervalWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wall clock time and simulation time for last time step.
     */
    private void interval(int n, double gillespie, long interval) {
        StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append(',');
        sb.append(gillespie);
        sb.append(',');
        sb.append(interval);
        sb.append('\n');
        try {
            intervalWriter.append(sb.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }
}
