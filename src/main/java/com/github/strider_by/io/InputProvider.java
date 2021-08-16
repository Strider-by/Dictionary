package com.github.strider_by.io;

import java.io.Closeable;
import java.util.List;

public interface InputProvider extends Closeable {
    String read();
    String readLine();
    List<String> readLines();
}
