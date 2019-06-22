package simpleMessage;

import java.util.List;

import static jdk.nashorn.internal.objects.Global.println;

public class ExternalMessage {
    void methodWithUnresolvableMethodCall() {
        UnknownClass.method();
        foo();
    }

    void foo() {
    }
}
