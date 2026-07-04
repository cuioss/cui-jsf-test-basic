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
package de.cuioss.test.jsf.mocks;

import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import de.cuioss.test.valueobjects.api.object.ObjectTestContracts;
import de.cuioss.test.valueobjects.api.object.VetoObjectTestContract;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@VerifyBeanProperty(exclude = {"invokedParams", "parmetersProvided"})
@VetoObjectTestContract(ObjectTestContracts.SERIALIZABLE)
class CuiMockMethodExpressionTest extends ValueObjectTest<CuiMockMethodExpression> {

    @Test
    void shouldTrackInvocationWithParams() {
        var expression = new CuiMockMethodExpression();
        expression.setInvokeResult("result");

        assertEquals("result", expression.invoke(null, new Object[]{"a", "b"}));
        assertTrue(expression.isInvoked(), "invoke must mark the expression as invoked");
        assertArrayEquals(new Object[]{"a", "b"}, expression.getInvokedParams(),
            "invoke must record the passed parameters");
    }

    @Test
    void shouldTolerateNullParams() {
        var expression = new CuiMockMethodExpression();

        assertNull(expression.invoke(null, null), "invoke returns the (unset) result");
        assertTrue(expression.isInvoked(), "invoke must mark the expression as invoked");
        assertEquals(0, expression.getInvokedParams().length,
            "null params must be normalized to an empty array (MOCK-10)");
    }
}
