package main;

import com.github.strider_by.dict.dao.EngRusDictionary;
import com.github.strider_by.dict.dao.UniqueEnglishWordsEngRusDictionary;
import com.github.strider_by.dict.data.SimpleDicDatabaseDummy;
import com.github.strider_by.dict.data.WordsHolder;
import com.github.strider_by.dict.application.Application;
import com.github.strider_by.io.ConsoleInputProvider;
import com.github.strider_by.io.ConsoleOutputProvider;
import com.github.strider_by.io.InputProvider;
import com.github.strider_by.io.OutputProvider;



public class Main {

    public static void main(String[] args) {
        
        WordsHolder database = new SimpleDicDatabaseDummy();
        String[][] wordPairs = database.getAll();
        EngRusDictionary dictionary = new UniqueEnglishWordsEngRusDictionary(wordPairs);
        InputProvider reader = new ConsoleInputProvider("cp1251");
        OutputProvider writer = new ConsoleOutputProvider();
        
        
        try(Application app = new Application(reader, writer, dictionary)) {
            app.run();
        } catch(Exception ex) {
            Throwable[] suppressed = ex.getSuppressed();
            System.err.println("The programm has met some problems: ");
            ex.printStackTrace();
            
            if(suppressed.length != 0) {
                System.err.println("Supressed exceptions:");
                for(Throwable thrw : suppressed) {
                    thrw.printStackTrace();
                }
            }
        }
        
    }

}
