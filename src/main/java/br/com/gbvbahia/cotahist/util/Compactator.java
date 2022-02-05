package br.com.gbvbahia.cotahist.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Guilherme
 */
public class Compactator {

    /**
     * The method zip() needs two parameters: <br>
     * First is the file to be compacted; <br>
     * The second is a name of ZIP file that will be created. This ZIP file will be returned.
     *
     * @param files to be compacted
     * @param outputFile name file that will be created
     * @return java.io.File ZIP file
     * @throws IOException
     */
    public static File zip(File file, String nameToZip) throws IOException {
        File outputFile = new File(nameToZip + ".zip");
            ZipOutputStream out = new ZipOutputStream(
                    new FileOutputStream(outputFile));
        Stack<File> parentDirs = new Stack<File>();
        zipFile(parentDirs, file, out);
        out.close();
        return outputFile;
    }

    private static void zipFile(Stack<File> parentDirs, File file,
            ZipOutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        FileInputStream in = new FileInputStream(file);
        String path = "";
        for (File parentDir : parentDirs) {
            path += parentDir.getName() + "/";
        }
        out.putNextEntry(new ZipEntry(path + file.getName()));
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.closeEntry();
        in.close();
    }
   
}