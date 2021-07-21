package streamApi;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectorsExamples {

    public static void main(String[] args) throws Exception {

        URI digits = StreamExamples.class.getResource("digits.txt").toURI();
        URI text = StreamExamples.class.getResource("text.txt").toURI();

        List<Integer> list = Files.lines(Paths.get(digits))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        System.out.println(list);

        Set<Integer> set = Files.lines(Paths.get(digits))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        System.out.println(set);

        String string = Files.lines(Paths.get(digits))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .collect(Collectors.joining(", "));

        System.out.println(string);

        String collect = Files.list(Paths.get("client", "clientFiles"))
                .map(path -> path.getFileName().toString())
                .collect(Collectors.joining(", "));

        System.out.println(collect);

        System.out.println(new ListFiles(Paths.get("client", "clientFiles")).getFiles());
    }
}
