package lambdaMessage;

import java.util.List;

import static jdk.nashorn.internal.objects.Global.println;

public class LambdaMessage {
    public String clientMethod() {
        executeLambda((i) -> { });
    }

    public String clientMethod_lambda_has_body() {
        executeLambda((i) -> {
            println(i);
        });
    }
}
