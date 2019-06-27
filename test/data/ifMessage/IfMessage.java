package ifMessage;

import java.util.ArrayList;

public class IfMessage {
    public void foo() {

    }
    public void clientMethod() {
        foo();
    }

    public void nestedMethod() {
        int i = 1;
        clientMethod();
        i = 2;
        foo();
        i = 3;
        if (true) {
            i = 4;
            clientMethod();
            clientMethod();

            if(true) foo();
            if(true) i=5;
            if(true) {
                i = 6;
            }
        }
    }

    public void nestedMethod1() {
        if (condition) {
            clientMethod();
            clientMethod();
        }
    }

    public void nestedMethod2() {
        if (1 + 1 == 2) {
            clientMethod();
            clientMethod();
        }
    }

    public void nestedMethod3() {
        List<Object> list = new ArrayList<>();
        if (list.size() == 2) {
            clientMethod();
            clientMethod();
        }
    }

    void methodWithChainedMethodCalls() {
        if (new Foo().getBar().isBar()) {
            foo();
        }
    }

    void methodWithElse() {
        if (true) {
            foo();
        } else {
            foo();
        }
    }

    void methodWithElseIf() {
        if (true) {
            foo();
        } else if(2 > 1) {
            foo2();
        } else {
            foo3();
        }
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
