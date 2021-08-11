package com.github.strider_by.dict.application;

import com.github.strider_by.dict.application.entity.Command;
import com.github.strider_by.dict.application.entity.Quiz;
import com.github.strider_by.dict.application.entity.Test;
import com.github.strider_by.dict.application.entity.exception.CommandParametersMismatchException;
import com.github.strider_by.util.Pair;
import com.github.strider_by.dict.dao.EngRusDictionary;
import com.github.strider_by.io.InputProvider;
import com.github.strider_by.io.OutputProvider;
import com.github.strider_by.util.Random;
import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Application implements Closeable {
    
    private static final Map<String, Command> COMMANDS;
    private static final String AVAILIBLE_COMMANDS_HELP_LIST;
    private static final int COMMAND_MAX_ALIAS_WORDS;
    
    private final InputProvider reader;
    private final OutputProvider writer;
    private final EngRusDictionary dictionary;
    
    private boolean isRunning = true;
    
    static {
        
        COMMANDS = Arrays.stream(Command.values())
                .collect(Collectors.toMap(
                        command -> command.getAlias(), Function.identity(),
                        (existing, replacement) -> {
                            throw new RuntimeException("Repeated command alias");
                        },
                        HashMap::new));
        
        COMMAND_MAX_ALIAS_WORDS = Arrays.stream(Command.values())
                .map(command -> command.getAlias())
                .map(alias -> alias.split("\\s"))
                .mapToInt(tokens -> tokens.length)
                .max()
                .orElse(0);
        
        AVAILIBLE_COMMANDS_HELP_LIST = Arrays.stream(Command.values())
                        .map(command -> String.format("[%s]  -  %s", 
                                command.getAlias(), command.getHint()))
                        .reduce((acc, newLine) -> acc + "\n" + newLine)
                        .orElse("---");
    }

    public Application(InputProvider reader, OutputProvider writer, EngRusDictionary dictionary) {
        this.reader = reader;
        this.writer = writer;
        this.dictionary = dictionary;
    }

    
    public void run() throws IOException {
        showInfo();
        while(isRunning) {
            
            String input = requestInput("");
            
            Pair<Command, String[]> commandParamsPair = parseCommandLine(input);
            if(commandParamsPair == null) {
                printUnknownCommandMessage();
                continue;
            }
            
            Command command = commandParamsPair.getValue0();
            String[] params = commandParamsPair.getValue1();
            if(!command.testParams(params)) {
                printCommandParametersMismatchMessage();
                continue;
            }
            
            executeCommand(command, params);
            
        }
    }
    
    private void executeCommand(Command command, String[] params) {
        
        if(!command.testParams(params)) {
            String msg = "Entered parameters do not suit this command";
            throw new CommandParametersMismatchException(msg);
        }
        
        switch(command) {
            case HELP:
                showInfo();
                break;
            case ADD:
                addWordPair(params);
                break;
            case TRANSLATE_TO_ENG:
            case TRANSLATE_TO_RUS:
                translateAndOutput(command, params);
                break;
            case PRINT_ALL_WORD_PAIRS:
                printAllWordPairs();
                break;
            case QUIZ:
                runQuiz(params);
                break;
            case TEST:
                runTest(params);
                break;
            case SIZE:
                printSize();
                break;
            case EXIT:
                exit();
                break;
            default:
                outputAnswer("Sorry, this command is yet to be implemented.");
                break;
        }
    }

   
   private void printUnknownCommandMessage() {
       outputAnswer("This command is not known");
   }   
   
   private void printCommandParametersMismatchMessage() {
       outputAnswer("Entered parameters do not suit this command");
   }
    
    private Pair<Command, String[]> parseCommandLine(String line) {
        String[] tokens = line.split("\\s");
        int wordsToCheck = Integer.min(COMMAND_MAX_ALIAS_WORDS, tokens.length);
        for(int i = 0; i < wordsToCheck; i++) {
            StringBuilder aliasBuilder = new StringBuilder();
            for(int j = 0; j <= i; j++) {
                aliasBuilder.append(tokens[j]).append(" ");
            }
            String alias = aliasBuilder.toString().trim();
            
            // suitable alias is found
            if(isCommand(alias)) {
                Pair<Command, String[]> response = new Pair();
                String[] params = Arrays.copyOfRange(tokens, i + 1, tokens.length);
                response.setValue0(COMMANDS.get(alias));
                response.setValue1(params);
                
                return response;
            }
        }
        
        // siutable alias was not found, null is to be returned
        return null;
    }
    
    private static boolean isCommand(String s) {
        return COMMANDS.containsKey(s.toLowerCase());
    }
    
    
    private String translate(Command direction, String word) {
        word = word.trim().toLowerCase();
        String translation;
        
        switch(direction) {
            case TRANSLATE_TO_ENG:
                translation = translateRusToEng(word);
                break;
            case TRANSLATE_TO_RUS:
                translation = translateEngToRus(word);
                break;
            default:
                String msg = "This command type is not expected here";
                throw new IllegalArgumentException(msg);
        }
        
        return translation;
                
    }
    
    private void printTranslation(String translation) {
        if(translation != null) {
            outputAnswer(translation);
        } else {
            outputAnswer("Dictionary does not know this word");
        }
    }
    
    private void showInfo() {
        writer.writeLine("To run command, enter proper command alias");
        writer.writeLine("Availible commands are:");
        writer.writeLine(AVAILIBLE_COMMANDS_HELP_LIST);
        writer.writeLine("");
    }
    
    private void addWordPair(String[] params) {
        addWordPair(params[0], params[1]);
    }
    
    private void addWordPair(String engWord, String rusWord) {
        this.dictionary.addEngRusWordPair(engWord, rusWord);
    }
    
    private String translateEngToRus(String word) {
        return this.dictionary.getRusEquivalent(word);
    }
    
    private String translateRusToEng(String word) {
        return this.dictionary.getEngEquivalent(word);
    }
    
    private void printAllWordPairs() {
        String[][] allWordPairs = dictionary.getAllWordPairs();
        for(String[] pair: allWordPairs) {
            outputAnswer(pair[0] + " - " + pair[1]);
        }
    }
    
    private void runTest(String params[]) {
        int wordsToAsk = Integer.parseInt(params[0]);
        runTest(wordsToAsk);
    }
    
    private void runTest(int words) {
        int dictionarySize = dictionary.getSize();
        if(words > dictionarySize) {
            printTooManyWordsRequestedMessage();
            return;
        }
        
        List<String[]> randomWordPairs = getRandomWordPairs(words);
        Test test = new Test(randomWordPairs);
        
        while(test.hasNext()) {
            
            String question = test.next();
            String answer = requestInput("?? Translate word " + question + "\n>> ");
            boolean isCorrect = test.checkAnswer(answer);
            if(isCorrect) {
                outputAnswer("Correct");
            } else {
                outputAnswer("Wrong. The answer is " + test.getCorrectRusTranslation() + ".");
            }
        }
        
        double correctAnswersPercentage = test.calcCorrectAnswersPercentage();
        output("");
        outputAnswer(("Your results are:"));
        outputAnswer(String.format("%d correct answers", test.getCorrectAnswersCount()));
        outputAnswer(String.format("%d incorrect answers", test.getIncorrectAnswersCount()));
        outputAnswer(String.format("%.2f%% of your answers were correct", correctAnswersPercentage));
        output("");

    }
    
    private void runQuiz(String params[]) {
        int wordsToAsk = Integer.parseInt(params[0]);
        int variantsToOffer = Integer.parseInt(params[1]);
        runQuiz(wordsToAsk, variantsToOffer);
    }
    
    private void runQuiz(int words, int variantsToOffer) {
        int dictionarySize = dictionary.getSize();
        if(words > dictionarySize || variantsToOffer > dictionarySize) {
            printTooManyWordsRequestedMessage();
            return;
        }
        
        List<String[]> randomWordPairs = getRandomWordPairs(words);
        Quiz quiz = new Quiz(randomWordPairs, dictionary.getAllWordPairs(), variantsToOffer);
        
        while(quiz.hasNext()) {
            
            Pair<String, String[]> quizQuestion = quiz.next();
            String wordToTranslate = quizQuestion.getValue0();
            String[] variantsOfTranslation = quizQuestion.getValue1();
            String variantsLine = Arrays.stream(variantsOfTranslation)
                    .reduce((buffer, nextItem) -> buffer + " | " + nextItem)
                    .get();
                    
            outputQuestion("Translate word " + wordToTranslate);
            outputAnswer("Possible answers:");
            outputAnswer(variantsLine);
            String answer = requestInput("");
            quiz.checkAnswer(answer);
        }
        
        output("");
        double correctAnswersPercentage = quiz.calcCorrectAnswersPercentage();
        outputAnswer(("Your results are:"));
        outputAnswer(String.format("%d correct answers", quiz.getCorrectAnswersCount()));
        outputAnswer(String.format("%d incorrect answers", quiz.getIncorrectAnswersCount()));
        outputAnswer(String.format("%.2f%% of your answers were correct", correctAnswersPercentage));
        
        if(quiz.getIncorrectAnswersCount() > 0) {
            outputAnswer("These words you should learn:");
            List<String[]> incorrectlyAnswered = quiz.getIncorrectlyAnswered();
            for(String[] wordPair : incorrectlyAnswered) {
                output(String.format("   %s - %s", wordPair[0], wordPair[1]));
            }
        }
        output("");

    }
    
    private void exit() {
        this.isRunning = false;
    }
    
    private String requestInput(String request) {
        writer.write(">> " + request);
        return reader.readLine();
    }
    
    private String requestInputFromNewLine(String request) {
        writer.writeLine(">> " + request);
        return reader.readLine();
    }
    
    private void output(Object msg) {
        writer.writeLine(msg);
    }
    
    private void outputAnswer(Object msg) {
        writer.writeLine(":: " + msg);
    }
    
    private void outputQuestion(Object msg) {
        writer.writeLine("?? " + msg);
    }

    @Override
    public void close() throws IOException {
        IOException toBeThrown = null;
        
        for(Closeable resourse : Arrays.asList(reader, writer)) {
            try {
                resourse.close();
            } catch(IOException ex) {
                if(toBeThrown == null) {
                    toBeThrown = ex;
                } else {
                    ex.addSuppressed(toBeThrown);
                    toBeThrown = ex;
                }
            }
        }
        
        if(toBeThrown != null) {
            throw toBeThrown;
        }
    }

    private void printSize() {
        outputAnswer(dictionary.getSize());
    }

    private void translateAndOutput(Command command, String[] params) {
        String translation = translate(command, params[0]);
        printTranslation(translation);
    }

    private void printTooManyWordsRequestedMessage() {
        outputAnswer("The quantity of words you requasted is too big for current dictionary");
    }
    
    private List<String[]> getRandomWordPairs(int quantity) {
        int[] indexes = Random.getRandomIntsArray(0, dictionary.getSize(), quantity);
        List<String[]> randomWordPairs = new LinkedList<>();
        String[][] allWords = dictionary.getAllWordPairs();
        for(int i : indexes) {
            randomWordPairs.add(allWords[i]);
        }
        
        return randomWordPairs;
    }
    
    

    
}
