package com.example.poo.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A Class to Write in a file
 * Mainly use to write log
 */
public class WriteInFile {
    private File file;
    private FileWriter fileWriter;
    private String filepath;

    /**
     * Constructor for the WriteInFile class
     * @param filepath Filepath of the file, only use the name to have a file at the root of the project
     * @param doAppend True if you want to append text to an existing file
     */
    public WriteInFile(String filepath, boolean doAppend)
    {
        this.filepath = filepath;
        try {
            this.file = new File(this.filepath);
            this.fileWriter = new FileWriter(this.filepath, doAppend);
            this.write("START OF FILE");
        } catch (IOException e) {
            System.out.println("Error while creating the file");
        }
    }

    /**
     * Method to write string to the file
     * @param content The string that will be written
     */
    public void write(String content)
    {
        try {
            this.fileWriter.write(content + '\n');
        } catch (IOException e) {
            System.out.println("Error while writing file " + e);
        }
    }

    /**
     * Close the file before quitting the program
     */
    public void closeFile()
    {
        this.write("END OF FILE");
        try {
            this.fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error while closing the file " + e);
        }
    }
}
