package de.icw.cui.test.jsf.converter;

import javax.faces.convert.IntegerConverter;

class AbstractConverterTestTest extends AbstractConverterTest<IntegerConverter, Integer> {

    @Override
    public void populate(final TestItems<Integer> testItems) {
        testItems.addRoundtripValues("1", "122", "2132121").addInvalidString("a")
                .addInvalidStringWithMessage("a", "javax.faces.converter.IntegerConverter.INTEGER")
                .addValidString("13").addValidStringWithObjectResult("17", 17)
                .addValidObject(2)
                .addValidObjectWithStringResult(14, "14");

    }

}
