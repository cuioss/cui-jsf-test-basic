package de.cuioss.test.jsf.mocks;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;

import de.cuioss.tools.string.MoreStrings;
import lombok.Getter;
import lombok.Setter;

/**
 * Mock Implementation ResourceHandler
 *
 * @author Oliver Wolff
 */
public class CuiMockResourceHandler extends ResourceHandler {

    /**  */
    public static final String DUMMY_URL = "http://dummy";

    /** "libNotThere" */
    public static final String LIBRARY_NOT_THERE = "libNotThere";

    /** "resNotThere" */
    public static final String RESOURCE_NOT_THERE = "resNotThere";

    /** "rendererererer" */
    public static final String RENDERER_SUFFIX = "-rendererererer";

    /**
     * the availableResouces use the form libraryName +
     * LIBRARY_RESOURCE_DELIMITER + resourceName as key
     */
    public static final String LIBRARY_RESOURCE_DELIMITER = "-";

    /** "image/png" */
    public static final String DEFAULT_CONTENT_TYPE = "image/png";

    @Setter
    private boolean resourceRequest;

    /**
     * A map of resources to be provided by this resourceHandler.
     */
    @Getter
    @Setter
    private Map<String, CuiMockResource> availableResouces = new HashMap<>();

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

    /**
     * Creates an resourceIdetnifier utilized by availableResouces
     *
     * @param resourceName
     *            may be null
     * @param libraryName
     *            may be null
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
