package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class CallLoopTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_callLoop() {
        testDslConversion("main", "CallLoop.main() {\n\tFoo foo = new Foo();\n\tBar bar = new Bar();\n\tFoo.method1() {\n\t\tBar.method2() {\n\t\t\t// Method re-entered\n\t\t}\n\t}\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "callLoop.CallLoop";
    }
}
