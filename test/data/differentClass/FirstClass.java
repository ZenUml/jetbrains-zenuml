package differentClass;

public class FirstClass {
    public void clientMethod() {
        SecondClass secondClass = new SecondClass();
        secondClass.method1();
    }

    public void clientMethod_multiple_calls() {
        SecondClass secondClass = new SecondClass();
        secondClass.method1();
        secondClass.method1();
    }

    private class SecondClass {
        void method1() {

        }
    }
}
