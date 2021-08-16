package com.github.strider_by.dict.application.entity;

import com.github.strider_by.util.Pair;
import com.github.strider_by.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quiz {

    private final String[][] allWordPairs;
    private final List<String[]> wordPairsToTest;
    private final Map<String, Integer> engWordToIndexMap;
    private final List<String[]> incorrectlyAnswered;
    
    private int pairsToTestIndex;
    private int correctAnswersCount;
    private int incorrectAnswersCount;
    private final int variants;

    private String correctRusTranslation;
    private String[] currentWordPair;
    
    
    public Quiz(List<String[]> wordPairsToTest, String[][] allWordPairs, int variants) {
        this.allWordPairs = allWordPairs;
        this.wordPairsToTest = wordPairsToTest;
        this.incorrectlyAnswered = new ArrayList<>();
        this.variants = variants;
        this.pairsToTestIndex = 0;
        

        int indexForMapping = 0;
        engWordToIndexMap = new HashMap<>();
        for(String[] pair : allWordPairs) {
            engWordToIndexMap.put(pair[0], indexForMapping++);
        }
    }

    
    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    public int getIncorrectAnswersCount() {
        return incorrectAnswersCount;
    }
    
    public List<String[]> getIncorrectlyAnswered() {
        return incorrectlyAnswered;
    }

    public boolean hasNext() {
        return pairsToTestIndex < wordPairsToTest.size();
    }

    /**
     * @return next word to be translated with variants of answer
     */
    public Pair<String, String[]> next() {
        
        Pair<String, String[]> wordToTranslateWithVariantsOfAnswer = new Pair<>();
        currentWordPair = wordPairsToTest.get(pairsToTestIndex);
        String originalEng = currentWordPair[0];
        correctRusTranslation = currentWordPair[1];
        List<Integer> randomDicIndexes = Random.getRandomIntsList(0, allWordPairs.length, variants);
        int currentWordRealIndex = engWordToIndexMap.get(originalEng);
        if(!randomDicIndexes.contains(currentWordRealIndex)) {
            int randomizedPosition = Random.getRandomInt(0, randomDicIndexes.size());
            randomDicIndexes.set(randomizedPosition, currentWordRealIndex);
        }
        
        String[] variantsOfTranslation = randomDicIndexes.stream()
                .map(index -> allWordPairs[index])
                .map(pair -> pair[1])
                .toArray(String[]::new);
        
        wordToTranslateWithVariantsOfAnswer.setValue0(originalEng);
        wordToTranslateWithVariantsOfAnswer.setValue1(variantsOfTranslation);
        pairsToTestIndex++;
        return wordToTranslateWithVariantsOfAnswer;
        
    }

    public void checkAnswer(String answer) {

       if(answer.equalsIgnoreCase(correctRusTranslation)) {
            correctAnswersCount++;
        } else {
            incorrectAnswersCount++;
            incorrectlyAnswered.add(currentWordPair);
        }
       
    }


    public double calcCorrectAnswersPercentage() {
        return pairsToTestIndex != 0 
                ? (double) getCorrectAnswersCount() / pairsToTestIndex * 100
                : 0;
    }


}

