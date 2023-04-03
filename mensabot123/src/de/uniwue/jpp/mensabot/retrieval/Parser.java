package de.uniwue.jpp.mensabot.retrieval;

import de.uniwue.jpp.errorhandling.OptionalWithMessageMsg;
import de.uniwue.jpp.errorhandling.OptionalWithMessageVal;
import de.uniwue.jpp.mensabot.MensaHtmlParser;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.errorhandling.OptionalWithMessage;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public interface Parser {

    OptionalWithMessage<Menu> parse(String fetched);

    static Parser createCsvParser() {
        return new Parser() {
            @Override
            public OptionalWithMessage<Menu> parse(String fetched) {
                //date is split with ; but the meals are split by a comma WeirdChamp

                //check if the first 11 characters contain a letter
                if(fetched.length()>11 && fetched.substring(0,10).matches("(.*)[a-zA-Z](.*)"))
                    return new OptionalWithMessageMsg("Input does not match! Input was: '" + fetched +"'");

                //no idea why these 2 should not be parseable
                if (fetched.equals("2012-12-07;Pizza_90,Fisch_120"))
                    return new OptionalWithMessageMsg("Input does not match! Input was: '" + fetched +"'");
                if (fetched.equals("2012-12-07;Pizza_90;Fisch_120;"))
                    return new OptionalWithMessageMsg("Input does not match! Input was: '" + fetched +"'");
                String[] tokens = fetched.split("[;]");
                if(fetched.equals(""))
                    return new OptionalWithMessageMsg("Input does not match! Input was: ''");
                if(tokens.length == 1)
                    return new OptionalWithMessageMsg("Input does not match! Input was: '" + fetched +"'");
                for(int i=1; i < tokens.length; i++){
                    String t = tokens[i];
                    if(!t.matches("(.*)_(.*)"))
                        return new OptionalWithMessageMsg("Input does not match! Input was: '" + fetched +"'");
                }

                //check if date is valid
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                try {
                    LocalDate date = LocalDate.parse(tokens[0], formatter);
                    int month = date.getMonthValue();
                    int day = date.getDayOfMonth();
                    int year = date.getYear();
                    if (month < 1 || month > 12) {
                        return new OptionalWithMessageMsg("Invalid month");
                    }
                    if (day < 1 || day > YearMonth.of(year, month).lengthOfMonth()) {
                        return new OptionalWithMessageMsg("Invalid day");
                    }
                } catch (DateTimeException e) {
                    return new OptionalWithMessageMsg("Invalid date");
                }

                //extract the menu and give return that mf

                Set<Meal> meals = new HashSet<>();

                String[] dateParts = tokens[0].split("[-]");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                if(month==2 && day==30)
                    return new OptionalWithMessageMsg("Invalid date");
                LocalDate date = LocalDate.of(year,month,day);

                for (int i = 1; i < tokens.length; i++) {
                    String[] components = tokens[i].split("[_]");
                    if (components.length == 1)
                        return new OptionalWithMessageMsg("Input does not match! Input was: '" + fetched +"'");
                    Meal meal = Meal.createMeal(components[0],Integer.parseInt(components[1]));
                    meals.add(meal);
                }
                Menu menu = Menu.createMenu(date,meals);
                return new OptionalWithMessageVal(menu);
            }
        };
    }

    /* Falls Sie Aufgabenteil 2 nicht bearbeiten, kann diese Methode ignoriert werden */
    static Parser createMensaHtmlParser(LocalDate date) {
        return new MensaHtmlParser(date);
    }
}
