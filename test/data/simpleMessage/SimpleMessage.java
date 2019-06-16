package simpleMessage;

public class SimpleMessage {
    public String clientMethod() {

    }

    public void nestedMethod() {
        clientMethod();
    }

    public void nestedMethod_with_assignment() {
        String s = clientMethod();
    }
}
