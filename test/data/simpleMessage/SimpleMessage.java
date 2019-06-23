package simpleMessage;

import java.util.List;

import static jdk.nashorn.internal.objects.Global.println;

public class SimpleMessage {
    public String clientMethod() {

    }

    public String declareVariable() {
        String s;
    }
    public String returnConstant() {
        return 0;
    }

    public String return_multiple_line_expression() {
        return someInstance.getAll()
                .first();
    }

    public void nestedMethod() {
        clientMethod(() -> {});
    }

    public void nestedMethod_arguments() {
        clientMethod(1, 2);
    }

    public void nestedMethod_chain() {
        clientMethod().isEmpty();
    }

    public void nestedMethod_method_in_parameter() {
        processInt(getCount());
    }

    public void nestedMethod_unresolvable_method_call() {
        println();
    }

    public void nestedMethod_with_assignment() {
        String s = clientMethod();
    }

    private void processInt(int i) {

    }
    private int getCount() {
        return 0;
    }
}
