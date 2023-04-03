package de.uniwue.jpp.mensabot.retrieval.htmlparsing;

import java.util.*;

public class StandardHtmlNode implements HtmlNode {

    private List<String> elements;
    private String tagName;
    private Map<String, String> attributes;
    private List<HtmlNode> children = new ArrayList<>();

    public StandardHtmlNode(List<String> elements) throws HtmlParsingException {
        if (elements.size() < 2) {
            throw new HtmlParsingException("Size is smaller than 2");
        }
        if (HtmlParsingUtil.isStartTag(elements.get(0)).isEmpty()) {
            throw new HtmlParsingException("First element is not a StartTag");
        }
        if (HtmlParsingUtil.isEndTag(elements.get(elements.size() - 1)).isEmpty()) {
            throw new HtmlParsingException("Last element is not an EndTag");
        }
        if (!HtmlParsingUtil.isStartTag(elements.get(0)).get().equals(HtmlParsingUtil.isEndTag(elements.get(elements.size() - 1)).get())) {
            throw new HtmlParsingException("TagNames are different");
        }

        //if elements contains a void tag with a corresponding end tag with the same name, throw an exception

        for (int i = 0; i < elements.size(); i++) {
            //if the current element is a void tag
            if (isVoidTag(elements.get(i))) {
                String tagName = extractTagName(elements.get(i));
                for (int j = i + 1; j < elements.size(); j++) {
                    //if the current element is an end tag with the same name as the void tag
                    String tagName2 = extractTagName(elements.get(j));
                    if(tagName2.equals(tagName) && elements.get(j).charAt(1) == '/') {
                        throw new HtmlParsingException("Void tag with corresponding end tag found.");
                    }
                }
            }
        }

        this.elements = new ArrayList<>(elements);
        String element = elements.get(0);
        tagName = HtmlParsingUtil.isStartTag(element).get();
        attributes = HtmlParsingUtil.parseAttributesFromStartTag(element);

        //the list of children represents the elements between the first and the last element
        List<String> children = new ArrayList<>(elements);
        children.remove(children.size() - 1);
        children.remove(0);


        //NODE CREATION

        //iterate over the children and create the corresponding HtmlNodes
        for (String child : children){
            //if the child is a void tag, create a VoidHtmlNode
            if(isVoidTag(child)){
                this.children.add(new VoidHtmlNode(child));
            }
            //if the child is a start tag, create a StandardHtmlNode
            else if(HtmlParsingUtil.isStartTag(child).isPresent()){
                List<String> childrenOfChild = new ArrayList<>();
                childrenOfChild.add(child);
                //find the corresponding end tag
                for (int i = children.indexOf(child) + 1; i < children.size(); i++) {
                    childrenOfChild.add(children.get(i));
                    if(HtmlParsingUtil.isEndTag(children.get(i)).isPresent() && HtmlParsingUtil.isEndTag(children.get(i)).get().equals(HtmlParsingUtil.isStartTag(child).get())){
                        break;
                    }
                }
                this.children.add(new StandardHtmlNode(childrenOfChild));
            }
            //if the child is a text node, create a TextHtmlNode
            else if(HtmlParsingUtil.splitInElements(child).size() ==1){
                this.children.add(new TextHtmlNode(child));
            }
        }
    }


    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public List<HtmlNode> getChildren() {
        return children;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (String elem : elements) {
            sb.append(elem);
        }
        return sb.toString();
    }

    @Override
    public String getInnerText() {
        StringBuilder sb = new StringBuilder();
        for (HtmlNode childNode : children) {
            sb.append(childNode.getText());
        }
        return sb.toString();
    }

    @Override
    public List<HtmlNode> getNodesByAttribute(String key, String value) {
        List<HtmlNode> nodes = new ArrayList<>();
        getNodesByAttribute(key, value, nodes);
        return nodes;
    }

    @Override
    public void getNodesByAttribute(String key, String value, List<HtmlNode> previous) {
        if (hasAttribute(key) && value != null && value.equals(this.getAttribute(key))) {
            previous.add(this);
        }
        for (HtmlNode childNode : getChildren()) {
            childNode.getNodesByAttribute(key, value, previous);
        }
    }

    @Override
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    @Override
    public String getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public String toString() {
        return getText();
    }

    private static final Set<String> VOID_TAGS = new HashSet<>(Arrays.asList(
            "area", "base", "br", "col", "embed", "hr", "img", "input", "link", "meta", "param", "source", "track", "wbr"
    ));

    public static boolean isVoidTag(String element) throws HtmlParsingException {
        if (HtmlParsingUtil.isStartTag(element).isPresent()) {
            String tagName = extractTagName(element);
            return VOID_TAGS.contains(tagName);
        }
        return false;
    }

    public static String extractTagName(String element) {
        if (element == null || element.length() < 2) {
            return "";
        }

        int startIndex = element.startsWith("</") ? 2 : 1;
        int endIndex = element.length() - 1;

        for (int i = startIndex; i < element.length(); i++) {
            char ch = element.charAt(i);
            if (Character.isWhitespace(ch) || ch == '>' || ch == '/') {
                endIndex = i;
                break;
            }
        }

        return element.substring(startIndex, endIndex);
    }
}