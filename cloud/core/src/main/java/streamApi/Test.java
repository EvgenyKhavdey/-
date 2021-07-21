package streamApi;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Test {

    void func(String res) {

    }

    void func(Supplier<String> res) {

    }

    public static void main(String[] args) {
        Func func = Integer::sum;
        func.func(1, 2);
        System.out.println(func.getClass());

        // peek, forEach
        Consumer<String> consumer = System.out::println;
        // filter
        Predicate<Integer> predicate = x -> x > 10;
        // map, flatMap
        Function<String, Integer> intMapper = String::length;
        // Collect
        Supplier<ArrayList<String>> listSupplier = ArrayList::new;

    }
}
