package ifMessage;

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
}
