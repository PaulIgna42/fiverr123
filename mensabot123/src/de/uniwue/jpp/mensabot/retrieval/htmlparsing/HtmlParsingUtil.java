package de.uniwue.jpp.mensabot.retrieval.htmlparsing;

import de.uniwue.jpp.mensabot.util.html.CharacterReference;
import javafx.scene.chart.ValueAxis;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlParsingUtil {

    public static Optional<String> isStartTag(String tag) throws HtmlParsingException{

        if (tag.charAt(0) != '<')
            return Optional.empty();
        if (tag.charAt(0) == '<' && tag.charAt(1) == '/')
            return Optional.empty();
        if (tag.charAt(0) == '<' && tag.charAt(tag.length()-1) != '>')
            throw new HtmlParsingException("faulty tag");
        //get tag name
        StringBuilder out = new StringBuilder();
        if (tag.charAt(0) == '<' && tag.charAt(tag.length()-1) == '>') {
            for (int i = 1; i <= tag.length() - 2; i++) {
                if (tag.charAt(i) != ' ' && tag.charAt(i) != '/')
                    out.append(tag.charAt(i));
                else
                    break;
            }
        }
        if (out.toString().equals(""))
            throw new HtmlParsingException("tag name missing");
        return Optional.of(out.toString());
    }

    public static Optional<String> isEndTag(String tag) throws HtmlParsingException{
        if (tag.charAt(0) != '<')
            return Optional.empty();
        if (tag.charAt(0) == '<' && tag.charAt(1) != '/')
            return Optional.empty();
        if (tag.charAt(0) == '<' && tag.charAt(tag.length()-1) != '>' && tag.charAt(1) == '/')
            throw new HtmlParsingException("faulty tag");
        //get tag name
        StringBuilder out = new StringBuilder();
        if (tag.charAt(0) == '<' && tag.charAt(1) == '/' && tag.charAt(tag.length()-1) == '>') {
            for (int i = 2; i <= tag.length() - 2; i++) {
                if (tag.charAt(i) != ' ')
                    out.append(tag.charAt(i));
                else
                    break;
            }
        }
        if (out.toString().equals(""))
            throw new HtmlParsingException("tag name missing");
        return Optional.of(out.toString());
    }

    public static boolean isVoidElement(String tagName){
        if(tagName.equals("area") ||tagName.equals("base") ||tagName.equals("br") ||tagName.equals("col") ||tagName.equals("command"))
            return true;
        if(tagName.equals("embed") ||tagName.equals("hr") ||tagName.equals("img") ||tagName.equals("input") ||tagName.equals("keygen"))
            return true;
        return tagName.equals("link") || tagName.equals("meta") || tagName.equals("param") || tagName.equals("source") || tagName.equals("track") || tagName.equals("wbr");
    }

    public static String getAttributeString(String startTag){

        String tagContent = startTag.substring(1,startTag.length()-1);
        String[] parts = tagContent.split("\\s+");
        //figure out at what index the tag name ends
        int index = 0;
        for (int i = 0; i < tagContent.length()-1; i++) {
            if (tagContent.charAt(i) == ' ' && tagContent.charAt(i+1) != ' ')
                break;
            index++;
        }
        index++;
        if(parts.length == 1)
            return "";
        if(parts.length == 2 && parts[1].equals("/"))
            return "";
        StringBuilder out = new StringBuilder();
        for(int i=index; i <= tagContent.length()-1;i++){
            out.append(tagContent.charAt(i));
        }

        if(out.charAt(out.length()-1) == '/')//delete the last "/"
            out.deleteCharAt(out.length()-1);
        while(out.charAt(out.length()-1) == ' ')//delete the last spaces
            out.deleteCharAt(out.length()-1);
        return out.toString();
    }

    public static Map<String, String> parseAttributeString(String attributeString) throws HtmlParsingException {
        Map<String, String> result = new HashMap<>();
        List<String> validName = List.of("\"", "'", ">", "/", "=", " ");
        int i = 0, j = 0;
        while (i < attributeString.length()) {

            while (attributeString.charAt(i) == ' ' || attributeString.charAt(i) == '\n') {
                i++;
                j++;
            }


            while (attributeString.charAt(j) != '=' && (attributeString.charAt(j) != ' ')) {
                if (j >= attributeString.length() - 1) {
                    String key = attributeString.substring(i, j + 1).trim();
                    result.put(key, null);
                    return result;
                }
                j++;
            }

            while (attributeString.charAt(j) != '=' && !checkIfAlphanumeric(attributeString.charAt(j))) j++;

            if (checkIfAlphanumeric(attributeString.charAt(j))) {
                String key = attributeString.substring(i, j).trim();
                result.put(key, null);
                i = j;
                continue;
            }

            String key = attributeString.substring(i, j).trim();
            for (int p = 0; p < validName.size(); p++) {
                if (key.contains(validName.get(p))) {
                    throw new HtmlParsingException("something went wrong");
                }
            }

            j++;
            i = j;

            if (i >= attributeString.length()) {
                throw new HtmlParsingException("something went wrong");
            }

            while (attributeString.charAt(i) == ' ' || attributeString.charAt(i) == '\n') {
                i++;
                j++;
            }

            if (attributeString.charAt(j) == '\'' || attributeString.charAt(j) == '\"') {

                char temp = attributeString.charAt(j);
                i = j;
                i++;
                try {
                    while (attributeString.charAt(i) != temp)
                        i++;
                } catch (Exception ex) {
                    throw new HtmlParsingException("something went wrong");
                }
                String name = attributeString.substring(j + 1, i);
                result.put(key, name);
                i++;
                j = i;
                continue;
            }

            while ((j < (attributeString.length())) && (attributeString.charAt(j) != ' ')) {
                if(attributeString.charAt(j) == '\'' || attributeString.charAt(j) == '\"') {
                    throw new HtmlParsingException("something went wrong");
                }
                j++;

            }

            String name = attributeString.substring(i, j).trim().replaceAll("\"", "");

            result.put(key, name);

            i = j;
        }

        return result;
    }

    public static boolean checkIfAlphanumeric(char character) {
        if ((character >= '0' & character <= '9') || (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z')) {
            return true;
        } else {
            return false;
        }
    }



    public static Map<String, String> parseAttributesFromStartTag(String startTag) throws HtmlParsingException{
        //just like pAS but without end tag

        String attributes = getAttributeString(startTag);
        return parseAttributeString(attributes);
    }

    public static List<String> splitInElements(String text) {
        boolean insideQuotes = false;
        List<String> elements = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '<' && !insideQuotes) {
                if (sb.length() > 0) {
                    elements.add(sb.toString());
                    sb = new StringBuilder();
                }
                sb.append(ch);
            } else if (ch == '>' && !insideQuotes) {
                sb.append(ch);
                elements.add(sb.toString());
                sb = new StringBuilder();
            } else if (ch == '\"' && (i == 0 || text.charAt(i - 1) != '\\')) {
                insideQuotes = !insideQuotes;
                sb.append(ch);
            } else {
                sb.append(ch);
            }
        }
        if (sb.length() > 0) {
            elements.add(sb.toString());
        }
        return elements;
    }

    public static void cleanElements(String startTag, String endTag, List<String> elements) throws HtmlParsingException{
        try{
            boolean done = false;
            while(!done){
                int end = -1;
                int endFoundCounter = 0;
                boolean endFound = false;
                boolean deleted = false;
                int start = -1;
                int temp = -1;
                for (int i = elements.size()-1; i > -1; i--){
                    String element = elements.get(i);
                    if (element.contains(endTag) && !endFound){
                        end = i;
                        endFoundCounter = 1;
                        endFound = true;
                    }
                    if (element.contains(startTag)){
                        temp = i;
                    }
                    if (temp == end && temp != -1){
                        elements.remove(temp);
                        deleted = true;
                        break;
                    }
                    if (temp != start){
                        start = temp;
                    }
                    if (element.contains(endTag) && endFoundCounter == 1 && start != -1){
                        start = temp;
                        break;
                    }
                }
                if (!deleted){
                    if ((start != -1 && end == -1)){
                        throw new HtmlParsingException();
                    }
                    else if (start == end && start != -1){
                        elements.remove(start);
                    } else if (start == end && start == -1){
                        done = true;
                    }
                    else if (elements.size()>1) {
                        elements.subList(start, end+1).clear();
                        boolean endFound2 = false;
                        boolean startFound2 = false;
                        for (int i = elements.size()-1; i > -1; i--) {
                            String element = elements.get(i);
                            if (element.contains(endTag) && !endFound){
                                endFound2 = true;
                            }
                            if (element.contains(startTag)){
                                startFound2 = true;
                            }
                            if (i == 0 &&(!startFound2 && !endFound2)){
                                done = true;
                            }
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e){
            throw new HtmlParsingException();
        }
    }




    public static void trimElements(List<String> elements) throws HtmlParsingException{

        //delete the first element of the list if isStartTag returns false in a loop
        while(!elements.isEmpty() && !isStartTag(elements.get(0)).isPresent()){
            elements.remove(0);
        }
        //delete the last element of the list if isEndTag returns false in a loop
        while(!elements.isEmpty() && !isEndTag(elements.get(elements.size()-1)).isPresent()){
            elements.remove(elements.size()-1);
        }
    }


    public static String replaceCharacterReferencesInText(String input){
        try{
            Pattern pattern = Pattern.compile("(&#(\\d*);&#(\\d*);)|(&#[xX](\\w*\\d*);&#[xX](\\w*\\d*);)|(&(\\w+);)|(&#(\\d*);)|(&#[xX](\\w*\\d*);)");
            Matcher matcher = pattern.matcher(input);
            Map<String, CharacterReference> map = CharacterReference.loadCharacterReferences();
            Collection<CharacterReference> arr = CharacterReference.loadCharacterReferences().values();
            Set<String> set = map.keySet();
            List<CharacterReference> list = new ArrayList<>(arr);
            List<String> list2 = new ArrayList<>(set);

            while (matcher.find()){
                System.out.println(matcher.group());
                if (!(matcher.group(1) == null)){
                    List<Integer> save = new LinkedList<>();
                    save.add(Integer.parseInt(matcher.group(2)));
                    save.add(Integer.parseInt(matcher.group(3)));
                    String symbol = finder(list, save, map);
                    input = input.replace(matcher.group(1), symbol);
                }
                else if (!(matcher.group(4) == null)) {
                    List<Integer> save = new LinkedList<>();
                    int hexadeci1 = Integer.parseInt(matcher.group(5), 16);
                    int hexadeci2 = Integer.parseInt(matcher.group(6), 16);
                    save.add(hexadeci1); save.add(hexadeci2);
                    String symbol = finder(list, save, map);
                    input = input.replace(matcher.group(4), symbol);
                }
                else if (!(matcher.group(7) == null)) {
                    System.out.println("Found the symbol! It is: " + matcher.group(7));
                    int tracker = 0;
                    for (int i = 0; i < map.size(); i++){
                        if (list2.get(i).equals(matcher.group(7))){
                            String symbol = list.get(tracker).getCharacters();
                            input = input.replace(matcher.group(7), symbol);
                            break;
                        }
                        tracker = tracker + 1;
                    }
                } else if (!(matcher.group(9) == null)) {
                    List<Integer> save = new LinkedList<>();
                    save.add(Integer.parseInt(matcher.group(10)));
                    String symbol = finder(list, save, map);
                    input = input.replace(matcher.group(9), symbol);
                } else if (!(matcher.group(11) == null)) {
                    List<Integer> save = new LinkedList<>();
                    int hexadeci1 = Integer.parseInt(matcher.group(12), 16);
                    save.add(hexadeci1);
                    String symbol = finder(list, save, map);
                    input = input.replace(matcher.group(11), symbol);
                }
            }
            //g1: Zahlen, zwei - g2: Inhalt der 1. Zahl - g3 Zahl der 2. Zahl
            //g4: Hexa, zwei - g5: Inhalt Hexa 1. Zahl - g6: Inhalt Hexa 2. Zahl - g7: Name
            //g8: Inhalt Name - g9: Zahl, einzeln - g10: Inhalt Zahl einzeln- g11:Einzelnes Hexa
            //g12: Inhalt einzelnes Hexa
            return input;
        } catch (NumberFormatException e){
            return input;
        }
    }

    public static String finder(List<CharacterReference> list, List<Integer> numbers, Map map){
        int tracker = 0;
        String res = "";
        System.out.println("numbers is: " + numbers);
        if (numbers.size() == 1){
            for (int i = 0; i < map.size(); i++){//Assumes list only has one entry
                if (list.get(i).getCodepoints().equals(numbers)){
                    res = list.get(tracker).getCharacters();
                    System.out.println(res);
                    break;
                }
                tracker = tracker + 1;
            }
        }
        else {//assumes list has 2 entries
            boolean found = false;//if a list with two has been found
            for (int i = 0; i < map.size(); i++){
                if (list.get(i).getCodepoints().equals(numbers)){
                    res = list.get(tracker).getCharacters();
                    found = true;
                    break;
                }
                tracker = tracker + 1;
            }
            tracker = 0;
            if (!found){
                List<Integer> number1 = new ArrayList<>();
                List<Integer> number2 = new ArrayList<>();
                number1.add(numbers.get(0));
                number2.add(numbers.get(1));
                String temp1 = "";
                String temp2 = "";
                for (int i = 0; i < map.size(); i++){//Assumes list only has one entry
                    if (list.get(i).getCodepoints().equals(number1)){
                        temp1 = list.get(tracker).getCharacters();
                        System.out.println(temp1);
                        break;
                    }
                    tracker = tracker + 1;
                }
                tracker = 0;
                for (int i = 0; i < map.size(); i++){//Assumes list only has one entry
                    if (list.get(i).getCodepoints().equals(number2)){
                        temp2 = list.get(tracker).getCharacters();
                        System.out.println(temp2);
                        break;
                    }
                    tracker = tracker + 1;
                }
                StringBuilder fuck = new StringBuilder();
                fuck.append(temp1); fuck.append(temp2);
                System.out.println("fuck is: " + fuck);
                res = fuck.toString();
            }
        }
        return res;
    }

}