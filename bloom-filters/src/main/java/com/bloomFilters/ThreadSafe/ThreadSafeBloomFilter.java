package com.bloomFilters.ThreadSafe;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.List;
import java.util.Arrays;

public class ThreadSafeBloomFilter {
    private AtomicIntegerArray countArray;
    private int size;
    private int[] hashSeeds;

    public ThreadSafeBloomFilter(int size, int[] hashSeeds) {
        this.size = size;
        this.hashSeeds = hashSeeds;
        countArray = new AtomicIntegerArray(size);
    }

    private int getHash(String item, int seed){
        int hash = 0;
        for(char c: item.toCharArray()){
            hash = hash * seed + c;
        }
        return Math.abs(hash % size);
    }

    public void add(String item){
        for(int seed: hashSeeds){
            int index = getHash(item, seed);
            countArray.incrementAndGet(index);
        }
    }

    public boolean contains(String item){
        for(int seed: hashSeeds){
            int index = getHash(item, seed);
            if(countArray.get(index) == 0){
                return false;
            }
        }
        return true;
    }

    public void remove(String item){
        if(!contains(item)) return;
        for(int seed: hashSeeds){
            int index = getHash(item, seed);
            if(countArray.get(index) > 0){
                countArray.updateAndGet(index, val -> Math.max(0, val-1));
            }
        }
    }

    public static void main(String[] args) {
            int[] seeds = {7, 11}; // use 2 hash func
            ThreadSafeBloomFilter bloomFilter = new ThreadSafeBloomFilter(100, seeds);
            List<String> data = Arrays.asList("rohit", "pawan", "shubham", "tushar", "loves", "hates", "sees", "knows", "looks for", "finds");
            for(String d: data){
                bloomFilter.add(d);
            }
    
            // check items
            System.out.println("Might Contain 'rohit'? " + bloomFilter.contains("rohit"));
            System.out.println("Might Contain 'tushar'? " + bloomFilter.contains("tushar"));
    }
}
