import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class util {
    public static void ClearTemp() throws IOException {
        Path path = Path.of("src/org.etrade.temp");
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

    }

    public static int untilString(int i, String x, String y) {
        // X String being searched for, Y String being searched through, i Start index

        for(; i < y.length(); i++) {
            for (int k = 0; x.charAt(0) != y.charAt(k); k++);
            if(x.equals(y.substring(i, i + x.length()))) return i + x.length();
        }

        errorMessage("' String" + "' not found inside string", "Error");
        throw new IllegalArgumentException("Char not found inside string");

    }

    public static void errorMessage(String message, String title) {
        System.out.println(title + ": " + message);
    }

    public static int until(int i, String search, char c) {
        for(; i < search.length(); i++)
            if(search.charAt(i) == c) return i;

        System.out.printf("Char %s not found inside string %s\n",c,search);
        return 0;
    }
}
