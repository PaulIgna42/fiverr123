package de.uniwue.jpp.mensabot.dataclasses;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public interface Meal {
    String getName();
    int getPrice(); // in cents

    static Meal createMeal(String name, int price) {
        if (name == null)
            throw new IllegalArgumentException("Name must not be null!");
        if(name.equals(""))
            throw new IllegalArgumentException("Name must not be empty!");
        if(price < 0)
            throw new IllegalArgumentException("Price must not be of negative value!");

        return new Meal() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public int getPrice() {
                return price;
            }

            @Override
            public String toString(){
                double myprice = (double) price;
                myprice = myprice/100;
                Locale locale = Locale.GERMAN;
                NumberFormat nf = NumberFormat.getInstance(locale);
                nf.setMinimumFractionDigits(2);
                return name + " (" + nf.format(myprice) + "\u20ac" + ")";
            }

            @Override
            public boolean equals(Object obj) {
                if(obj == null)
                    return false;
                if(obj == this)
                    return true;
                if(!(obj instanceof Meal))
                    return false;
                return (((Meal) obj).getName().equals(name) && price == ((Meal) obj).getPrice());
            }

            @Override
            public int hashCode() {
                return Objects.hash(name,price);
            }
        };
    }

}