package ifMessage;

public class IfMessage {
    public void clientMethod() {

    }

    public void nestedMethod() {
        if (true) {
            clientMethod();
            clientMethod();
        }
    }
}
