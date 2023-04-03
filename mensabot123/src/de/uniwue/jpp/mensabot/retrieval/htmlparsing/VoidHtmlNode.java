package de.uniwue.jpp.mensabot.retrieval.htmlparsing;

import java.util.*;

public class VoidHtmlNode implements HtmlNode {

    private String element;
    private String tagName;
    private Map<String, String> attributes;

    public VoidHtmlNode(String element) {

        try {
            Optional<String> startTag = HtmlParsingUtil.isStartTag(element);
            if (startTag.isPresent()) {
                tagName = startTag.get();
                attributes = HtmlParsingUtil.parseAttributesFromStartTag(element);
            } else {
                throw new HtmlParsingException("Invalid HTML element: " + element);
            }
        } catch (HtmlParsingException e) {
            e.printStackTrace();
        }
        this.element = element;
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public List<HtmlNode> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String getText() {
        return element;
    }

    @Override
    public String getInnerText() {
        return "";
    }

    @Override
    public List<HtmlNode> getNodesByAttribute(String key, String value) {
        List<HtmlNode> nodes = new ArrayList<>();
        getNodesByAttribute(key, value, nodes);
        return nodes;
    }

    @Override
    public void getNodesByAttribute(String key, String value, List<HtmlNode> previous) {
        if (hasAttribute(key) && value!=null && value.equals(this.getAttribute(key))) {
            previous.add(this);
        }
        for (HtmlNode childNode : getChildren()) {
            childNode.getNodesByAttribute(key, value, previous);
        }
    }

    @Override
    public boolean hasAttribute(String key) {
        if (attributes.containsKey(key)) {
            return true;
        }
        return false;
    }

    @Override
    public String getAttribute(String key) {
        if (attributes.containsKey(key)) {
            return attributes.get(key);
        }
        return null;
    }

    @Override
    public String toString() {
        return getText();
    }
}