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

import jakarta.el.ELContext;
import jakarta.el.MethodExpression;
import jakarta.el.MethodInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Oliver Wolff
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public class CuiMockMethodExpression extends MethodExpression {

    @Serial
    private static final long serialVersionUID = 2692860324272056192L;

    /**
     * Indicates whether method was invoked.
     */
    @Getter
    @Setter
    private boolean invoked = false;

    /**
     * Parameters method was invoked with.
     */
    @Getter
    private transient Object[] invokedParams;

    /**
     * Result to be returned on {@link #invoke(ELContext, Object[])}
     */
    @Setter
    private Serializable invokeResult;

    @Setter
    private transient MethodInfo methodInfo;

    @Getter
    @Setter
    private String expressionString;

    @Getter
    @Setter
    private boolean literalText = false;

    @Override
    public MethodInfo getMethodInfo(final ELContext context) {
        return methodInfo;
    }

    @Override
    public Object invoke(final ELContext context, final Object[] params) {
        invoked = true;
        invokedParams = Arrays.copyOf(params, params.length);
        return invokeResult;
    }
}
