/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package layers.continuum;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import test.LinearMocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class ContinuumLayerSchedulerTest extends LinearMocks {

    private ScheduledOperations so;
    private HoldManager holdManager;
    private ContinuumLayerScheduler query;
    private ArgumentCaptor<Runnable> runArgument;

    @Before
    public void init() throws Exception {
        so = new ScheduledOperations(indexer, 3);
        holdManager = mock(HoldManager.class);
        runArgument = ArgumentCaptor.forClass(Runnable.class);

        query = new ContinuumLayerScheduler(so, holdManager);
    }

    @Test
    public void apply() throws Exception {
        DenseMatrix matrix = matrix(1.0, 2.0, 3.0);
        query.apply(matrix);
        runRunnable();
        checkMatrix(so.getOperator(), 2.0, 3.0, 4.0);
    }

    @Test
    public void exp() throws Exception {
        query.exp(a, 1.0);
        runRunnable();
        checkMatrix(so.getOperator(), 2.0, 1.0, 1.0);
    }

    @Test
    public void injectScalar() throws Exception {
        query.inject(a, 1.0);
        runRunnable();
        checkVector(so.getSource(), 1.0, 0.0, 0.0);
    }

    @Test
    public void injectVector() throws Exception {
        DenseVector vector = vector(1.0, 2.0, 3.0);
        query.inject(vector);
        runRunnable();
        assertVectorsEqual(vector, so.getSource(), epsilon);
    }

    @Test
    public void resetCallsHoldManager() throws Exception {
        query.reset();
        verify(holdManager).reset();
    }

    @Test
    public void holdCallsHoldManager() throws Exception {
        query.hold();
        verify(holdManager).hold();
    }

    @Test
    public void releaseCallsHoldManager() throws Exception {
        query.release();
        verify(holdManager).release();
    }

    @Test
    public void resetCallsScheduledOperations() throws Exception {
        // SO is not a mock, so we have to do this the hard way
        so.inject(vector(1.0, 1.0, 1.0));
        query.reset();
        checkVector(so.getSource(), 0.0, 0.0, 0.0);
    }

    @Test
    public void getLinkerAsksHoldManager() throws Exception {
        ContinuumAgentLinker linker = mock(ContinuumAgentLinker.class);
        when(holdManager.getLinker(any())).thenReturn(linker);
        assertEquals(linker, query.getLinker(null));
    }

    @Test
    public void getIdAsksHoldManager() throws Exception {
        when(holdManager.getId()).thenReturn("test");
        assertEquals("test", query.getId());
    }

    @Test
    public void solveCallsHoldManager() throws Exception {
        query.solve();
        verify(holdManager).solve();
    }

    private void runRunnable() {
        verify(holdManager).resolve(runArgument.capture());
        Runnable runnable = runArgument.getValue();
        runnable.run();
    }

}