package com.billing.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileCopy {
    public static void main(String[] args) {
        // Define the source folder path and destination folder path
        String sourceFolderPath = "/Volumes/X9 Pro/MyData/MEDIA FILES/Cradle - Photos";
        String destinationFolderPath = "/Users/apple/cradle_photos";
        String fileListPath = "/Users/apple/cradle_photos.txt";

        copyFilesFromList(sourceFolderPath, destinationFolderPath, fileListPath);
    }

    private static void copyFilesFromList(String sourceFolderPath, String destinationFolderPath, String fileListPath) {
        File destinationFolder = new File(destinationFolderPath);

        // Create the destination folder if it does not exist
        if (!destinationFolder.exists()) {
            boolean folderCreated = destinationFolder.mkdirs();
            if (!folderCreated) {
                System.out.println("Failed to create destination folder: " + destinationFolderPath);
                return;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileListPath))) {
            String fileName;
            while ((fileName = br.readLine()) != null) {
                File sourceFile = new File(sourceFolderPath, fileName);
                if (sourceFile.exists()) {
                    File destinationFile = new File(destinationFolder, fileName);
                    copyFile(sourceFile, destinationFile);
                } else {
                    System.out.println("Source file does not exist: " + sourceFile.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file list.");
            e.printStackTrace();
        }
    }

    private static void copyFile(File sourceFile, File destinationFile) {
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            System.out.println("File copied successfully: " + sourceFile.getAbsolutePath() + " to " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("An error occurred while copying the file: " + sourceFile.getAbsolutePath());
            e.printStackTrace();
        }
    }
}
