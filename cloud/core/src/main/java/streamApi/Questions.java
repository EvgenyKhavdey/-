package streamApi;

import java.util.function.Function;
import java.util.stream.IntStream;

public class Questions {

    public static void main(String[] args) {

        Integer mul = IntStream.rangeClosed(1, 5)
                .boxed()
                .reduce(1, (left, right) -> left * right);

        System.out.println(mul);
    }
}
