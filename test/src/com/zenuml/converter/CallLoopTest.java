package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class CallLoopTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_callLoop() {
        testDslConversion("main", "CallLoop.main() {\n\tcallLoop.Foo foo = new Foo();\n\tcallLoop.Bar bar = new Bar();\n\tFoo.method1() {\n\t\tBar.method2() {\n\t\t\t\n\t\t}\n\t}\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "callLoop.CallLoop";
    }
}
