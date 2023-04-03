package de.uniwue.jpp.mensabot.retrieval.htmlparsing;

import java.util.List;
import java.util.Map;

public class TextHtmlNode implements HtmlNode{

    String text;

    public TextHtmlNode(String text) {
        this.text = text;
    }

    @Override
    public String getTagName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<HtmlNode> getChildren() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getText() {
        return HtmlParsingUtil.replaceCharacterReferencesInText(text);
    }

    @Override
    public String getInnerText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<HtmlNode> getNodesByAttribute(String key, String value) {
        //return an empty list
        return List.of();
    }

    @Override
    public void getNodesByAttribute(String key, String value, List<HtmlNode> previous) {

    }

    @Override
    public boolean hasAttribute(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAttribute(String key) {
        throw new UnsupportedOperationException();
    }


    //rewrite toString() method
    @Override
    public String toString() {
        return text;
    }
}
