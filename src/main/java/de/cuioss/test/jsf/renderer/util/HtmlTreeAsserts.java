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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import lombok.experimental.UtilityClass;

/**
 * Provides asserts for {@link Document} and Jdom elements.
 *
 * @author Oliver Wolff
 */
@UtilityClass
public final class HtmlTreeAsserts {

    private static final String THE_VALUES_ARE_NOT_EQUAL_EXPECTED = "%s: The values for attribute '%s' are not equal, expected=%s, actual=%s";

    private static final String ACTUAL_MUST_NOT_BE_NULL = "Actual must not be null";

    private static final String EXPECTED_MUST_NOT_BE_NULL = "Expected must not be null";

    private static final AttributeComparator ATTRIBUTE_COMPARATOR = new AttributeComparator();

    /**
     * Compares two instances of {@link Document} for equality. Attribute order is
     * not relevant, but element order is.
     *
     * @param expected expected value
     * @param actual   the value to check against <code>expected</code>
     */
    public static void assertHtmlTreeEquals(final Document expected, final Document actual) {
        assertNotNull(expected, EXPECTED_MUST_NOT_BE_NULL);
        assertNotNull(actual, ACTUAL_MUST_NOT_BE_NULL);
        var expectedNode = expected.getRootElement();
        var actualNode = actual.getRootElement();
        assertElementWithChildrenEquals(expectedNode, actualNode, "");
    }

    /**
     * Compares two instances of {@link Element} for equality. Attribute order is
     * not relevant.It checks the children recursively as well.
     *
     * @param expected expected value
     * @param actual   the value to check against <code>expected</code>
     * @param pointer  String based path identifier
     */
    public static void assertElementWithChildrenEquals(final Element expected, final Element actual,
            final String pointer) {
        var currentPointer = pointer + ">" + expected.getName();
        assertElementEquals(expected, actual, currentPointer);
        var expectedChildren = expected.getChildren();
        var actualChildren = actual.getChildren();
        if (null != expected.getAttribute("id")) {
            currentPointer = currentPointer + "[" + expected.getAttribute("id").getValue() + "]";
        }
        var expectedTextChild = expected.getTextNormalize();
        var actualTextChild = actual.getTextNormalize();
        assertEquals(expectedTextChild, actualTextChild,
                "%s: The text content of the elements are not equal, expected=%s, actual=%s".formatted(currentPointer,
                        expectedTextChild, actualTextChild));
        if (expectedChildren.isEmpty() && actualChildren.isEmpty()) {
            return;
        }
        if (expectedChildren.size() != actualChildren.size()) {
            fail("%s: The number of children is not equal, expected=%s, actual=%s".formatted(currentPointer,
                    expectedChildren, actualChildren));
        }
        for (var i = 0; i < expectedChildren.size(); i++) {
            assertElementWithChildrenEquals(expectedChildren.get(i), actualChildren.get(i), currentPointer);
        }
    }

    /**
     * Compares two instances of {@link Element} for equality. Attribute order is
     * not relevant. It checks the name of the elements and the attributes,
     * <em>not</em> the children
     *
     * @param expected expected value
     * @param actual   actual the value to check against <code>expected</code>
     * @param pointer  String based path identifier
     */
    public static void assertElementEquals(final Element expected, final Element actual, final String pointer) {
        assertNotNull(expected, EXPECTED_MUST_NOT_BE_NULL);
        assertNotNull(actual, ACTUAL_MUST_NOT_BE_NULL);
        assertEquals(expected.getName(), actual.getName(),
                "%s: The names are not equal, expected=%s, actual=%s".formatted(pointer, expected, actual));
        var expectedAttributes = expected.getAttributes();
        var actualAttributes = actual.getAttributes();
        if (expectedAttributes.isEmpty() && actualAttributes.isEmpty()) {
            return;
        }
        expectedAttributes.sort(ATTRIBUTE_COMPARATOR);
        actualAttributes.sort(ATTRIBUTE_COMPARATOR);
        if (expectedAttributes.size() != actualAttributes.size()) {
            fail("%s: The number of the attributes are not equal, expected=%s, actual=%s".formatted(pointer,
                    expectedAttributes, actualAttributes));
        }
        for (var i = 0; i < expectedAttributes.size(); i++) {
            assertAttributeEquals(expectedAttributes.get(i), actualAttributes.get(i), pointer);
        }
    }

    /**
     * Compares two instances of {@link Attribute} for equality.
     *
     * @param expected expected value
     * @param actual   the value to check against <code>expected</code>
     * @param pointer  String based path identifier
     */
    public static void assertAttributeEquals(final Attribute expected, final Attribute actual, final String pointer) {
        assertNotNull(expected, EXPECTED_MUST_NOT_BE_NULL);
        assertNotNull(actual, ACTUAL_MUST_NOT_BE_NULL);
        assertEquals(expected.getName(), actual.getName(),
                "%s: The name of the attributes are not equal, expected=%s, actual=%s".formatted(pointer, expected,
                        actual));
        assertEquals(expected.getValue(), actual.getValue(), THE_VALUES_ARE_NOT_EQUAL_EXPECTED.formatted(pointer,
                expected.getName(), expected.getValue(), actual.getValue()));
    }
}
