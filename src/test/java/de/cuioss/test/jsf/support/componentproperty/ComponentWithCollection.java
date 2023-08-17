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
package de.cuioss.test.jsf.support.componentproperty;

import java.util.List;

import javax.faces.component.html.HtmlInputText;

import de.cuioss.test.jsf.config.component.VerifyComponentProperties;

@SuppressWarnings("javadoc")
@VerifyComponentProperties(of = { "stringList" })
public class ComponentWithCollection extends HtmlInputText {

    public static final String STRING_LIST_KEY = "stringList";

    public void setStringList(final List<String> value) {
        getStateHelper().put(STRING_LIST_KEY, value);
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList() {
        return (List<String>) getStateHelper().eval(STRING_LIST_KEY);
    }
}
