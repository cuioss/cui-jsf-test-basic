/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.renderer.util;

import de.cuioss.tools.io.IOStreams;
import lombok.experimental.UtilityClass;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.xml.XMLConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static java.util.Objects.requireNonNull;

/**
 * Helper class providing convenience methods for dealing with Dom trees.
 *
 * @author Oliver Wolff
 */
@UtilityClass
public class DomUtils {

    /**
     * The root element for creating {@link Document}s. This ensures correct
     * structure regarding xml only having one root-element
     */
    public static final String ROOT_TEMPLATE = "<root>%s</root>";

    /**
     * Creates an instance of {@link Document} for the given htmlString. It always
     * uses {@link #ROOT_TEMPLATE} as the root element.
     *
     * @param htmlString must not be null
     * @return the created {@link Document} with {@link #ROOT_TEMPLATE} as the root
     * element.
     */
    public static Document htmlStringToDocument(final String htmlString) {
        requireNonNull(htmlString);
        final var wrappedInput = ROOT_TEMPLATE.formatted(htmlString);
        try (var input = IOStreams.toInputStream(wrappedInput)) {
            var saxBuilder = new SAXBuilder();
            saxBuilder.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            saxBuilder.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            return saxBuilder.build(input);
        } catch (JDOMException | IOException e) {
            throw new IllegalArgumentException("Unable to parse given String, due to ", e);
        }
    }

    /**
     * Extracts all attributes with the given name. The method will recursively
     * check all children as well.
     *
     * @param element       to be checked, must not be null.
     * @param attributeName to be looked for, must not be null nor empty.
     * @return a {@link List} with the found attributes, never null but may be
     * empty.
     */
    public static List<Attribute> filterForAttribute(final Element element, final String attributeName) {
        requireNonNull(element);
        requireNonNull(emptyToNull(attributeName));
        List<Attribute> found = new ArrayList<>();
        var current = element.getAttribute(attributeName);
        if (null != current) {
            found.add(current);
        }
        for (Element child : element.getChildren()) {
            found.addAll(filterForAttribute(child, attributeName));
        }
        return found;
    }

    /**
     * Extracts all attributes with the given name and the attribute-value
     * containing the given String. The method will recursively check all children
     * as well.
     *
     * @param element            to be checked, must not be null.
     * @param attributeName      to be looked for, must not be null nor empty.
     * @param attributeValuePart the string of the attribute value to be filtered
     *                           for.
     * @return a {@link List} with the found attributes, never null but may be
     * empty.
     */
    public static List<Attribute> filterForAttributeContainingValue(final Element element, final String attributeName,
        final String attributeValuePart) {
        requireNonNull(emptyToNull(attributeValuePart));

        return filterForAttribute(element, attributeName).stream()
            .filter(a -> a.getValue().contains(attributeValuePart)).toList();
    }
}
