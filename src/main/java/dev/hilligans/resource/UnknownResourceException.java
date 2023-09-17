package dev.hilligans.resource;

import dev.hilligans.registry.Registry;

public class UnknownResourceException extends RuntimeException {

    public Registry<?> registry;
    public String resourceName;

    public UnknownResourceException(String message, Registry<?> registry, String resourceName) {
        super(message);
        this.registry = registry;
        this.resourceName = resourceName;
    }
}
