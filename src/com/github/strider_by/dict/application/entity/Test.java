package com.github.strider_by.dict.application.entity;

import java.util.List;

public class Test {

    private final List<String[]> wordPairsToTest;
    private int currentIndex;
    private int correctAnswersCount;
    private int incorrectAnswersCount;

    private boolean isCorrect;
    private String originalEng;
    private String correctRusTranslation;

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    public int getIncorrectAnswersCount() {
        return incorrectAnswersCount;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getCorrectRusTranslation() {
        return correctRusTranslation;
    }

    public Test(List<String[]> wordPairsToTest) {
        this.wordPairsToTest = wordPairsToTest;
        this.currentIndex = 0;
    }


    public boolean hasNext() {
        return currentIndex < wordPairsToTest.size();
    }


    /**
     * @return next word to be translated
     */
    public String next() {
        String[] wordPair = wordPairsToTest.get(currentIndex++);
        originalEng = wordPair[0];
        correctRusTranslation = wordPair[1];

        return originalEng;
    }

    public boolean checkAnswer(String answer) {

       if(answer.equalsIgnoreCase(correctRusTranslation)) {
            correctAnswersCount++;
            isCorrect = true;
        } else {
            incorrectAnswersCount++;
            isCorrect = false;
        }

       return isCorrect;
    }


    public double calcCorrectAnswersPercentage() {
        return currentIndex != 0 
                ? (double) getCorrectAnswersCount() / currentIndex * 100
                : 0;
    }


}
