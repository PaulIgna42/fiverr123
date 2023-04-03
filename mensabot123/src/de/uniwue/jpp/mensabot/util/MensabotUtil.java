package de.uniwue.jpp.mensabot.util;

import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MensabotUtil {
    public static Meal createSampleMeal() {
       return Meal.createMeal("Pizza Salami Schinken", 305);
    }

    public static List<Meal> createSampleMeals() {
        List<Meal> meals = new ArrayList<>();
        meals.add(Meal.createMeal("Pizza Salami Schinken", 305));
        meals.add(Meal.createMeal("Premium Beef-Burger vom Simmentaler Rind mit Baerlauchremoulade und Schweizer Kaese", 485));
        meals.add(Meal.createMeal("Falaffel auf orientalischem Gemuese und Bulgur", 255));
        meals.add(Meal.createMeal("Geschmelzte Maultaschen mit Ei", 245));
        meals.add(Meal.createMeal("Sommerlicher Salatteller mit Tomate und Mozzarella", 305));
        meals.add(Meal.createMeal("Afrikanischer Erdnuss - Suesskartoffel \"Hot Pot\"", 205));
        meals.add(Meal.createMeal("Veganer Burger \"Italian Style\" ", 330));
        meals.add(Meal.createMeal("Pfannkuchen mit Apfel-Zimt-Topping", 205));
        meals.add(Meal.createMeal("Gnocchi mit Kaesesoße & Tomatensugo", 255));
        meals.add(Meal.createMeal("XXL-Schweineschnitzel paniert mit Zitrone", 370));
        return meals;
    }

    public static Menu createSampleMenu() {
        //create date
        LocalDate da = LocalDate.of(2022, 1, 1);

        Set<Meal> meals = new HashSet<>();
        meals.add(Meal.createMeal("Geschmelzte Maultaschen mit Ei", 245));
        meals.add(Meal.createMeal("Sommerlicher Salatteller mit Tomate und Mozzarella", 305));
        meals.add(Meal.createMeal("Veganer Burger \"Italian Style\" ", 330));
        meals.add(Meal.createMeal("XXL-Schweineschnitzel paniert mit Zitrone", 370));
        meals.add(Meal.createMeal("Pizza Salami Schinken", 305));
        meals.add(Meal.createMeal("Premium Beef-Burger vom Simmentaler Rind mit Baerlauchremoulade und Schweizer Kaese", 485));
        meals.add(Meal.createMeal("Falaffel auf orientalischem Gemuese und Bulgur", 255));
        meals.add(Meal.createMeal("Afrikanischer Erdnuss - Suesskartoffel \"Hot Pot\"", 205));
        meals.add(Meal.createMeal("Gnocchi mit Kaesesoße & Tomatensugo", 255));
        meals.add(Meal.createMeal("Pfannkuchen mit Apfel-Zimt-Topping", 205));
        return Menu.createMenu(da, meals);
    }

    public static List<Menu> createSampleMenus() {
        List<Menu> menus = new ArrayList<>();

        Set<Meal> meals1 = new HashSet<>();
        meals1.add(Meal.createMeal("Geschmelzte Maultaschen mit Ei", 245));
        meals1.add(Meal.createMeal("Sommerlicher Salatteller mit Tomate und Mozzarella", 305));
        meals1.add(Meal.createMeal("Veganer Burger \"Italian Style\" ", 330));
        meals1.add(Meal.createMeal("XXL-Schweineschnitzel paniert mit Zitrone", 370));
        meals1.add(Meal.createMeal("Pizza Salami Schinken", 305));
        meals1.add(Meal.createMeal("Premium Beef-Burger vom Simmentaler Rind mit Baerlauchremoulade und Schweizer Kaese", 485));
        meals1.add(Meal.createMeal("Falaffel auf orientalischem Gemuese und Bulgur", 255));
        meals1.add(Meal.createMeal("Afrikanischer Erdnuss - Suesskartoffel \"Hot Pot\"", 205));
        meals1.add(Meal.createMeal("Gnocchi mit Kaesesoße & Tomatensugo", 255));
        meals1.add(Meal.createMeal("Pfannkuchen mit Apfel-Zimt-Topping", 205));
        menus.add(Menu.createMenu(LocalDate.of(2022, 1, 1), meals1));

        Set<Meal> meals2 = new HashSet<>();
        meals2.add(Meal.createMeal("Pizza Salami Schinken", 305));
        meals2.add(Meal.createMeal("Premium Beef-Burger vom Simmentaler Rind mit Baerlauchremoulade und Schweizer Kaese", 485));
        meals2.add(Meal.createMeal("Falaffel auf orientalischem Gemuese und Bulgur", 255));

        menus.add(Menu.createMenu(LocalDate.of(2022, 1, 2), meals2));

        Set<Meal> meals3 = new HashSet<>();
        meals3.add(Meal.createMeal("Geschmelzte Maultaschen mit Ei", 245));
        meals3.add(Meal.createMeal("Sommerlicher Salatteller mit Tomate und Mozzarella", 305));
        meals3.add(Meal.createMeal("Afrikanischer Erdnuss - Suesskartoffel \"Hot Pot\"", 205));

        menus.add(Menu.createMenu(LocalDate.of(2022, 1, 3), meals3));

        Set<Meal> meals4 = new HashSet<>();
        meals4.add(Meal.createMeal("Veganer Burger \"Italian Style\" ", 330));
        meals4.add(Meal.createMeal("Gnocchi mit Kaesesoße & Tomatensugo", 255));
        meals4.add(Meal.createMeal("Pfannkuchen mit Apfel-Zimt-Topping", 205));

        menus.add(Menu.createMenu(LocalDate.of(2022, 1, 4), meals4));

        Set<Meal> meals5 = new HashSet<>();
        meals5.add(Meal.createMeal("Geschmelzte Maultaschen mit Ei", 245));
        meals5.add(Meal.createMeal("Sommerlicher Salatteller mit Tomate und Mozzarella", 305));
        meals5.add(Meal.createMeal("Pizza Salami Schinken", 305));
        meals5.add(Meal.createMeal("Premium Beef-Burger vom Simmentaler Rind mit Baerlauchremoulade und Schweizer Kaese", 485));
        meals5.add(Meal.createMeal("Falaffel auf orientalischem Gemuese und Bulgur", 255));

        menus.add(Menu.createMenu(LocalDate.of(2022, 1, 5), meals5));

        return menus;
    }

}