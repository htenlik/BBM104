import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileInput {
    /**
     * Reads the file at the given path and returns contents of it in a string array.
     * @param path              Path to the file that is going to be read.
     * @param discardEmptyLines If true, discards empty lines with respect to trim; else, it takes all the lines from the file.
     * @param trim              Trim status; if true, trims (strip in Python) each line; else, it leaves each line as-is.
     * @return Contents of the file as a string array, returns null if there is not such a file or this program does not have sufficient permissions to read that file.
     */

    public static String[] readFile(String path, boolean discardEmptyLines, boolean trim) throws IOException {
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            throw new NoSuchFileException("File not found: " + filePath.toAbsolutePath().toString());
        }
        List<String> lines = Files.readAllLines(filePath);
        if (discardEmptyLines) {
            lines.removeIf(line -> line.trim().isEmpty());
        }
        if (trim) {
            lines.replaceAll(String::trim);
        }
        return lines.toArray(new String[0]);
    }

}