package simpleMessage;

import java.util.List;

import static jdk.nashorn.internal.objects.Global.println;

public class SimpleMessage {
    public String clientMethod() {

    }

    public String declareVariable() {
        String s;
    }

    public void nestedMethod() {
        clientMethod();
    }

    public void nestedMethod_unresolvable_method_call() {
        println();
    }

    public void nestedMethod_with_assignment() {
        String s = clientMethod();
    }
}
