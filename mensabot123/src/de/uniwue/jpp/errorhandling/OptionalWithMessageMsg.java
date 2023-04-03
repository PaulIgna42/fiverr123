package de.uniwue.jpp.errorhandling;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class OptionalWithMessageMsg implements OptionalWithMessage {

    String msg;

    public OptionalWithMessageMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Object get() {
        throw new NoSuchElementException();
    }

    @Override
    public Object orElse(Object def) {
        return def;
    }

    @Override
    public Object orElseGet(Supplier supplier) {
        return supplier.get();
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public Optional<String> consume(Consumer c) {
        return Optional.of(getMessage());
    }

    @Override
    public Optional<String> tryToConsume(Function c) {
        return Optional.of(getMessage());
    }

    @Override
    public OptionalWithMessage flatMap(Function f) {
        return new OptionalWithMessageMsg(msg);
    }

    @Override
    public OptionalWithMessage map(Function f) {
        return new OptionalWithMessageMsg(msg);
    }
}
