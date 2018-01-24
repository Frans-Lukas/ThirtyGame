package frlo.umu.cs.se.thirty;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private ThirtyModel model;

    @Before
    public void setUp() throws Exception {
        model = new ThirtyModel();
    }

    @Test
    public void singleDiceCalculationIsCorrect() throws Exception {
        int dices[] = {
                6,6,6,6,6,6
        };
        model.setScoreMode(6);
        model.setDiceRoll(dices);
        model.calculateScore();
        assertEquals(36, model.getScore());
    }

    @Test
    public void sixAdditionCalculationIsCorrect() throws Exception {
        int dices[] = {
                1,1,1,1,1,1
        };
        model.setScoreMode(6);
        model.setDiceRoll(dices);
        model.calculateScore();
        assertEquals(6, model.getScore());
    }

    @Test
    public void fiveAdditionIsCorrect() throws Exception {
        int dices[] = {
                1,1,1,1,1,111
        };
        model.setScoreMode(5);
        model.setDiceRoll(dices);
        model.calculateScore();
        assertEquals(5, model.getScore());
    }

    @Test
    public void fourAdditionIsCorrect() throws Exception {
        int dices[] = {
                1,1,1,1,1111,111
        };
        model.setScoreMode(4);
        model.setDiceRoll(dices);
        model.calculateScore();
        assertEquals(4, model.getScore());
    }

    @Test
    public void threeAdditionIsCorrect() throws Exception {
        int dices[] = {
                1,1,1,1111,1111,111
        };
        model.setScoreMode(3);
        model.setDiceRoll(dices);
        model.calculateScore();
        assertEquals(3, model.getScore());
    }

    @Test
    public void additionalTestForDifferentValues() throws Exception {
        int dices[] = {
                1,1,1,11,10,3
        };
        model.setScoreMode(12);
        model.setDiceRoll(dices);
        model.calculateScore();
        assertEquals(24, model.getScore());
    }
}