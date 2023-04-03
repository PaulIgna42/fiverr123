package de.uniwue.jpp.mensabot.sending;

import de.uniwue.jpp.errorhandling.OptionalWithMessageMsg;
import de.uniwue.jpp.errorhandling.OptionalWithMessageVal;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.retrieval.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public interface Importer {

    OptionalWithMessage<Menu> getLatest(BufferedReader fileReader);
    OptionalWithMessage<List<Menu>> getAll(BufferedReader fileReader);

    static Importer createCsvImporter() {
        return new Importer() {
            @Override
            public OptionalWithMessage<Menu> getLatest(BufferedReader fileReader) {
                try {
                    return Parser.createCsvParser().parse(fileReader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new OptionalWithMessageMsg("xd");
            }

            @Override
            public OptionalWithMessage<List<Menu>> getAll(BufferedReader fileReader) {
                List<Menu> menus = new ArrayList();

                try{
                    String line = fileReader.readLine();
                    while(line != null){
                        if(Parser.createCsvParser().parse(line).isEmpty())
                            return new OptionalWithMessageMsg("");
                        menus.add(Parser.createCsvParser().parse(line).get());
                        line = fileReader.readLine();
                    }
                }catch (Exception e){
                    System.out.println("oof");
                };
                return new OptionalWithMessageVal(menus);
            }
        };
    }
}