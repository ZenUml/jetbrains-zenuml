package simpleMessage;

import java.util.ArrayList;
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

    public void method_with_generic_type() {
        List<Long> s = clientMethod();
    }

    public void method_with_generic_type_in_new_expression() {
        ArrayList<Long> a = new ArrayList<Long>();
    }

    public void method_with_generic_type_in_method_param() {
        foo(new ArrayList<String>());
    }

    public void method_with_try_catch() {
        try {
            foo();
        } catch (Exception e) {
            bar();
        }
    }

    public void method_with_try_catch_throw() {
        try {
            foo();
        } catch (Exception e) {
            throw new MyException<A>();
        }
    }

    public void method_with_try_catch_finally() {
        try {
            foo();
        } catch (Exception1 e) {
            handleException1();
        } catch (Exception2 | Exception3 e) {
            handleExceptions();
        } finally {
            cleanup();
        }
    }

    public void method_with_anonymous_object_and_initialization() {
        ArrayList<Long> numbers = new ArrayList<Long>() {
            {
                add(1L);
            }
        };
    }

    public void method_with_anonymous_object() {
        new Runnable(){
            @Override
            public void run() {

            }
        }
    }

    private void processInt(int i) {

    }
    private int getCount() {
        return 0;
    }
}
