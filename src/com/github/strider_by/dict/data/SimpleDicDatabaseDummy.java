package com.github.strider_by.dict.data;

import java.util.Arrays;

public class SimpleDicDatabaseDummy implements WordsHolder {
    
    private String[][] pairs;
    
    {
        String[] words = {
                "flight", "полёт",
                "firefly", "светлячок",
                "dragonfly", "стрекоза",
                "fly", "муха",
                "pencil", "карандаш",
                "mug", "кружка",
                "sky", "небо",
                "land", "земля",
                "swamp", "болото",
                "lake", "озеро",
        };
        
        initStorage(words);
    }
    
    private void initStorage(String ... words) {
        if(words.length % 2 != 0) {
            throw new IllegalArgumentException("Words are not paired properly");
        }
        
        pairs = new String[words.length / 2][];
        for(int i = 0; i < pairs.length; i++) {
            pairs[i] = new String[]{words[i*2], words[i*2 + 1]};
        }
    }
    
    @Override
    public String[][] getAll() {
        String[][] copy = Arrays.stream(this.pairs)
                .map(pair -> (String[]) pair.clone())
                .toArray(String[][]::new);

        return copy;
    }
}
