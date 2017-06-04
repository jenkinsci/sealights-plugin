package io.sealights.plugins.sealightsjenkins.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Ronis on 6/4/2017.
 */
public class TimeUtilsTest {
    @Test
    public void toStringDuration_moreThen24_shouldShow36(){
        //Arrange
        long millis = 36*60*60*1000;
        String expected = "36:00:00";

        //Act
        String actual = TimeUtils.toStringDuration(millis);

        //Assert
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void toStringDuration_secondsMoreThen60_shouldIncrementMinutes(){
        //Arrange
        long millis = 36*60*60*1000+67*1000;
        String expected = "36:01:07";

        //Act
        String actual = TimeUtils.toStringDuration(millis);

        //Assert
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void toStringDuration_minutesMoreThen60_shouldIncrementHours(){
        //Arrange
        long millis = 36*60*60*1000+65*60*1000;
        String expected = "37:05:00";

        //Act
        String actual = TimeUtils.toStringDuration(millis);

        //Assert
        Assert.assertEquals(expected,actual);
    }
}
