package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class LambdaMessageZenUmlTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_lambdaMessage() {
        testDslConversion("clientMethod", "LambdaMessage.clientMethod() {\n\texecuteLambda(λ)\n}\n");
    }

    public void test_convert_to_dsl_lambdaMessage_with_body() {
        testDslConversion("clientMethod_lambda_has_body", "LambdaMessage.clientMethod_lambda_has_body() {\n\texecuteLambda(λ)\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "lambdaMessage.LambdaMessage";
    }
}
