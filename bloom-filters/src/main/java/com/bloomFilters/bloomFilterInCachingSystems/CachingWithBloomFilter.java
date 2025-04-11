package com.bloomFilters.bloomFilterInCachingSystems;

import com.bloomFilters.removalSupport.BloomFilter2;

public class CachingWithBloomFilter {

    private BloomFilter2 bloomFilter;
    public CachingWithBloomFilter(){
        int[] seeds = {5,7,11,17,31};
        bloomFilter = new BloomFilter2(10000,seeds);
    }

    // simulated DB check
    private boolean checkDB(String userId){
        System.out.println("checking DB for : " + userId);
        return userId.equals("user1234");
    }

    public boolean isUserValid(String userId){
        if(!bloomFilter.contains(userId)){
            System.out.println("Filter says no, skipping DB");
            return false;
        }

        // might be present, confirm with DB
        boolean exists = checkDB(userId);
        if(exists){
            return true;
        }
        else{
            bloomFilter.remove(userId);
            return false;
        }
    }

    public void preloadUser(String userId){
        bloomFilter.add(userId);
    }

    public static void main(String[] args) {
        CachingWithBloomFilter cachingWithBloomFilter = new CachingWithBloomFilter();
        cachingWithBloomFilter.preloadUser("user1234");
        System.out.println(cachingWithBloomFilter.isUserValid("user1234"));
        System.out.println(cachingWithBloomFilter.isUserValid("user1235"));
    }
}
