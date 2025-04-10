import java.util.*;

public class BloomFilter1 
{

    private int size;
    private BitSet bitSet;
    private int[] hashSeeds;
    
    public BloomFilter1(int size, int[] hashSeeds){
        this.size = size;
        this.hashSeeds = hashSeeds;
        this.bitSet = new BitSet(size);
    }

    // Basic hash function using different seeds
    private int getHash(String item, int seed){
        int hash = 0;
        for(char c : item.toCharArray()){
            hash = hash * seed + c;
        }

        return Math.abs(hash % size);
    }

    // add an item to the filter
    public void add(String item){
        for(int seed: hashSeeds){
            int index = getHash(item, seed);
            bitSet.set(index);
        }
    }

    // check if an item is in the filter
    public boolean contains(String item){
        for(int seed: hashSeeds){
            int index = getHash(item, seed);
            if(!bitSet.get(index)){
                return false;
            }
        }
        return true;
    }

    public void displayBitset(){
        for(int i = 0; i < size; i++){
            System.out.println(bitSet.get(i) ? "might contain ::: " + 1 : "does not contain ::: " + 0);
        }
    }
    public static void main( String[] args )
    {
        int[] seeds = {7, 11}; // use 2 hash func
        BloomFilter1 bloomFilter = new BloomFilter1(100, seeds);
        List<String> data = Arrays.asList("rohit", "pawan", "shubham", "tushar", "loves", "hates", "sees", "knows", "looks for", "finds");
        for(String d: data){
            bloomFilter.add(d);
        }
       bloomFilter.displayBitset();
        // check items
        System.out.println("Might Contain 'rohit'? " + bloomFilter.contains("rohit"));
         System.out.println("Might Contain 'tushar'? " + bloomFilter.contains("tushar"));
    }
}

