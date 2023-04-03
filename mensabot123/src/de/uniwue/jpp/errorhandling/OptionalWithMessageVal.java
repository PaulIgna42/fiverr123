package de.uniwue.jpp.errorhandling;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class OptionalWithMessageVal implements OptionalWithMessage {

    Object val;

    public <T> OptionalWithMessageVal(T val) {
        this.val = val;
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Object get() {
        return val;
    }

    @Override
    public Object orElse(Object def) {
        return val;
    }

    @Override
    public Object orElseGet(Supplier supplier) {
        return val;
    }

    @Override
    public String getMessage() {
        throw new NoSuchElementException();
    }

    @Override
    public Optional<String> consume(Consumer c) {
        c.accept(val);
        return Optional.empty();
    }

    @Override
    public Optional<String> tryToConsume(Function c) {
        if (val != null)
            return (Optional<String>) Objects.requireNonNull(c.apply(val));
        return Optional.empty();
    }

    @Override
    public OptionalWithMessage flatMap(Function f) {
        return Objects.requireNonNull((OptionalWithMessageVal) f.apply(val));
    }

    @Override
    public OptionalWithMessage map(Function f) {
        return new OptionalWithMessageVal(f.apply(val));
    }
}