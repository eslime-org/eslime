/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize;

import control.halt.HaltCondition;
import layers.LayerManager;
import processes.StepState;

/**
 * Created by dbborens on 5/15/14.
 */
public class MockSerializer extends Serializer {
    private boolean isDispatchHalt;
    private boolean isClose;
    private boolean isInit;
    private boolean isFlush;

    public MockSerializer(LayerManager layerManager) {
        super(null, layerManager);

        isDispatchHalt = false;
        isClose = false;
        isInit = false;
        isFlush = false;
    }

    public boolean isDispatchHalt() {
        return isDispatchHalt;
    }

    public boolean isClose() {
        return isClose;
    }

    public boolean isInit() {
        return isInit;
    }

    public boolean isFlush() {
        return isFlush;
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        isDispatchHalt = true;
    }

    @Override
    public void close() {
        isClose = true;
    }

    @Override
    public void init() {
        isInit = true;
    }


    @Override
    public void flush(StepState stepState) {
        isFlush = true;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MockSerializer);
    }
}
