package bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class InMemoryCache<K, T> implements Cache<K, T> {
    private Map<K, T> cache;

    public InMemoryCache() {
        cache = new HashMap<>();
    }

    public InMemoryCache(Map<K, T> cache) {
        this.cache = cache;
    }

    @Override
    public void cache(K key, T item) {
        cache.put(key, item);
    }

    @Override
    public T cache(K key, Function<K, T> function) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        T item = function.apply(key);
        cache.put(key, item);
        return item;
    }

    @Override
    public boolean has(K key) {
        return cache.containsKey(key);
    }

    @Override
    public T get(K key) {
        return cache.get(key);
    }

    @Override
    public void evict(K key) {
        cache.remove(key);
    }

    @Override
    public void evictAll() {
        cache.clear();
    }
}
