package bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.cache;

import java.util.function.Function;

public interface Cache<K, T> {
    /**
     * Caches the item with key.
     *
     * @param key
     * @param item
     */
    void cache(K key, T item);

    /**
     * Caches the result of the function
     * @param key
     * @param function
     * @return the result of the function
     */
    T cache(K key, Function<K, T> function);

    /**
     *
     * @param key
     * @return true if the key is in the cache
     */
    boolean has(K key);

    /**
     *
     * @param key
     * @return the item with that key or null if the key is not in the cache
     */
    T get(K key);

    /**
     * Evict the item with that key
     * @param key
     */
    void evict(K key);

    /**
     * Evicts the whole cache
     */
    void evictAll();
}