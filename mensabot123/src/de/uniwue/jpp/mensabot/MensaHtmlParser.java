package de.uniwue.jpp.mensabot;

import com.fasterxml.jackson.databind.node.TextNode;
import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.mensabot.retrieval.Parser;
import de.uniwue.jpp.mensabot.retrieval.htmlparsing.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MensaHtmlParser implements Parser {

    LocalDate date;

    public MensaHtmlParser(LocalDate date) {
        this.date = date;
    }

    public static MensaHtmlParser createMensaHtmlParser(LocalDate date) {
        return new MensaHtmlParser(date);
    }

    @Override
    public OptionalWithMessage<Menu> parse(String fetched) {
        HtmlNode rootNode = buildHtmlTree(fetched); // Assuming buildHtmlTree is a method that creates the tree structure

        String dateString = date.format(DateTimeFormatter.ofPattern("EEEE dd.MM."));
        List<HtmlNode> dayNodes = rootNode.getNodesByAttribute("data-day", dateString);
        /*
        if (dayNodes.isEmpty()) {
            return "Could not parse html!";
        }
        */
        HtmlNode dayNode = dayNodes.get(0);
        List<HtmlNode> menuNodes = dayNode.getNodesByAttribute("class", "menu");
        /*
        if (menuNodes.isEmpty()) {
            return "No meals found for today!";
        }
        */
        Set<Meal> meals = new HashSet<>();
        for (HtmlNode menuNode : menuNodes) {
            HtmlNode titleNode = menuNode.getNodesByAttribute("class", "title").get(0);
            String dishName = titleNode.getInnerText().trim();
            List<HtmlNode> priceNodes = menuNode.getNodesByAttribute("class", "price");
            int price;
            if (priceNodes.isEmpty()) {
                price = 0;
            } else {
                HtmlNode priceNode = priceNodes.get(0);
                String priceString = priceNode.getAttribute("data-default");
                try {
                    price = Integer.parseInt(priceString);
                } catch (NumberFormatException e) {
                    price = 0;
                }
            }

            meals.add(Meal.createMeal(dishName,price)); // Assuming Dish is a class representing a single dish with a name and a price
        }

        return OptionalWithMessage.of(Menu.createMenu(date,meals)); // Assuming Menu is a class representing a collection of dishes
    }
        //extract the dishes from the fetched html
        //parse the document using the html tree structure and the methods of the HtmlNode interface
        private HtmlNode buildHtmlTree(String fetched)  {
            List<String> elements = HtmlParsingUtil.splitInElements(fetched); // Assuming splitInElements is a method that splits the fetched HTML into elements
            return buildNode(elements);
        }

    private HtmlNode buildNode(List<String> elements) {
        if (elements.size() == 1 && !elements.get(0).startsWith("<")) {
            return new TextHtmlNode(elements.get(0));
        }
        //implement isVoidNode
        /*
        if (isVoidNode(elements)) {
            return new VoidHtmlNode(elements.get(0));
        }
         */
        //handle exception somehow
        return null;
        //return new StandardHtmlNode(elements);
    }

}
