package edu.nanoracket.npr.util;

import junit.framework.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void testConvertString(){
        String result = "hello";
        String input = "#12 - hello#34:";
        Assert.assertEquals(result, StringUtils.convertString(input));
    }

    @Test
    public void testConvertStoryTitle(){
        String result = "hello_world_";
        String input = "hello world!";
        Assert.assertEquals(result, StringUtils.convertStoryTitle(input));
    }
}
