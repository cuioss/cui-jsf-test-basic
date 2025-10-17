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
package de.cuioss.test.jsf.config;

/**
 * Simple Marker Interface for grouping the different JSf test configurators
 * like {@link ApplicationConfigurator},{@link ComponentConfigurator} and {@link RequestConfigurator}
 *
 * @author Oliver Wolff
 * @deprecated As of 1.0.0, all sub-interfaces are deprecated in favor of the parameter resolution approach.
 * See the migration guide for more details on how to migrate to the parameter resolution approach.
 */
@Deprecated
public interface JsfTestContextConfigurator {

}
