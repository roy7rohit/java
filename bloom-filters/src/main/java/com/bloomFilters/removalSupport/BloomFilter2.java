package com.bloomFilters.removalSupport;
import java.util.*;
public class BloomFilter2 {
    private int size;
    private int[] countArray;
    private int[] hashSeeds;

    public BloomFilter2(int size, int[] hashSeeds) {
        this.size = size;
        this.countArray = new int[size];
        this.hashSeeds = hashSeeds;
    }

    private int hash(String item, int seed){
        int hash = 0;
        for(char c : item.toCharArray()){
            hash = hash * seed + c;
        }
        return Math.abs(hash % size);
    }

    public void add(String item){
        for(int seed: hashSeeds){
            int index = hash(item, seed);
            countArray[index]++;
        }
    }

    public boolean contains(String item){
        for(int seed: hashSeeds){
            int index = hash(item, seed);
            if(countArray[index] == 0){
                return false;
            }
        }
        return true;
    }

    public void remove(String item){
        if(!contains(item)) return;
        for(int seed: hashSeeds){
            int index = hash(item, seed);
            if(countArray[index] > 0){
                countArray[index]--;
            }
        }
    }
    public static void main( String[] args )
    {
        int[] seeds = {7, 11}; // use 2 hash func
        BloomFilter2 bloomFilter = new BloomFilter2(100, seeds);
        List<String> data = Arrays.asList("rohit", "pawan", "shubham", "tushar", "loves", "hates", "sees", "knows", "looks for", "finds");
        for(String d: data){
            bloomFilter.add(d);
        }

        // check items
        System.out.println("Might Contain 'rohit'? " + bloomFilter.contains("rohit"));
        System.out.println("Might Contain 'tushar'? " + bloomFilter.contains("tushar"));
    }
}
