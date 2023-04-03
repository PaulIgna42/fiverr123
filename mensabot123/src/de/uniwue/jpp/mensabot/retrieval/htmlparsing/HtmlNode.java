package de.uniwue.jpp.mensabot.retrieval.htmlparsing;

import java.util.*;

public interface HtmlNode {

    String getTagName();

    List<HtmlNode> getChildren();

    Map<String, String> getAttributes();

    String getText();

    String getInnerText();

    List<HtmlNode> getNodesByAttribute(String key, String value);

    void getNodesByAttribute(String key, String value, List<HtmlNode> previous);

    boolean hasAttribute(String key);

    String getAttribute(String key);

    public static HtmlNode createStandardHtmlNode(List<String> elements) throws HtmlParsingException {
        if (elements.size() < 2) {
            throw new HtmlParsingException("Size is smaller than 2");
        }
        if (HtmlParsingUtil.isStartTag(elements.get(0)).isEmpty()) {
            throw new HtmlParsingException("First element is not a StartTag");
        }
        if (HtmlParsingUtil.isEndTag(elements.get(elements.size() - 1)).isEmpty()) {
            throw new HtmlParsingException("Last element is not a LastTag");
        }
        if (!HtmlParsingUtil.isStartTag(elements.get(0)).get().equals(HtmlParsingUtil.isEndTag(elements.get(elements.size() - 1)).get())) {
            throw new HtmlParsingException("TagNames are different");
        }
        return new StandardHtmlNode(elements);
    }

    public static HtmlNode createVoidHtmlNode(String element) throws HtmlParsingException{
        //if element is an empty string, throw an exception
        if(element.equals(""))
            throw new HtmlParsingException("Element is an empty string.");
        //if element is not a startTag, throw an exception
        if(HtmlParsingUtil.isStartTag(element).isEmpty())
            throw new HtmlParsingException("Element is not a start tag.");
        //if the attribute of the node cannot be parsed, throw an exception
        if(HtmlParsingUtil.parseAttributeString(element).isEmpty())
            throw new HtmlParsingException("Attribute cannot be parsed.");
        return new VoidHtmlNode(element);
    }

    public static HtmlNode createTextHtmlNode(String text) {
        return new TextHtmlNode(text);
    }

    public static HtmlNode parseString(String string) throws HtmlParsingException{
        throw new UnsupportedOperationException();
    }
}
