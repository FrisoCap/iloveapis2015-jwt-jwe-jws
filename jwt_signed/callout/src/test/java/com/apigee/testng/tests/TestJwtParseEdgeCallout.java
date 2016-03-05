package com.apigee.testng.tests;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.BeforeMethod;

import mockit.Mock;
import mockit.MockUp;

import com.apigee.flow.execution.ExecutionContext;
import com.apigee.flow.message.MessageContext;
import com.apigee.flow.execution.ExecutionResult;

import com.apigee.callout.jwtsigned.JwtParserCallout;

public class TestJwtParseEdgeCallout {

    MessageContext msgCtxt;
    ExecutionContext exeCtxt;

    @BeforeMethod()
    public void testSetup1() {

        msgCtxt = new MockUp<MessageContext>() {
            private Map variables;
            public void $init() {
                variables = new HashMap();
            }

            @Mock()
            public <T> T getVariable(final String name){
                if (variables == null) {
                    variables = new HashMap();
                }
                return (T) variables.get(name);
            }

            @Mock()
            public boolean setVariable(final String name, final Object value) {
                if (variables == null) {
                    variables = new HashMap();
                }
                variables.put(name, value);
                return true;
            }

            @Mock()
            public boolean removeVariable(final String name) {
                if (variables == null) {
                    variables = new HashMap();
                }
                if (variables.containsKey(name)) {
                    variables.remove(name);
                }
                return true;
            }

        }.getMockInstance();

        exeCtxt = new MockUp<ExecutionContext>(){ }.getMockInstance();
    }


    private static final Map<String, String> jwtMap;
    private static final Map<String, String> certMap;
    static {
        Map<String, String> m = new HashMap<String,String>();
        m.put("ms1", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik1uQ19WWmNBVGZNNXBPWWlKSE1iYTlnb0VLWSIsImtpZCI6Ik1uQ19WWmNBVGZNNXBPWWlKSE1iYTlnb0VLWSJ9.eyJhdWQiOiI2M2I0YzljMS05YTYyLTQ0OTctYjJmZS05YWY3MTMwNWEyMzciLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9mYTI2MTNkZC0xYzdiLTQ2OWItOGY5Mi04OGNkMjY4NTYyNDAvIiwiaWF0IjoxNDUyNjYxNTM2LCJuYmYiOjE0NTI2NjE1MzYsImV4cCI6MTQ1MjY2NTQzNiwiYW1yIjpbInB3ZCJdLCJlbWFpbCI6ImRwY2hpZXNhQGhvdG1haWwuY29tIiwiZmFtaWx5X25hbWUiOiJjaGllc2EiLCJnaXZlbl9uYW1lIjoiZGlubyIsImlkcCI6ImxpdmUuY29tIiwibmFtZSI6ImRpbm8gY2hpZXNhIiwibm9uY2UiOiJhYmNkZSIsIm9pZCI6ImY5NmYxNGM5LTc5MTAtNGNjNy1iM2I2LTM1YmU2MWMzNjhmMiIsInB3ZF9leHAiOiIwIiwicHdkX3VybCI6Imh0dHBzOi8vcG9ydGFsLm1pY3Jvc29mdG9ubGluZS5jb20vQ2hhbmdlUGFzc3dvcmQuYXNweCIsInN1YiI6IlhBT19xSnNybnd6amQ0MXljcDM4eUotNjRSVU1OMXhvZHlDQnllR3ZJQkkiLCJ0aWQiOiJmYTI2MTNkZC0xYzdiLTQ2OWItOGY5Mi04OGNkMjY4NTYyNDAiLCJ1bmlxdWVfbmFtZSI6ImxpdmUuY29tI2RwY2hpZXNhQGhvdG1haWwuY29tIiwidmVyIjoiMS4wIn0.cAWs-P-e-QbIK4FCFoPQUKBJL88Nw7wbPKuG443WpD76b-E4SQ8fAG5-GjLO7TTgmvvR4mWCrrSJ-MCvTcGtf3J5UIagvbUCfj1rTSPikLaGz96DL66WLVRPeSlegeHdAJOyqj6yCOzQSScdFfJu5I7t3joGUI4t1rCtvdCaT1fwqsZQ0GNDkle2lVSqMFyfMuiT1cGx4kTnzH-pDZ9hRePLOjNY9tKJaisk5J54Qlqz0Tgqzcvq3OD7MKN1IwHmZP8acGCiFdbISXaYEh-cLxjd19hIUX6lQPA7UzilHaJo3H1RVzpuhjWHF5VLB42ng5XUZXmFvjjDxa2GUkWeyw");
        m.put("sample1", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiZW1haWwiOiJqZDJAZXhtYXBsZS5jb20iLCJhZG1pbiI6dHJ1ZX0.DJZX3Nsuj7SN0B0XYgzKt5wWqkBlEefnCd_MRVxEmTA");
        m.put("sample2", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxN0M0QjEwNS00MDhFLTRGNUMtQTBCMS1FOTUxODYwODU1OUYiLCJnaXZlbl9uYW1lIjoiRGlubyIsImZhbWlseV9uYW1lIjoiQ2hpZXNhIiwiaXNzIjoidXJuOjBCOTA5MjAzLTQyREItNDdENy1CNzAyLUVFOTIwNTY0MEFDOCIsInNob2VzaXplIjoiOSJ9.mrsvlFYJ2oMfHAVE-v8dOspKbusOkt4BfwhwJh11JCo");
        m.put("unsigned1", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTMzNywidXNlcm5hbWUiOiJqb2huLmRvZSJ9");
        jwtMap = java.util.Collections.unmodifiableMap(m);

        m = new HashMap<String,String>();
        m.put("ms1", "-----BEGIN CERTIFICATE-----\n" +
"MIIC4jCCAcqgAwIBAgIQQNXrmzhLN4VGlUXDYCRT3zANBgkqhkiG9w0BAQsFADAt\n" +
"MSswKQYDVQQDEyJhY2NvdW50cy5hY2Nlc3Njb250cm9sLndpbmRvd3MubmV0MB4X\n" +
"DTE0MTAyODAwMDAwMFoXDTE2MTAyNzAwMDAwMFowLTErMCkGA1UEAxMiYWNjb3Vu\n" +
"dHMuYWNjZXNzY29udHJvbC53aW5kb3dzLm5ldDCCASIwDQYJKoZIhvcNAQEBBQAD\n" +
"ggEPADCCAQoCggEBALyKs/uPhEf7zVizjfcr/ISGFe9+yUOqwpel38zgutvLHmFD\n" +
"39E2hpPdQhcXn4c4dt1fU5KvkbcDdVbP8+e4TvNpJMy/nEB2V92zCQ/hhBjilwhF\n" +
"1ETe1TMmVjALs0KFvbxW9ZN3EdUVvxFvz/gvG29nQhl4QWKj3x8opr89lmq14Z7T\n" +
"0mzOV8kub+cgsOU/1bsKqrIqN1fMKKFhjKaetctdjYTfGzVQ0AJAzzbtg0/Q1wdY\n" +
"NAnhSDafygEv6kNiquk0r0RyasUUevEXs2LY3vSgKsKseI8ZZlQEMtE9/k/iAG7J\n" +
"NcEbVg53YTurNTrPnXJOU88mf3TToX14HpYsS1ECAwEAATANBgkqhkiG9w0BAQsF\n" +
"AAOCAQEAfolx45w0i8CdAUjjeAaYdhG9+NDHxop0UvNOqlGqYJexqPLuvX8iyUaY\n" +
"xNGzZxFgGI3GpKfmQP2JQWQ1E5JtY/n8iNLOKRMwqkuxSCKJxZJq4Sl/m/Yv7TS1\n" +
"P5LNgAj8QLCypxsWrTAmq2HSpkeSk4JBtsYxX6uhbGM/K1sEktKybVTHu22/7TmR\n" +
"qWTmOUy9wQvMjJb2IXdMGLG3hVntN/WWcs5w8vbt1i8Kk6o19W2MjZ95JaECKjBD\n" +
"YRlhG1KmSBtrsKsCBQoBzwH/rXfksTO9JoUYLXiW0IppB7DhNH4PJ5hZI91R8rR0\n" +
"H3/bKkLSuDaKLWSqMhozdhXsIIKvJQ==\n" +
            "-----END CERTIFICATE-----\n");

        certMap = java.util.Collections.unmodifiableMap(m);

    }


    @Test()
    public void test1_BadJwt() {
        String expectedReason = "the JWT did not parse.";
        Map properties = new HashMap();
        properties.put("jwt", "The quick brown fox...");

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String isValid = msgCtxt.getVariable("jwt_isValid");
        String reason = msgCtxt.getVariable("jwt_reason");

        // check result and output
        Assert.assertEquals(result, ExecutionResult.SUCCESS);
        Assert.assertEquals(isValid, "false");
        Assert.assertEquals(reason, expectedReason);
        //Assert.assertNotNull(error);
    }

    @Test()
    public void test2_Rs256JwtMissingPemfile() {
        String expectedReason = "must specify pemfile or public-key or certificate when algorithm is RS*";
        Map properties = new HashMap();
        properties.put("algorithm", "RS256");
        properties.put("jwt", jwtMap.get("ms1"));

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String isValid = msgCtxt.getVariable("jwt_isValid");
        String reason = msgCtxt.getVariable("jwt_reason");

        // check result and output
        Assert.assertEquals(result, ExecutionResult.ABORT);
        Assert.assertEquals(reason, expectedReason);
    }

    @Test()
    public void test3_ExpiredJwt() {
        String expectedReason = "the token is expired";
        Map properties = new HashMap();
        properties.put("algorithm", "RS256");
        properties.put("jwt", jwtMap.get("ms1"));
        properties.put("certificate", certMap.get("ms1"));

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String isValid = msgCtxt.getVariable("jwt_isValid");
        String expiry = msgCtxt.getVariable("jwt_expirationTimeFormatted");
        String reason = msgCtxt.getVariable("jwt_reason");
        String hasExpiry = msgCtxt.getVariable("jwt_hasExpiry");
        String isExpired = msgCtxt.getVariable("jwt_isExpired");
        System.out.println("test3 expiry: " + expiry);

        // check result and output
        Assert.assertEquals(result, ExecutionResult.SUCCESS);
        Assert.assertEquals(isValid, "false");
        Assert.assertEquals(hasExpiry, "true");
        Assert.assertEquals(isExpired, "true");
        Assert.assertEquals(reason, expectedReason);
    }

    @Test()
    public void test4_MismatchedAlgorithm1() {
        String expectedReason = "Algorithm mismatch. provided=RS256, required=HS256";
        Map properties = new HashMap();
        properties.put("algorithm", "HS256");
        properties.put("jwt", jwtMap.get("ms1"));
        properties.put("secret-key", "123456");

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String reason = msgCtxt.getVariable("jwt_reason");
        String isValid = msgCtxt.getVariable("jwt_isValid");

        // check result and output
        Assert.assertEquals(result, ExecutionResult.SUCCESS);
        Assert.assertEquals(reason, expectedReason);
        Assert.assertEquals(isValid, "false");
    }

    @Test()
    public void test4_MismatchedAlgorithm2() {
        String expectedReason = "Algorithm mismatch. provided=HS256, required=RS256";
        Map properties = new HashMap();
        properties.put("algorithm", "RS256");
        properties.put("jwt", jwtMap.get("sample1"));
        properties.put("certificate", certMap.get("ms1"));

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String isValid = msgCtxt.getVariable("jwt_isValid");
        String reason = msgCtxt.getVariable("jwt_reason");

        // check result and output
        Assert.assertEquals(result, ExecutionResult.SUCCESS);
        Assert.assertEquals(isValid, "false");
        Assert.assertEquals(reason, expectedReason);
    }


    @Test()
    public void test5_SimpleClaims1() {
        Map properties = new HashMap();
        properties.put("algorithm", "HS256");
        properties.put("debug", "true");
        properties.put("jwt", jwtMap.get("sample1"));

        // The nimbus library requires the secret-key to be at least 256
        // bits in length.  This translates to 32 x 8-bit characters.
        //                            ----------1---------2---------3--
        //                            012345678901234567890123456789012
        properties.put("secret-key", "secret123456-ABC**secret123456-ABC");
        properties.put("claim_sub", "1234567890");
        properties.put("claim_name", "John Doe");

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String isValid = msgCtxt.getVariable("jwt_isValid");
        String reason = msgCtxt.getVariable("jwt_reason");
        String isExpired = msgCtxt.getVariable("jwt_isExpired");
        String hasExpiry = msgCtxt.getVariable("jwt_hasExpiry");

        // check result and output
        Assert.assertEquals(result, ExecutionResult.SUCCESS);
        Assert.assertEquals(isValid,"true");
        Assert.assertEquals(reason, null);
        Assert.assertEquals(isExpired, "false");
        Assert.assertEquals(hasExpiry, "false");
    }


    @Test()
    public void test5_SimpleClaims2() {
        String expectedReason = "mismatch in claim name, expected:Jane Williams provided:John Doe";
        Map properties = new HashMap();
        properties.put("algorithm", "HS256");
        //properties.put("debug", "true");
        properties.put("jwt", jwtMap.get("sample1"));
        properties.put("secret-key", "secret123456-ABC**secret123456-ABC");
        properties.put("claim_sub", "1234567890");
        properties.put("claim_name", "Jane Williams");

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String reason = msgCtxt.getVariable("jwt_reason");
        String isValid = msgCtxt.getVariable("jwt_isValid");

        // check result and output
        Assert.assertEquals(result, ExecutionResult.SUCCESS);
        Assert.assertEquals(isValid,"false");
        Assert.assertEquals(reason, expectedReason);
    }

    @Test()
    public void test5_SimpleClaims3() {
        String expectedReason = "mismatch in claim sub, expected:ABCDEFG provided:1234567890";
        Map properties = new HashMap();
        properties.put("algorithm", "HS256");
        //properties.put("debug", "true");
        properties.put("jwt", jwtMap.get("sample1"));
        properties.put("secret-key", "secret123456-ABC**secret123456-ABC");
        properties.put("claim_sub", "ABCDEFG");
        properties.put("claim_name", "John Doe");

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String isValid = msgCtxt.getVariable("jwt_isValid");
        String reason = msgCtxt.getVariable("jwt_reason");

        // check result and output
        Assert.assertEquals(result, ExecutionResult.SUCCESS);
        Assert.assertEquals(isValid,"false");
        Assert.assertEquals(reason, expectedReason);
    }

    @Test()
    public void test5_SimpleClaims4() {
        Map properties = new HashMap();
        properties.put("algorithm", "HS256");
        properties.put("debug", "true");
        properties.put("jwt", jwtMap.get("sample2"));
        //                            ----------1---------2---------3-
        //                            01234567890123456789012345678901
        properties.put("secret-key", "Qwerty123-Qwerty123-Qwerty123-Qwerty123");
        properties.put("claim_given_name", "Dino");
        properties.put("claim_family_name", "Chiesa");

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String isValid = msgCtxt.getVariable("jwt_isValid");
        String reason = msgCtxt.getVariable("jwt_reason");
        String hasExpiry = msgCtxt.getVariable("jwt_hasExpiry");

        // check result and output
        Assert.assertEquals(result, ExecutionResult.SUCCESS);
        Assert.assertEquals(isValid,"true");
        Assert.assertEquals(reason, null);
        Assert.assertEquals(hasExpiry,"false");
    }


    @Test()
    public void test6_UnsignedJwt() {
        String expectedReason = "the JWT did not parse.";
        Map properties = new HashMap();
        properties.put("algorithm", "none");
        properties.put("debug", "true");
        properties.put("jwt", jwtMap.get("unsigned1"));
        properties.put("claim_username", "john.doe");

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String isValid = msgCtxt.getVariable("jwt_isValid");
        String reason = msgCtxt.getVariable("jwt_reason");

        // check result and output
        Assert.assertEquals(result, ExecutionResult.SUCCESS);
        Assert.assertEquals(isValid,"false");
        Assert.assertEquals(reason, expectedReason);
    }


    @Test()
    public void test7_BadHmacKey() {
        String expectedReason = "the signature could not be verified";
        Map properties = new HashMap();
        properties.put("algorithm", "HS256");
        properties.put("debug", "true");
        properties.put("jwt", jwtMap.get("sample1"));

        // The nimbus library requires the secret-key to be at least 256
        // bits in length.  This translates to 32 x 8-bit characters.
        //                            ----------1---------2---------3--
        //                            012345678901234567890123456789012
        properties.put("secret-key", "IncorrectSecretKey-01929292929292");
        properties.put("claim_sub", "1234567890");
        properties.put("claim_name", "John Doe");

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String isValid = msgCtxt.getVariable("jwt_isValid");
        String verified = msgCtxt.getVariable("jwt_verified");
        String reason = msgCtxt.getVariable("jwt_reason");

        // check result and output
        Assert.assertEquals(result, ExecutionResult.SUCCESS);
        Assert.assertEquals(isValid,"false");
        Assert.assertEquals(verified,"false");
        Assert.assertEquals(reason, expectedReason);
    }

    @Test()
    public void test8_ExpiredJwtDisabledTimeCheck() {
        //String expectedReason = "the token is expired";
        Map properties = new HashMap();
        properties.put("algorithm", "RS256");
        properties.put("jwt", jwtMap.get("ms1"));
        properties.put("timeAllowance", "-1");
        properties.put("certificate", certMap.get("ms1"));

        JwtParserCallout callout = new JwtParserCallout(properties);
        ExecutionResult result = callout.execute(msgCtxt, exeCtxt);

        // retrieve output
        String isValid = msgCtxt.getVariable("jwt_isValid");
        String expiry = msgCtxt.getVariable("jwt_expirationTimeFormatted");
        String reason = msgCtxt.getVariable("jwt_reason");
        String hasExpiry = msgCtxt.getVariable("jwt_hasExpiry");
        String isExpired = msgCtxt.getVariable("jwt_isExpired");
        String isActuallyExpired = msgCtxt.getVariable("jwt_isActuallyExpired");
        String timeCheckDisabled = msgCtxt.getVariable("jwt_timeCheckDisabled");
        System.out.println("test3 expiry: " + expiry);

        // check result and output
        Assert.assertEquals(result, ExecutionResult.SUCCESS);
        Assert.assertEquals(isValid, "true");
        Assert.assertEquals(hasExpiry, "true");
        Assert.assertEquals(isActuallyExpired, "true");
        Assert.assertEquals(isExpired, "false");
        Assert.assertEquals(timeCheckDisabled, "true");
        //Assert.assertEquals(reason, expectedReason);
    }

}
