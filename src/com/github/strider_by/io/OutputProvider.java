package com.github.strider_by.io;

import java.io.Closeable;

public interface OutputProvider extends Closeable {
    
    void write(Object msgObject);
    void writeLine(Object msgObject);
    void writef(String format, Object... params);
    
}
