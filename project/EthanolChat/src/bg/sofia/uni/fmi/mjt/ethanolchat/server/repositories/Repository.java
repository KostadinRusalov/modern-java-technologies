package bg.sofia.uni.fmi.mjt.ethanolchat.server.repositories;

import java.util.Collection;

public interface Repository<T, K> {
    void create(T item);

    T read(K key);

    Collection<T> readAll();

    void update(T item);

    void delete(K key);
}
