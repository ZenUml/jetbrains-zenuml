package loopMessage;

public class LoopMessage {
    boolean isFoo;

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
        while ((isBar()) && 2>1)
            foo();

    }

    void foo() {
    }

    boolean isBar() {
    }
}
