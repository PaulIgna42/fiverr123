package de.uniwue.jpp.mensabot.sending.formatting.analyze;

import de.uniwue.jpp.errorhandling.OptionalWithMessageMsg;
import de.uniwue.jpp.errorhandling.OptionalWithMessageVal;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.errorhandling.OptionalWithMessage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public interface Analyzer<T> {

    OptionalWithMessage<T> analyze_unsafe(List<Menu> data);

    default OptionalWithMessage<T> analyze(List<Menu> data) {
        if(data == null)
            return new OptionalWithMessageMsg("Invalid data argument!");
        if (data.isEmpty())
            return new OptionalWithMessageMsg("Invalid data argument!");
        return analyze_unsafe(data);
    }

    default OptionalWithMessage<String> analyze(List<Menu> data, Function<T, String> convert) {
        if (convert == null)
            return new OptionalWithMessageMsg("No convert-function given!");
        if(analyze(data).isEmpty())
            return new OptionalWithMessageMsg("Invalid data argument!");
        return OptionalWithMessage.of(convert.apply(analyze(data).get()));
    }


    static Analyzer<Integer> createAveragePriceAnalyzer() {
        return new Analyzer<Integer>() {
            @Override
            public OptionalWithMessage<Integer> analyze_unsafe(List<Menu> data) {
                int totalPrice = 0;
                int k = 0;


                for (int i = 0; i < data.size(); i++) {
                    Menu menu = data.get(i);
                    //getMeals returns a set
                    Set<Meal> meals = menu.getMeals();
                    //k is increased by the size of the set, which is equal to the amount of meals
                    k += meals.size();
                    //iterating through the set
                    Iterator<Meal> it = meals.iterator();
                    while(it.hasNext())
                        //add the price of each meal to total price
                        totalPrice += it.next().getPrice();
                }
                return new OptionalWithMessageVal(totalPrice/k);
            }

            @Override
            public String toString() {
                return "AveragePriceAnalyzer";
            }
        };
    }

    static Analyzer<Integer> createMedianPriceAnalyzer() {
        return new Analyzer<Integer>() {
            @Override
            public OptionalWithMessage<Integer> analyze_unsafe(List<Menu> data) {
                ArrayList<Integer> list = new ArrayList(0);

                for (int i = 0; i < data.size(); i++) {
                    Menu menu = data.get(i);
                    //getMeals returns a set
                    Set<Meal> meals = menu.getMeals();
                    //iterating through the set
                    Iterator<Meal> it = meals.iterator();
                    while(it.hasNext()) {
                        //add all prices to the array list
                        list.add(it.next().getPrice());
                    }
                }
                //calculate the median of pricesArr
                double median;
                int[] pricesArr = new int[list.size()];
                int m = 0;
                //add all prices from the list to our array
                for(int j:list){
                    pricesArr[m] = j;
                    m++;
                }
                Arrays.sort(pricesArr);
                if (pricesArr.length % 2 == 0)
                    median = (double)pricesArr[pricesArr.length/2-1] ;
                else
                    median = (double) pricesArr[pricesArr.length/2];
                int out = (int)median;
                return new OptionalWithMessageVal(out);
            }

            @Override
            public String toString() {
                return "MedianPriceAnalyzer";
            }
        };
    }

    static Analyzer<Meal> createMinPriceMealAnalyzer() {
        return new Analyzer<Meal>() {
            @Override
            public OptionalWithMessage<Meal> analyze_unsafe(List<Menu> data) {
                ArrayList<Integer> list = new ArrayList(0);
                int min = Integer.MAX_VALUE;
                Meal mymeal = null;
                Meal minMeal = null;
                for (int i = 0; i < data.size(); i++) {
                    Menu menu = data.get(i);
                    //getMeals returns a set
                    Set<Meal> meals = menu.getMeals();
                    //iterating through the set
                    Iterator<Meal> it = meals.iterator();
                    while(it.hasNext()) {
                        //add all prices to the array list
                        mymeal = it.next();
                        list.add(mymeal.getPrice());
                        if(mymeal.getPrice()<min){
                            min = mymeal.getPrice();
                            minMeal = mymeal;
                        }
                    }
                }
                //calculate the median of pricesArr
                int[] pricesArr = new int[list.size()];
                int m = 0;
                //add all prices from the list to our array
                for(int j:list){
                    pricesArr[m] = j;
                    m++;
                }
                Arrays.sort(pricesArr);
                //got the lowest price. need to return the whole ass meal
                return new OptionalWithMessageVal(minMeal);
            }

            @Override
            public String toString() {
                return "MinPriceMealAnalyzer";
            }
        };
    }

    static Analyzer<Meal> createMaxPriceMealAnalyzer() {
        return new Analyzer<Meal>() {
            @Override
            public OptionalWithMessage<Meal> analyze_unsafe(List<Menu> data) {
                ArrayList<Integer> list = new ArrayList(0);
                int max = Integer.MIN_VALUE;
                Meal mymeal = null;
                Meal maxMeal = null;
                for (int i = 0; i < data.size(); i++) {
                    Menu menu = data.get(i);
                    //getMeals returns a set
                    Set<Meal> meals = menu.getMeals();
                    //iterating through the set
                    Iterator<Meal> it = meals.iterator();
                    while(it.hasNext()) {
                        //add all prices to the array list
                        mymeal = it.next();
                        list.add(mymeal.getPrice());
                        if(mymeal.getPrice()>max){
                            max = mymeal.getPrice();
                            maxMeal = mymeal;
                        }
                    }
                }
                //calculate the median of pricesArr
                int[] pricesArr = new int[list.size()];
                int m = 0;
                //add all prices from the list to our array
                for(int j:list){
                    pricesArr[m] = j;
                    m++;
                }
                Arrays.sort(pricesArr);
                //got the lowest price. need to return the whole ass meal
                return new OptionalWithMessageVal(maxMeal);
            }

            @Override
            public String toString() {
                return "MaxPriceMealAnalyzer";
            }
        };
    }

    static Analyzer<Integer> createTotalPriceAnalyzer() {
        return new Analyzer<Integer>() {
            @Override
            public OptionalWithMessage<Integer> analyze_unsafe(List<Menu> data)  {
                int totalPrice = 0;
                for (int i = 0; i < data.size(); i++) {
                    Menu menu = data.get(i);
                    //getMeals returns a set
                    Set<Meal> meals = menu.getMeals();
                    //iterating through the set
                    Iterator<Meal> it = meals.iterator();
                    while(it.hasNext())
                        //add the price of each meal to total price
                        totalPrice += it.next().getPrice();
                }
                return new OptionalWithMessageVal(totalPrice);
            }

            @Override
            public String toString() {
                return "TotalPriceAnalyzer";
            }
        };
    }

    /* Die folgenden Analyzer werden nicht getestet und muessen nicht implementiert werden.
     * Es sind lediglich Vorschlaege fuer weitere Analyzer fuer Aufgabenteil 3. */
    static Analyzer<Map<LocalDate, Double>> createAveragePricePerDayAnalyzer() {
        throw new UnsupportedOperationException();
    }

    static Analyzer<Map<LocalDate, Double>> createTotalPricePerDayAnalyzer() {
        throw new UnsupportedOperationException();
    }

    static Analyzer<Meal> createPopularityAnalyzer(int rank) {
        throw new UnsupportedOperationException();
    }

    static Analyzer<Meal> createPopularMealOnWeekdayAnalyzer(DayOfWeek dayOfWeek) {
        throw new UnsupportedOperationException();
    }

    static Analyzer<Map<Integer, Long>> createPriceRangeAnalyzer() {
        throw new UnsupportedOperationException();
    }

    static Analyzer<Map<LocalDate, Integer>> createAmountOfDishesPerDayAnalyzer() {
        throw new UnsupportedOperationException();
    }

    static Analyzer<Map<Meal, Long>> createRepetitionAnalyzer() {
        throw new UnsupportedOperationException();
    }
}
