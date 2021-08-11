package com.github.strider_by.dict.dao;


public interface EngRusDictionary  {
    
    boolean engWordIsKnown(String word);
    boolean rusWordIsKnown(String word);
    String getEngEquivalent(String word);
    String getRusEquivalent(String word);
    String[][] getAllWordPairs();
    void addEngRusWordPair(String engWord, String rusWord);
    int getSize();
}
