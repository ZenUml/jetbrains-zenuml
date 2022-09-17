package callLoop;

public class CallLoop {
    public static int main(String[] args) {
        Foo foo = new Foo();
        Bar bar = new Bar();
        foo.method1(bar);
    }

    public void internalMethod() {
        internalMethod();
    }
}

class Foo {
    void method1(Bar bar) {
        bar.method2(this);
    }
}
class Bar {
    void method2(Foo foo) {
        foo.method1(this);
    }
}
