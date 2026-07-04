/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.test.jsf.converter;

import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.jsf.mocks.ReverseConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression test for LIFE-5: an {@link AbstractConverterTest} subclass overriding
 * {@link #configureComponents(ComponentConfigDecorator)} must actually have that
 * override invoked before the converter tests run. Previously the method was a
 * documented no-op because the base class never dispatched it.
 */
class ConverterConfigureComponentsRegressionTest extends AbstractConverterTest<ReverseConverter, String> {

    private boolean configureComponentsCalled;

    @Override
    protected void configureComponents(final ComponentConfigDecorator decorator) {
        super.configureComponents(decorator);
        configureComponentsCalled = true;
    }

    @Override
    public void populate(TestItems<String> testItems) {
        testItems.addRoundtripValues("abc");
    }

    @Test
    void configureComponentsOverrideIsInvoked() {
        assertTrue(configureComponentsCalled,
            "The configureComponents override must be invoked by initConverter (LIFE-5)");
    }
}
