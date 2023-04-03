package de.uniwue.jpp.mensabot.sending.formatting;

import de.uniwue.jpp.errorhandling.OptionalWithMessageVal;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.sending.formatting.analyze.Analyzer;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;

public interface Formatter {

    OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus);

    static Formatter createSimpleFormatter() {
        return new Formatter() {

            public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {
                //if one of the arguments is null, return an error message
                if(latestMenu == null || allMenus == null)
                    throw new NullPointerException("At least one of the arguments was null");

                StringBuilder out = new StringBuilder();
                out.append("Essen am ").append(latestMenu.getDate()).append(":").append("\n");
                Set<Meal> meals = latestMenu.getMeals();
                Iterator<Meal> it = meals.iterator();
                //converting the price to german format
                Locale locale = Locale.GERMAN;
                NumberFormat nf = NumberFormat.getInstance(locale);
                nf.setMinimumFractionDigits(2);
                while(it.hasNext()) {
                    Meal meal = it.next();
                    out.append(meal.getName()).append(" (");
                    out.append(nf.format((double) meal.getPrice()/100)).append("\u20ac");
                    out.append(")\n");
                }
                //deleting the last empty line in out
                int last = out.lastIndexOf("\n");
                if(last>=0){
                    out.delete(last,out.length());
                }
                return new OptionalWithMessageVal(out.toString());
            }
            public String toString() {
                return "SimpleFormatter";
            }
        };

    }

    static Formatter createSimpleFormatter(String info) {
        return new Formatter() {

            public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {
                //if one of the arguments is null, return an error message
                if(latestMenu == null || allMenus == null)
                    throw new NullPointerException("At least one of the arguments was null");

                StringBuilder out = new StringBuilder();
                out.append("Essen am ").append(latestMenu.getDate()).append(" (").append(info).append(")").append(":").append("\n");
                Set<Meal> meals = latestMenu.getMeals();
                Iterator<Meal> it = meals.iterator();
                //converting the price to german format
                Locale locale = Locale.GERMAN;
                NumberFormat nf = NumberFormat.getInstance(locale);
                nf.setMinimumFractionDigits(2);
                while(it.hasNext()) {
                    Meal meal = it.next();
                    out.append(meal.getName()).append(" (");
                    out.append(nf.format((double) meal.getPrice()/100)).append("\u20ac");
                    out.append(")\n");
                }
                //deleting the last empty line in out
                int last = out.lastIndexOf("\n");
                if(last>=0){
                    out.delete(last,out.length());
                }
                return new OptionalWithMessageVal(out.toString());
            }
            public String toString() {
                return "SimpleFormatter";
            }
        };
    }

    static Formatter createSimpleMealFormatter() {
        return new Formatter() {

            public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {

                if(latestMenu == null || allMenus == null)
                    throw new NullPointerException("At least one of the arguments was null");

                StringBuilder out = new StringBuilder();
                Set<Meal> meals = latestMenu.getMeals();
                Iterator<Meal> it = meals.iterator();
                //converting the price to german format
                Locale locale = Locale.GERMAN;
                NumberFormat nf = NumberFormat.getInstance(locale);
                nf.setMinimumFractionDigits(2);
                while(it.hasNext()) {
                    Meal meal = it.next();
                    out.append(meal.getName()).append(" (");
                    out.append(nf.format((double) meal.getPrice()/100)).append("\u20ac");
                    out.append(")\n");
                }
                //deleting the last empty line in out
                int last = out.lastIndexOf("\n");
                if(last>=0){
                    out.delete(last,out.length());
                }
                return new OptionalWithMessageVal(out.toString());
            }

            public String toString() {
                return "SimpleMealFormatter";
            }
        };
    }

    static Formatter createFormatterFromAnalyzer(String headline, Analyzer<?> analyzer) {
        if(headline == null)
            throw new IllegalArgumentException("Illegal argument!");
        if (headline.equals(""))
            throw new IllegalArgumentException("Illegal argument!");
        if(analyzer == null)
            throw new IllegalArgumentException("Illegal argument!");
        return new Formatter() {

            public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {

                if(latestMenu == null || allMenus == null)
                    throw new NullPointerException("At least one of the arguments was null");

                var myMenus = allMenus.get();
                StringBuilder out = new StringBuilder();
                out.append(headline).append(":");

                if(myMenus.isEmpty()) {
                    out.append("Analyzing is not possible");
                    //return new OptionalWithMessageVal(out.toString());
                }
                else {
                    if (analyzer.analyze(myMenus.get()).isEmpty()) {
                        out.append("Analyzing is not possible");
                        // return new OptionalWithMessageVal(out.toString());
                    }
                    else
                        out.append(analyzer.analyze(myMenus.get()).get());
                }
                out.append("\n");
                return new OptionalWithMessageVal(out.toString());
            }

            public String toString() {
                return "FormatterFromAnalyzer";
            }
        };
    }

    static Formatter createFormatterFromAnalyzer(String headline, Analyzer<?> analyzer, String name) {
        if(headline == null)
            throw new IllegalArgumentException("Illegal argument!");
        if (headline.equals(""))
            throw new IllegalArgumentException("Illegal argument!");
        if(analyzer == null)
            throw new IllegalArgumentException("Illegal argument!");
        return new Formatter() {

            public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {

                if(latestMenu == null || allMenus == null)
                    throw new NullPointerException("At least one of the arguments was null");

                var myMenus = allMenus.get();
                StringBuilder out = new StringBuilder();
                out.append(headline).append(":");

                if(myMenus.isEmpty()) {
                    out.append("Analyzing is not possible");
                    //return new OptionalWithMessageVal(out.toString());
                }
                else {
                    if (analyzer.analyze(myMenus.get()).isEmpty()) {
                        out.append("Analyzing is not possible");
                        // return new OptionalWithMessageVal(out.toString());
                    }
                    else
                        out.append(analyzer.analyze(myMenus.get()).get());
                }
                out.append("\n");
                return new OptionalWithMessageVal(out.toString());
            }

            public String toString() {
                return name;
            }
        };
    }

    static Formatter createComplexFormatter(List<String> headlines, List<Analyzer<?>> analyzers) {
        if(headlines == null || headlines.isEmpty())
            throw new IllegalArgumentException("Illegal argument!");
        if(analyzers == null || analyzers.isEmpty())
            throw new IllegalArgumentException("Illegal argument!");
        if(headlines.size() != analyzers.size())
            throw new IllegalArgumentException("There must be a headline for each analyzer!");

        for(String headline: headlines) {
            if (headline == null)
                throw new IllegalArgumentException("Illegal argument!");
            if (headline.equals(""))
                throw new IllegalArgumentException("Illegal argument!");
        }
        for(Analyzer analyzer:analyzers)
            if(analyzer == null)
                throw new IllegalArgumentException("Illegal argument!");
        return new Formatter() {

            public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {
                if(latestMenu == null || allMenus == null)
                    throw new NullPointerException("At least one of the arguments was null");

                var myMenus = allMenus.get();
                StringBuilder out = new StringBuilder();
                Iterator<String> headlineIterator = headlines.iterator();
                Iterator<Analyzer<?>> analyzerIterator = analyzers.iterator();

                while(headlineIterator.hasNext() && analyzerIterator.hasNext()){
                    out.append(headlineIterator.next()).append(":");
                    if(myMenus.isEmpty()) {
                        out.append("Analyzing is not possible");
                        out.append("\n");
                    }
                    else {
                        var nextUp = analyzerIterator.next();
                        if(nextUp.analyze(myMenus.get()).isEmpty())
                            out.append("Analyzing is not possible");
                        else
                            out.append(nextUp.analyze(myMenus.get()).get());
                        out.append("\n");
                    }
                }
                //delete the last empty line only when it's a single object I guess
                if(headlines.size() == 1) {
                    int last = out.lastIndexOf("\n");
                    if (last >= 0)
                        out.delete(last, out.length());
                }
                return new OptionalWithMessageVal(out.toString());
            }


            public String toString() {
                return "ComplexFormatter";
            }
        };
    }

    static Formatter createFormatterFromFormat(String format, List<Analyzer<?>> analyzers, String name) {

        //check for illegal arguments
        if(name == null || name.equals(""))
            throw new IllegalArgumentException("Illegal name argument!");
        if(format == null || format.equals(""))
            throw new IllegalArgumentException("Illegal format argument!");
        if(!format.contains("$"))
            throw new IllegalArgumentException("Format must contain $ signs!");
        if(analyzers == null || analyzers.isEmpty())
            throw new IllegalArgumentException("Illegal analyzer argument!");

        //count the amount of $ in format
        int totalCharacters = 0;
        char temp;
        for (int i = 0; i < format.length(); i++) {
            temp = format.charAt(i);
            if (temp == '$')
                totalCharacters++;
        }
        if(analyzers.size() != totalCharacters)
            throw new IllegalArgumentException("There must be a $ for each analyzer");
        return new Formatter() {

            public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {

                if(latestMenu == null || allMenus == null)
                    throw new NullPointerException("At least one of the arguments was null");

                var myMenus = allMenus.get();
                StringBuilder out = new StringBuilder();
                Iterator<Analyzer<?>> analyzerIterator = analyzers.iterator();

                String[] parts = format.split("[$]");
                for (int i = 0; i < parts.length-1; i++){
                    out.append(parts[i]);
                    var nextUp = analyzerIterator.next();
                    if(myMenus.isEmpty())
                        out.append("Analyzing is not possible");
                    else {
                        if (nextUp.analyze(myMenus.get()).isEmpty())
                            out.append("Analyzing is not possible");
                        else
                            out.append(nextUp.analyze(myMenus.get()).get());
                    }
                }
                out.append(parts[parts.length-1]);
                if (analyzerIterator.hasNext()){
                    var nextUp = analyzerIterator.next();
                    if(myMenus.isEmpty())
                        out.append("Analyzing is not possible");
                    else
                    if (nextUp.analyze(myMenus.get()).isEmpty())
                        out.append("Analyzing is not possible");
                    else
                        out.append(nextUp.analyze(myMenus.get()).get());
                }
                return new OptionalWithMessageVal(out.toString());
            }


            public String toString() {
                return name;
            }
        };
    }


    /* Die folgenden Formatter werden nicht getestet und muessen auch nicht implementiert werden.
     * Es sind lediglich Vorschlaege für Aufgabenteil 3. */
    static Formatter createHiddenFormatter() {
        throw new UnsupportedOperationException();
    }

    static Formatter createFirstWordFormatter() {
        throw new UnsupportedOperationException();
    }

    static Formatter createShortFormatter() {
        throw new UnsupportedOperationException();
    }

    static Formatter createPricelessFormatter() {
        throw new UnsupportedOperationException();
    }

    static Formatter createSimpleTotalFormatter() {
        throw new UnsupportedOperationException();
    }

    static Formatter createAlphabeticalOrderFormatter() {
        throw new UnsupportedOperationException();
    }

}
