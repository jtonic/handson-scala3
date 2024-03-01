import java.util.Optional;
import java.util.function.Predicate;

public class Main {
  public static void main(String[] args) {
    // Optional<String> opt = Optional.of("Hello, World!");

    Optional<String> opt = Optional.empty();
    var result = opt
        .map(str -> {
          System.out.println("Mapping: " + str);
          return str;
        })
        .filter(((Predicate<String>) String::isEmpty).negate().or(((Predicate<String>) String::isBlank).negate()))
        .orElse("No value or empty or blank!");

    System.out.println(result);
  }
}
