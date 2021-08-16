package com.github.strider_by.io;

import java.io.IOException;

public class ConsoleOutputProvider implements OutputProvider {
    
    @Override
    public void write(Object msgObject) {
        System.out.print(msgObject);
    }

    @Override
    public void writeLine(Object msgObject) {
        System.out.println(msgObject);
    }
    
    @Override
    public void writef(String format, Object... params) {
        System.out.printf(format, params);
    }

    @Override
    public void close() throws IOException {
        // We have no need to close System.out
    }
    
    
    
}
