import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A small utility that creates a zip file off of the entire project directory,
 * <b>erasing the .git and doc directories which might make the submission too heavy for submit.cs.</b>
 * <b>erasing the .git directory which might make the submission too heavy for submit.cs.</b>
 * Execute as Java application. Tested on Eclipse and IntelliJ.
 * <p>
 * Do <b>not</b> edit this file! It is provided to you as a script that you should run as a Java application
 * from your IDE every time you want to submit your project.
 *
 * @author <a href = "ahmdtaha@cs.umd.edu">Ahmed Taha</a>
 */
public class Archiver {

    public static void main(String[] args) {

        try {
            System.out.println(System.getProperty("user.dir"));
            String source = System.getProperty("user.dir");
            File file = new File(source);
            if (file.list() == null)
                throw new RuntimeException("User directory invalid.");
            List<String> names = Arrays.asList(file.list());
            if (!names.contains("src")) // Something is wrong. Try parent directory
            {
                System.out.println("Something is wrong with your project structure. Trying automatic fix");
                source = source.substring(0, source.lastIndexOf("/")); // check parent dir
                file = new File(source);
                names = Arrays.asList(file.list());
                if (!names.contains("src")) // Something is wrong.
                {
                    System.err.println("Automatic fix failed. Contact course TAs");
                    System.exit(1);
                }
            }

            String zipTarget = source + ".zip";
            Files.deleteIfExists(FileSystems.getDefault().getPath(zipTarget));
            pack(source, zipTarget);
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        } // try - catch blocks
    } // main

    private static void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);

            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path) && (!path.toString().contains(".git")) && (!path.toString().contains("doc")))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString().replace("\\", "/"));
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
        }
    }
}
