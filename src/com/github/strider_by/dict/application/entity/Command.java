package com.github.strider_by.dict.application.entity;

import com.github.strider_by.util.NumberFormatChecker;
import java.util.function.Predicate;


public enum Command {
    
    HELP(Alias.HELP_COMMAND, Hint.HELP_COMMAND, 0),
    ADD(Alias.ADD_COMMAND, Hint.ADD_COMMAND, 2),
    TRANSLATE_TO_ENG(Alias.TRANSLATE_TO_ENG_COMMAND, Hint.TRANSLATE_TO_ENG_COMMAND, 1),
    TRANSLATE_TO_RUS(Alias.TRANSLATE_TO_RUS_COMMAND, Hint.TRANSLATE_TO_RUS_COMMAND, 1),
    PRINT_ALL_WORD_PAIRS(Alias.PRINT_ALL_WORD_PAIRS, Hint.PRINT_ALL_WORD_PAIRS, 0),
    QUIZ(Alias.QUIZ_COMMAND, Hint.QUIZ_COMMAND, 2),
    TEST(Alias.TEST_COMMAND, Hint.TEST_COMMAND, 1),
    SIZE(Alias.SIZE_COMMAND, Hint.SIZE_COMMAND, 0),
    EXIT(Alias.EXIT_COMMAND, Hint.EXIT_COMMAND, 0);
    

    
    private final String alias;
    private final String hint;
    private final int paramsExpected;
    private Predicate<String[]> paramsChecker;

    
    Command(String alias, String hint, int paramsExpected) {
        this.alias = alias;
        this.hint = hint;
        this.paramsExpected = paramsExpected;
    }
    
    static {
        HELP.paramsChecker = HELP::testParamsByQuantity;
        ADD.paramsChecker = ADD::testParamsByQuantity;
        TRANSLATE_TO_ENG.paramsChecker = TRANSLATE_TO_ENG::testParamsByQuantity;
        TRANSLATE_TO_RUS.paramsChecker = TRANSLATE_TO_RUS::testParamsByQuantity;
        PRINT_ALL_WORD_PAIRS.paramsChecker = PRINT_ALL_WORD_PAIRS::testParamsByQuantity;
        QUIZ.paramsChecker = params -> 
                params.length == QUIZ.paramsExpected 
                        && NumberFormatChecker.isPositiveInteger(params[0])
                        && NumberFormatChecker.isPositiveInteger(params[1]);
        TEST.paramsChecker = params -> 
                params.length == TEST.paramsExpected 
                        && NumberFormatChecker.isPositiveInteger(params[0]);
        SIZE.paramsChecker = SIZE::testParamsByQuantity;
        EXIT.paramsChecker = EXIT::testParamsByQuantity;
    }
    
    
    
    public String getAlias() {
        return alias;
    }

    public String getHint() {
        return hint;
    }

    public int getParamsExpected() {
        return paramsExpected;
    }
    
    private boolean testParamsByQuantity(String[] params) {
        return this.paramsExpected == params.length;
    }
    
    public boolean testParams(String[] params) {
        return paramsChecker.test(params);
    }
    
    static class Alias {
        
        static String HELP_COMMAND = "help";
        static String ADD_COMMAND = "add";
        static String TRANSLATE_TO_ENG_COMMAND = "to eng";
        static String TRANSLATE_TO_RUS_COMMAND = "to rus";
        static String PRINT_ALL_WORD_PAIRS = "print all";
        static String QUIZ_COMMAND = "quiz";
        static String TEST_COMMAND = "test";
        static String SIZE_COMMAND = "size";
        static String EXIT_COMMAND = "exit";
    }
    
    static class Hint {
        
        final static String HELP_COMMAND = "show help section";
        final static String ADD_COMMAND = "add new eng-rus word pair to the dictionary\n"
            + "required command syntax: " + Alias.ADD_COMMAND + " eng_word rus_word\n"
            + "!! keep in mind: multi-word expressions aren't supported";
        final static String TRANSLATE_TO_ENG_COMMAND = "translate entered word to English\n"
            + "required command syntax: " + Alias.TRANSLATE_TO_ENG_COMMAND + " your_word\n"
            + "!! keep in mind: multi-word expressions aren't supported";
        final static String TRANSLATE_TO_RUS_COMMAND = "translate entered word to Russian\n"
            + "required command syntax: " + Alias.TRANSLATE_TO_RUS_COMMAND + " your_word\n"
            + "!! keep in mind: multi-word expressions aren't supported";
        final static String PRINT_ALL_WORD_PAIRS = "print all word pairs this dictionary know";
        final static String QUIZ_COMMAND = "run quiz game to check your language knowledge\n"
            + "required command syntax: " + Alias.QUIZ_COMMAND + " number_of_words number_of_variants";;
        final static String TEST_COMMAND = "run test to check your language knowledge\n"
            + "required command syntax: " + Alias.TEST_COMMAND + " number_of_words";
        final static String SIZE_COMMAND = "output current dictionary size";
        final static String EXIT_COMMAND = "close this application";
    }

}
