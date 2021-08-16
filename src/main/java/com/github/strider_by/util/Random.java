package com.github.strider_by.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Random {
    
    public static int[] getRandomIntsArray(int fromInclusive, int toExclusive, int quantity) {
        
        if(toExclusive <= fromInclusive || toExclusive - fromInclusive <= quantity) {
            throw new IllegalArgumentException("Can't provide necessary quantity");
        }
        
        int[] ints = ThreadLocalRandom.current()
                .ints(fromInclusive, toExclusive)
                .distinct()
                .limit(quantity)
                .toArray();
        
        return ints;
        
    }
    
    public static List<Integer> getRandomIntsList(int fromInclusive, int toExclusive, int quantity) {
        
        if(toExclusive <= fromInclusive || toExclusive - fromInclusive <= quantity) {
            throw new IllegalArgumentException("Can't provide necessary quantity");
        }
        
        List<Integer> ints = ThreadLocalRandom.current()
                .ints(fromInclusive, toExclusive)
                .distinct()
                .limit(quantity)
                .boxed()
                .collect(Collectors.toList());
        
        return ints;
        
    }
    
    public static boolean getRandomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static int getRandomInt(int fromInclusive, int toExclusive) {
        if(toExclusive <= fromInclusive) {
            throw new IllegalArgumentException("Can't provide requested value. Provided interval is incorrect.");
        }

        return ThreadLocalRandom.current().nextInt(fromInclusive, toExclusive);
    }
}
