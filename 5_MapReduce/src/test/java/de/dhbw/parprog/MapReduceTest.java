package de.dhbw.parprog;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class MapReduceTest {
    @Test
    public void calculationReturnsCorrectResult() {
        MapReduce mapReduce = new MapReduce();
        CalcResult result = mapReduce.doAnalysis().join();
        assertThat(result.maleCount).isEqualTo(5);
        assertThat(result.maxLen).isEqualTo(12);
        assertThat(result.avgAge).isEqualTo(52.0);
    }
}
