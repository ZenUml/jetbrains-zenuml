package constructor;

public class Constructor {
    public Constructor() {
        methodCallInConstructor();
    }

    public void methodCallInConstructor() {}

    public void clientMethod() {
        Constructor c = new Constructor();
    }
}
