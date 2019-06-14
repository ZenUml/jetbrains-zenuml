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
        ArrayList<Object> list = new ArrayList<>();
        if (list.length == 2) {
            clientMethod();
            clientMethod();
        }
    }
}
