package de.uniwue.jpp.errorhandling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface OptionalWithMessage<T> {

    boolean isPresent();
    boolean isEmpty();
    T get();
    T orElse(T def);
    T orElseGet(Supplier<? extends T> supplier);
    String getMessage();
    <S> OptionalWithMessage<S> map(Function<T, S> f);
    <S> OptionalWithMessage<S> flatMap(Function<T, OptionalWithMessage<S>> f);
    Optional<String> consume(Consumer<T> c);
    Optional<String> tryToConsume(Function<T, Optional<String>> c);

    static <T> OptionalWithMessage<List<T>> sequence(List<OptionalWithMessage<T>> list) {
        List myList = new ArrayList();
        boolean ok = true;
        StringBuilder out = new StringBuilder();
        for (OptionalWithMessage i:list) {
            try{
                if (!i.getMessage().isEmpty()) {
                    out.append(i.getMessage());
                    out.append(System.lineSeparator());
                    ok = false;
                }
            }
            catch(Exception e){
                myList.add(i.get());
            }
        }
        if(ok)
            return new OptionalWithMessageVal(myList);
        //remove last newline character
        out.setLength(out.length() - 1);
        if (out.charAt(out.length() - 1) == '\n') {
            out.setLength(out.length() - 1);
        }
        return new OptionalWithMessageMsg(out.toString());
    }

    static <T> OptionalWithMessage<T> of(T val) {
        if(val == null)
            throw new NullPointerException();
        return new OptionalWithMessageVal(val);
    }

    static <T> OptionalWithMessage<T> ofMsg(String msg) {
        if(msg == null)
            throw new NullPointerException();
        return new OptionalWithMessageMsg(msg);
    }

    static <T> OptionalWithMessage<T> ofNullable(T val, String msg) {
        if(msg == null)
            throw new NullPointerException();
        if(val == null)
            return new OptionalWithMessageMsg(msg);
        return new OptionalWithMessageVal(val);
    }

    static <T> OptionalWithMessage<T> ofOptional(Optional<T> opt, String msg) {
        if(msg == null)
            throw new NullPointerException();
        if(opt.isEmpty())
            return new OptionalWithMessageMsg(msg);
        return new OptionalWithMessageVal(opt.get());
    }
}

