package de.icw.cui.test.jsf.mocks;

import de.icw.cui.test.jsf.converter.AbstractConverterTest;
import de.icw.cui.test.jsf.converter.TestItems;

class ReverseConverterTest extends AbstractConverterTest<ReverseConverter, String> {

    @Override
    public void populate(TestItems<String> testItems) {
        testItems.addRoundtripValues("123");
    }
}
