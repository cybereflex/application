package cc.cybereflex.common.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class XmlParser {

    private static final Logger logger = LoggerFactory.getLogger(XmlParser.class);
    private static final SAXParser parser;

    static {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);
            parser = saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            logger.error("SAX parser init failed", e);
            throw new RuntimeException(e);
        }
    }


    public static void parse(InputStream inputStream, AbstractSAXHandler handler) {
        try {
            parser.parse(inputStream, handler);
        } catch (SAXException | IOException e) {
            logger.error("SAX parser parse failed", e);
            throw new RuntimeException(e);
        }
    }


    public static abstract class AbstractSAXHandler extends DefaultHandler {

        private final Deque<Element> elementStack = new ArrayDeque<>();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            Element element = Element.builder()
                    .uri(uri)
                    .localName(localName)
                    .qName(qName)
                    .attributes(attributes)
                    .build();

            elementStack.push(element);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            elementStack.pop();
        }

        protected Optional<Element> peekCurrentElement() {
            return Optional.ofNullable(elementStack.peek());
        }

        protected Optional<Element> peekParentElement(){
            Optional<Element> currentElement = peekCurrentElement();
            if (currentElement.isEmpty() || elementStack.size() < 2) {
                return Optional.empty();
            }
            Element[] elements = elementStack.toArray(Element[]::new);
            return Optional.of(elements[1]);
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Element {
        private String uri;
        private String localName;
        private String qName;
        private Attributes attributes;
    }


}
