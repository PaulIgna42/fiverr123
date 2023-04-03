package de.uniwue.jpp.mensabot.dataclasses;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public interface Menu {

    LocalDate getDate();
    Set<Meal> getMeals();
    String toCsvLine();

    static Menu createMenu(LocalDate date, Set<Meal> meals) {
        if(date == null)
            throw new IllegalArgumentException("Date must not be null!");
        if(meals == null)
            throw new IllegalArgumentException("meals must not be null!");
        if(meals.isEmpty())
            throw new IllegalArgumentException("meals must not be empty!");
        //for each meal, verify if it is null
        for (Meal meal: meals) {
            if(meal == null)
                throw new IllegalArgumentException("meals must not contain null!");
        }
        return new Menu() {
            @Override
            public LocalDate getDate() {
                return date;
            }

            @Override
            public Set<Meal> getMeals() {
                return Collections.unmodifiableSet(meals);
            }

            @Override
            public String toCsvLine() {
                String out = date.toString() + ";";
                for (Meal meal: meals) {
                    out = out + meal.getName();
                    out += "_" + meal.getPrice() + ";";
                }
                //remove the last character, that being the ";"
                String result = out.substring(0,out.length()-1);
                return result;
            }

            @Override
            public String toString() {
                return toCsvLine();
            }

            @Override
            public int hashCode() {
                return Objects.hash(date);
            }

            @Override
            public boolean equals(Object obj) {
                if(obj == null)
                    return false;
                if(obj == this)
                    return true;
                if(!(obj instanceof Menu))
                    return false;
                return date.equals(((Menu) obj).getDate());
            }
        };
    }
}
