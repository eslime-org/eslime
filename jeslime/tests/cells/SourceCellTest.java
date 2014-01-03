package cells;

import java.util.HashMap;

/**
 * Created by dbborens on 1/2/14.
 */
public class SourceCellTest extends CellTest {

    private SourceCell a, b;

    @Override
    public void setUp() {
        HashMap<String, Double> aProduction = new HashMap<>();
        aProduction.put("alpha", 1.0);
        aProduction.put("beta", 2.0);

        HashMap<String, Double> bProduction = new HashMap<>();
        aProduction.put("beta", 0.5);

        a = new SourceCell(1, aProduction);
        b = new SourceCell(2, bProduction);
    }

    @Override
    public void testGetState() throws Exception {
        assertEquals(1, a.getState());
        assertEquals(2, b.getState());
    }

    @Override
    public void testGetFitness() throws Exception {
        assertEquals(0.0, a.getFitness(), epsilon);
        assertEquals(0.0, b.getFitness(), epsilon);
    }

    @Override
    public void testIsDivisible() throws Exception {
        // SourceCells are never divisible
        assertFalse(a.isDivisible());
        assertFalse(b.isDivisible());
    }

    @Override
    public void testFeedConsiderApply() throws Exception {
        // Feed, consider and apply should have no effect on SourceCells
        int result = a.consider();
        assertEquals(1, result);
    }

    @Override
    public void testDivide() throws Exception {
        boolean thrown = false;
       try {
           a.divide();
       } catch (Exception e) {
           thrown = true;
       }

        assertTrue(thrown);
    }

    @Override
    public void testClone() throws Exception {
        fail("implement me");
    }

    @Override
    public void testGetProduction() throws Exception {
        fail("implement me");
    }
}
