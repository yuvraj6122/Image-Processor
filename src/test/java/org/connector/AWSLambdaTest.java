package org.connector;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AWSLambdaTest {
    private AWSLambda awsLambda;
    @Test
    public void test_handleRequest(){
        awsLambda = new AWSLambda();
        String s = awsLambda.handleRequest("test", null);
        assertEquals(s, "TEST");
    }
}