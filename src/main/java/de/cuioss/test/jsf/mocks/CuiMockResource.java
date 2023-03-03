package de.cuioss.test.jsf.mocks;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Mock with all necessary values provided with setters
 *
 * @author Oliver Wolff
 */
@ToString(callSuper = false)
public class CuiMockResource extends Resource {

    @Getter
    @Setter
    private InputStream inputStream;

    @Getter
    @Setter
    private Map<String, String> responseHeaders;

    @Getter
    @Setter
    private String requestPath;

    @Setter
    @Getter
    private URL uRL;

    @Setter
    private boolean userAgentNeedsUpdate;

    @Override
    public boolean userAgentNeedsUpdate(final FacesContext context) {
        return userAgentNeedsUpdate;
    }

}
