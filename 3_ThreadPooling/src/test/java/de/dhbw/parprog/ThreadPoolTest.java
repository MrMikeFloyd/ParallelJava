package de.dhbw.parprog;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class ThreadPoolTest {
    @Test
    public void calculationReturnsCorrectResult() {
        ThreadPool test = new ThreadPool();
        assertThat(test.doCalculation()).isEqualTo(10*42);
    }	
}
