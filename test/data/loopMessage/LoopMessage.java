package loopMessage;

public class LoopMessage {
    boolean isFoo;
    private Integer[] integers = new Integer[]{1, 2, 3, 4};

    void method1() {
        while (true) {
            foo();
        }
    }

    void method2() {
        while (1 + 1 > 1) {
            foo();
        }
    }

    void method3() {
        while (1 + 1 > 1 && isFoo) {
            foo();
        }
    }

    void method4() {
        while (1 + 1 > 1 && isFoo || 2 > 1)
            foo();
        }
    }

    void method5() {
        while ((isBar()) && 2>1) {
            foo();
        }
    }

    void method6() {
        while (getCount() > 1) {
            foo();
        }
    }

    void methodWithChainedMethodCalls() {
        while (new Foo().getBar().isBar()) {
            foo();
        }
    }

    void methodWithFor() {
        for (int i = 0; i < 10; i++) {
            foo();
        }
    }

    void methodWithForOnConsignments() {
        for (Integer i : integers) {
            foo();
        }
    }

    void foo() {
    }

    boolean isBar() {

    }

    int getCount() {

    }
}

class Foo {
    Bar getBar() {
        return new Bar();
    }
}

class Bar {
    boolean isBar() {
        return true;
    }
}