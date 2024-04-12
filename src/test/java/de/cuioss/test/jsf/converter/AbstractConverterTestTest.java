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
package de.cuioss.test.jsf.converter;

import jakarta.faces.convert.IntegerConverter;

class AbstractConverterTestTest extends AbstractConverterTest<IntegerConverter, Integer> {

    @Override
    public void populate(final TestItems<Integer> testItems) {
        testItems.addRoundtripValues("1", "122", "2132121").addInvalidString("a")
            .addInvalidStringWithMessage("a", "jakarta.faces.converter.IntegerConverter.INTEGER").addValidString("13")
            .addValidStringWithObjectResult("17", 17).addValidObject(2).addValidObjectWithStringResult(14, "14");

    }

}
