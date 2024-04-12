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
package de.cuioss.test.jsf.junit5;

import de.cuioss.test.jsf.support.beans.MediumComplexityBean;
import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@PropertyReflectionConfig(defaultValued = { MediumComplexityBean.STRING_WITH_DEFAULT_VALUE })
@ObjectTestConfig(equalsAndHashCodeExclude = MediumComplexityBean.ATTRIBUTE_NO_OBJECT_IDENTITY_STRING)
class AbstractBeanTestTest extends AbstractBeanTest<MediumComplexityBean> {

    @Test
    void shouldInstantiate() {
        assertNotNull(anyBean());
    }
}
