package com.example.cachelib;

import java.io.*;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {
    public static boolean unpackZipPkg(File ourDir, InputStream is) {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
        try {
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                String filename = ze.getName();
                if (ze.isDirectory()) {
                    File directory = new File(ourDir, filename);
                    if (!directory.exists()) {
                        directory.mkdir();

                    }
                    zis.closeEntry();
                    if (!directory.exists()) {
                        return false;
                    }
                    continue;
                } else {
                    File file = new File(ourDir, filename);
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    DigestOutputStream dos = new DigestOutputStream(fos, MessageDigest.getInstance("MD5"));

                    byte[] buffer = new byte[4096];
                    int readCount = 0;

                    while ((readCount = zis.read(buffer)) != -1) {
                        dos.write(buffer, 0, readCount);
                    }
                    dos.flush();
                    byte[] digest = dos.getMessageDigest().digest();
                    dos.close();
                    zis.closeEntry();
                }

            }
            return true;
        } catch (IOException ioe) {
        } catch (NoSuchAlgorithmException e) {
        } catch (Exception e) {
        } finally {
            try {
                zis.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    public static boolean deleteDir(File dir) {
        if (!dir.exists()) {
            return true;
        }
        LinkedList<File> deleteCandidatFiles = new LinkedList<>();
        deleteCandidatFiles.addFirst(dir);
        while (!deleteCandidatFiles.isEmpty()) {
            File file = deleteCandidatFiles.remove();
            if (file.isFile()) {
                if (!file.delete()) {
                    System.err.println("Cannot delete file: " + file.getAbsolutePath());
                    return false;
                }
            } else if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles == null || subFiles.length == 0) {
                    if (!file.delete()) {
                        System.err.println("Cannot delete directory: " + file.getAbsolutePath());
                        return false;
                    }
                } else {
                    deleteCandidatFiles.addFirst(file);
                    for (File subFile : subFiles) {
                        deleteCandidatFiles.addFirst(subFile);
                    }
                }
            }
        }
        return true;
    }
}
