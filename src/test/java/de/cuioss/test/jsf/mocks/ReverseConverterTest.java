package de.cuioss.test.jsf.mocks;

import de.cuioss.test.jsf.converter.AbstractConverterTest;
import de.cuioss.test.jsf.converter.TestItems;

class ReverseConverterTest extends AbstractConverterTest<ReverseConverter, String> {

    @Override
    public void populate(TestItems<String> testItems) {
        testItems.addRoundtripValues("123");
    }
}
