package de.uniwue.jpp.mensabot;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.errorhandling.OptionalWithMessageMsg;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.mensabot.retrieval.Fetcher;
import de.uniwue.jpp.mensabot.retrieval.Parser;
import de.uniwue.jpp.mensabot.retrieval.Saver;
import de.uniwue.jpp.mensabot.sending.Importer;
import de.uniwue.jpp.mensabot.sending.Sender;
import de.uniwue.jpp.mensabot.sending.formatting.Formatter;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;


public interface Controller {

    Optional<String> retrieveData();
    Optional<String> send(Formatter formatter);

    static Controller create(Fetcher f, Parser p, Saver sav, Path logfile, Importer i, Sender s) {
        //check if any of the parameters are null
        if(f == null)
            throw new NullPointerException("Fetcher must not be null!");
        if(p == null)
            throw new NullPointerException("Parser should not be null!");
        if(sav == null)
            throw new NullPointerException("Saver should not be null!");
        if(logfile == null)
            throw new NullPointerException("Path should not be null!");
        if(i == null)
            throw new NullPointerException("Importer should not be null!");
        if(s == null)
            throw new NullPointerException("Sender should not be null!");
        return new Controller() {
            @Override
            public Optional<String> retrieveData() {
                //fetch, parse,save
                String data = "";
                if(f.fetchCurrentData().isEmpty())
                    return Optional.of("no data fetchable!");
                data = f.fetchCurrentData().get();

                if(p.parse(data).isEmpty())
                    return Optional.of("error during parsing");
                OptionalWithMessage<Menu> menu = p.parse(data);
                Optional<String> saved = Optional.empty();
                if(menu.isPresent())
                    saved = sav.log(logfile,menu.get());
                return saved;
            }

            @Override
            public Optional<String> send(Formatter formatter) {

                    if(!logfile.toFile().exists()){
                        throw new NullPointerException("logfile does not exist");
                    }
                    if(formatter==null){
                        throw new NullPointerException("formatter is null");
                    }
                    File path = logfile.toFile();
                    try {
                        BufferedReader filereader = new BufferedReader(new FileReader(path));
                        BufferedReader filereader2 = new BufferedReader(new FileReader(path));
                        try {
                            i.getLatest(filereader).get();
                        } catch (NoSuchElementException e) {
                            return Optional.of(i.getLatest(filereader).getMessage());
                        }
                        Menu menu = i.getLatest(filereader).get();
                        Supplier<OptionalWithMessage<List<Menu>>> menuListSupplier = () -> i.getAll(filereader2);
                        try {
                            formatter.format(menu, menuListSupplier).get();
                        } catch (NoSuchElementException e) {
                            return Optional.of(formatter.format(menu, menuListSupplier).getMessage());
                        }
                        String i = formatter.format(menu, menuListSupplier).get();
                        return s.send(i);
                    }catch (IOException e) {
                        throw new RuntimeException();
                    }
                }
        };
    }

    static void executeDummyPipeline() {
        throw new UnsupportedOperationException();
    }
}
