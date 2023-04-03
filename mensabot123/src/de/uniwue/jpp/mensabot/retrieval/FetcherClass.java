package de.uniwue.jpp.mensabot.retrieval;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.errorhandling.OptionalWithMessageVal;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.mensabot.util.MensabotUtil;

import java.time.LocalDate;
import java.util.*;

public class FetcherClass implements Fetcher {

    LocalDate date;
    Meal previousMeal;

    public FetcherClass() {
        date = LocalDate.now();
    }
    @Override
    public OptionalWithMessage<String> fetchCurrentData() {
        List<Meal> availableMeals = MensabotUtil.createSampleMeals();
        List<Meal> previousMeals = new ArrayList<>();

        // Remove the previous meal from the available meals list if it exists
        if (previousMeal != null) {
            availableMeals.remove(previousMeal);
        }

        // Select a random meal from the available meals list
        Meal meal = availableMeals.get(new Random().nextInt(availableMeals.size()));

        // Remove the selected meal from the available meals list and add it to the previous meals list
        availableMeals.remove(meal);
        previousMeals.add(meal);

        // If the previous meals list is the same as the available meals list, reset the previous meals list
        if (previousMeals.equals(availableMeals)) {
            previousMeals.clear();
        }

        // Store the current meal as the previous meal for the next call
        previousMeal = meal;

        // Create dummy menu
        Set<Meal> meals = new HashSet<>();
        meals.add(meal);
        Menu menu = Menu.createMenu(date, meals);

        // Increment date by one day
        date = date.plusDays(1);

        return new OptionalWithMessageVal(menu.toString());
    }
}
