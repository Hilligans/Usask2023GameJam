package dev.hilligans.resource;

public interface IAllocator<T> {

    void free(T resource);

}
