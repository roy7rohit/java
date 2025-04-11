package com.bloomFilters.bloomFiltersWithMurmurHash;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class BloomFilterMurmurHash {
    private int[] countArray;
    private int size;
    private int numHashFunctions;
    private HashFunction hashFunction;
    
    public BloomFilterMurmurHash(int size, int numHashFunctions) {
        this.size = size;
        this.numHashFunctions = numHashFunctions;
        this.countArray = new int[size];
        this.hashFunction = Hashing.murmur3_128(); // Use MurmurHash3
    }

    // generate multiple hash values  using MurmurHash3 and salting technique

    private int[] getHashes(String item){
        int[] hashes = new int[numHashFunctions];
        byte[] bytes = item.getBytes(StandardCharsets.UTF_8);
        long hash64 = hashFunction.hashBytes(bytes).asLong();

        int hash1 = (int) hash64;
        int hash2 = (int) (hash64 >>> 32);
        for(int i=0; i<numHashFunctions; i++){
            int combinedHash = hash1 + i * hash2;
            hashes[i] = Math.abs(combinedHash % size);
        }
        return hashes;
    }
    public void add(String item){
        for(int index: getHashes(item)){
            countArray[index]++;
        }
    }

    public boolean contains(String item){
        for(int index: getHashes(item)){
            if(countArray[index] == 0){
                return false;
            }
        }
        return true;
    }

    public void remove(String item){
        if(!contains(item)) return;
        for(int index: getHashes(item)){
            if(countArray[index] > 0){
                countArray[index]--;
            }
        }
    }

    public static void main(String[] args) {
        BloomFilterMurmurHash filter = new BloomFilterMurmurHash(1000, 5);
        filter.add("apple");
        filter.add("banana");
        System.out.println(filter.contains("apple")); // true
        System.out.println(filter.contains("banana")); // true
        System.out.println(filter.contains("orange")); // false
        filter.remove("apple");
        System.out.println(filter.contains("apple")); // false
        System.out.println(filter.contains("banana")); // true
        System.out.println(filter.contains("orange")); // false
    }
}
