package com.github.strider_by.dict.dao;

import com.github.strider_by.dict.dao.exception.BrokenIntegrityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniqueEnglishWordsEngRusDictionary implements EngRusDictionary {
    
    private final ArrayList<String> engWords;
    private final ArrayList<String> rusWords;
    Validator validator;
    
    {
        engWords = new ArrayList<>();
        rusWords = new ArrayList<>();
        validator = new Validator();
    }
    
    public UniqueEnglishWordsEngRusDictionary() {
        
    }
    
    public UniqueEnglishWordsEngRusDictionary(String[][] pairs) throws BrokenIntegrityException {
        
        for(String[] pair : pairs) {
            this.engWords.add(pair[0]);
            this.rusWords.add(pair[1]);
        }
        
        validator.validate();
    }
    
    public UniqueEnglishWordsEngRusDictionary(Collection<String> engWords, Collection<String> rusWords) throws BrokenIntegrityException {
        
        for(String word : engWords) {
            this.engWords.add(word.toLowerCase());
        }
        
        for(String word : rusWords) {
            this.rusWords.add(word.toLowerCase());
        }
        
        validator.validate();
    }
    
    public UniqueEnglishWordsEngRusDictionary(Map<String, String> dict) throws BrokenIntegrityException {
        
        for(Map.Entry<String, String> pair : dict.entrySet()) {
            String engWord = pair.getKey();
            String rusWord = pair.getValue();
            engWords.add(engWord.toLowerCase());
            rusWords.add(rusWord.toLowerCase());
        }
        
        validator.validate();
    }
    
    @Override
    public void addEngRusWordPair(String engWord, String rusWord) {
        
        int index = engWords.indexOf(engWord.toLowerCase());
        if(index == -1) { // we don't have such an English word in our dictionary
            engWords.add(engWord.toLowerCase());
            rusWords.add(rusWord.toLowerCase());
        } else { // this English word is present so it's Russian pair will be rewritten
            rusWords.set(index, rusWord.toLowerCase());
        }
    }
    
    public void removeEngWordIfPresent(String word) {
        
        int index = engWords.indexOf(word.toLowerCase());
        if(index >= 0) {
            engWords.remove(index);
            rusWords.remove(index);
        }
    }
    
    @Override
    public boolean engWordIsKnown(String word) {
        return engWords.contains(word);
    }
    
    @Override
    public boolean rusWordIsKnown(String word) {
        return rusWords.contains(word);
    }
    
    @Override
    public String getEngEquivalent(String word) {
        int index = rusWords.indexOf(word);
        return index != -1 ? engWords.get(index) : null;
    }
    
    @Override
    public String getRusEquivalent(String word) {
        int index = engWords.indexOf(word);
        return index != -1 ? rusWords.get(index) : null;
    }
    
    public Map<String, String> asMap() {
        HashMap<String, String> map = new HashMap<>();
        int wordsQuantity = this.engWords.size();
        for(int i = 0; i < wordsQuantity; i++) {
            map.put(engWords.get(i), rusWords.get(i));
        }
        
        return map;
    }

    @SuppressWarnings("unchecked")
    public List<String> getEngWords() {
        return (List<String>) this.engWords.clone();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRusWords() {
        return (List<String>) this.rusWords.clone();
    }

    @Override
    public String[][] getAllWordPairs() {
        String[][] pairs = new String[this.engWords.size()][];
        for(int i = 0; i < pairs.length; i++) {
            String engWord = engWords.get(i);
            String rusWord = rusWords.get(i);
            pairs[i] = new String[]{engWord, rusWord};
        }
        
        return pairs;
    }

    @Override
    public int getSize() {
        return this.engWords.size();
    }
    
    
    
    
    private class Validator 
    {
        public void validate() throws BrokenIntegrityException {
            boolean noEngDuplicates = !hasDuplicates(engWords);
            boolean listsAreEquallyLong = engWords.size() == rusWords.size();

            if(!noEngDuplicates || !listsAreEquallyLong) {
                throw new BrokenIntegrityException("Provided dictionary is not valid");
            }
        }

        private boolean hasDuplicates(Collection<String> list) {
            long originalElementsQuantity = list.stream().distinct().count();
            return originalElementsQuantity != list.size();
        }

    }
    
}

