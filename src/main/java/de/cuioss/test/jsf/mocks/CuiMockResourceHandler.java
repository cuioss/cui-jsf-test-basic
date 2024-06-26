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
package de.cuioss.test.jsf.mocks;

import de.cuioss.tools.string.MoreStrings;
import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ViewResource;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Mock Implementation ResourceHandler
 *
 * @author Oliver Wolff
 */
public class CuiMockResourceHandler extends ResourceHandler {

    /**
     *
     */
    public static final String DUMMY_URL = "http://dummy";

    /**
     * "libNotThere"
     */
    public static final String LIBRARY_NOT_THERE = "libNotThere";

    /**
     * "resNotThere"
     */
    public static final String RESOURCE_NOT_THERE = "resNotThere";

    /**
     * "rendererererer"
     */
    public static final String RENDERER_SUFFIX = "-rendererererer";

    /**
     * the availableResouces use the form libraryName + LIBRARY_RESOURCE_DELIMITER +
     * resourceName as key
     */
    public static final String LIBRARY_RESOURCE_DELIMITER = "-";

    /**
     * "image/png"
     */
    public static final String DEFAULT_CONTENT_TYPE = "image/png";

    @Setter
    private boolean resourceRequest;

    /**
     * A map of resources to be provided by this resourceHandler.
     */
    @Getter
    @Setter
    private Map<String, CuiMockResource> availableResouces = new HashMap<>();

    /**
     * Creates an resourceIdetnifier utilized by availableResouces
     *
     * @param resourceName may be null
     * @param libraryName  may be null
     * @return the concatenated String, see {@link #LIBRARY_RESOURCE_DELIMITER}
     */
    public static final String createResourceMapKey(final String resourceName, final String libraryName) {
        var resource = resourceName;
        var library = libraryName;
        if (MoreStrings.isEmpty(library)) {
            library = "notThere";
        }
        if (MoreStrings.isEmpty(resource)) {
            resource = "notThere";
        }
        return library + LIBRARY_RESOURCE_DELIMITER + resource;
    }

    @Override
    public Resource createResource(final String resourceName) {
        return createResource(resourceName, null);
    }

    @Override
    public Resource createResource(final String resourceName, final String libraryName) {
        return createResource(resourceName, libraryName, DEFAULT_CONTENT_TYPE);
    }

    @Override
    public Resource createResource(final String resourceName, final String libraryName, final String contentType) {
        return availableResouces.get(createResourceMapKey(resourceName, libraryName));
    }

    @Override
    public ViewResource createViewResource(final FacesContext context, final String resourceName) {
        return new ViewResource() {

            @Override
            public URL getURL() {
                try {
                    return new URL(DUMMY_URL + resourceName);
                } catch (MalformedURLException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    @Override
    public boolean libraryExists(final String libraryName) {
        return LIBRARY_NOT_THERE.equals(libraryName);
    }

    @Override
    public void handleResourceRequest(final FacesContext context) {
        // NOOP
    }

    @Override
    public boolean isResourceRequest(final FacesContext context) {
        return resourceRequest;
    }

    @Override
    public String getRendererTypeForResourceName(final String resourceName) {
        return resourceName + RENDERER_SUFFIX;
    }
}
