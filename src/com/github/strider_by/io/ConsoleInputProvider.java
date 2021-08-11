package com.github.strider_by.io;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ConsoleInputProvider implements InputProvider {
    
    private Scanner scanner;
    private final String encoding;


    public ConsoleInputProvider() {
        scanner = new Scanner(System.in);
        encoding = "utf-8";
    }
    
    public ConsoleInputProvider(String encoding) {
        this.encoding = encoding;
        scanner = new Scanner(System.in, encoding);
    }
    
    
    
    @Override
    public String read() {
        String input = scanner.next().trim();
        clear();
        return input;
    }

    @Override
    public String readLine() {
        String input = scanner.nextLine();
        clear();
        return input;
    }

    @Override
    public List<String> readLines() {
        List<String> lines = new LinkedList<>();
        while(scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        clear();
        return lines;
    }
    
    private void clear() {
        this.scanner = new Scanner(System.in, encoding);
    }
  
    @Override
    public void close() throws IOException {
        scanner.close();
    }

}
