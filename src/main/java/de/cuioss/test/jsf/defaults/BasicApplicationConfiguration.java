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
package de.cuioss.test.jsf.defaults;

import de.cuioss.test.jsf.config.ApplicationConfigurator;
import de.cuioss.test.jsf.config.RequestConfigurator;
import de.cuioss.test.jsf.config.decorator.ApplicationConfigDecorator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;

import java.util.Collection;
import java.util.Locale;

import static de.cuioss.tools.collect.CollectionLiterals.immutableSet;

/**
 * Provides some default configuration for the tests. The defaults are:
 * <ul>
 * <li>Setting the supported locales to {@link Locale#ENGLISH} and
 * {@link Locale#GERMANY}</li>
 * <li>Setting the default locale to {@link Locale#ENGLISH}</li>
 * <li>Setting the user-agent to {@value #FIREFOX}</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
public class BasicApplicationConfiguration implements ApplicationConfigurator, RequestConfigurator {

    /**
     * Firefox user-agent
     */
    public static final String FIREFOX = "Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0";

    /**
     * User-agent identifier.
     */
    public static final String USER_AGENT = "user-agent";

    /**
     * The supported {@link Locale}s
     */
    @SuppressWarnings("java:S2386") // owolff: false positive -> immutableSet
    public static final Collection<Locale> SUPPORTED_LOCALES = immutableSet(Locale.ENGLISH, Locale.GERMANY,
        Locale.FRENCH);

    /**
     * The default {@link Locale}
     */
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    @Override
    public void configureApplication(final ApplicationConfigDecorator config) {
        config.registerSupportedLocales(SUPPORTED_LOCALES).registerDefaultLocale(DEFAULT_LOCALE).setContextPath(""); // default
        // context
        // path
        // for myfaces test is
        // 'null'
    }

    @Override
    public void configureRequest(final RequestConfigDecorator decorator) {
        decorator.setRequestHeader(USER_AGENT, FIREFOX)
            .setRequestLocale(SUPPORTED_LOCALES.toArray(new Locale[SUPPORTED_LOCALES.size()]));
    }
}
