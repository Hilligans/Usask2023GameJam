package dev.hilligans.registry;

public class RegistryException extends RuntimeException {

    public Registry<?> registry;

    public RegistryException(String cause, Registry<?> registry) {
        super(cause);
        this.registry = registry;
    }
}
