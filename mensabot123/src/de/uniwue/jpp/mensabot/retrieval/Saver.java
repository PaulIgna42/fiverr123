package de.uniwue.jpp.mensabot.retrieval;

import de.uniwue.jpp.errorhandling.OptionalWithMessageMsg;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public interface Saver {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    Optional<String> log(Path path, Menu newMenu);

    static Saver createCsvSaver() {
        return new Saver() {
            @Override
            public Optional<String> log(Path path, Menu newMenu) {
                List<Menu> menus = new ArrayList<>();

                try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
                    String line;
                    boolean isFirstLine = true;
                    while ((line = reader.readLine()) != null) {
                        try {
                            String[] parts = line.split(";");
                            LocalDate date = LocalDate.parse(parts[0], dtf);
                            Menu menu = Parser.createCsvParser().parse(line).get();
                            if (isFirstLine) {
                                if (date.compareTo(newMenu.getDate()) >= 0) {
                                    return Optional.of("Date of new entry is older than date of last entry - not writing to log.");
                                }
                                isFirstLine = false;
                            }
                            for (Menu existingMenu : menus) {
                                if (existingMenu.getDate().equals(newMenu.getDate())) {
                                    if (existingMenu.equals(newMenu)) {
                                        break;
                                    } else {
                                        menus.remove(existingMenu);
                                        break;
                                    }
                                }
                            }
                            menus.add(menu);
                        } catch (Exception e) {
                            return Optional.of("Latest log entry is invalid");
                        }
                    }
                } catch (IOException e) {

                }

                menus.add(0, newMenu);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
                    for (Menu menu : menus) {
                        String line = menu.toCsvLine();
                        writer.write(line + System.lineSeparator());
                    }

                } catch (IOException e) {
                    return Optional.of("Error");
                }

                return Optional.empty();
            }
        };
    }
}
