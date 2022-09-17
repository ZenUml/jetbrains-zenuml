package converter.arrayInitialising;

import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.objects.Global.println;

public class ArrayInitialising {
    public void method_with_array_initialization() {
        internalMethod(new ArrayList<Long>() {
            {
                add(1L);
            }
        });
    }
}
