package de.cuioss.test.jsf.mocks;

import java.nio.charset.StandardCharsets;

import javax.servlet.ServletRegistration.Dynamic;

import org.apache.myfaces.test.mock.MockServletContext;

import lombok.Getter;
import lombok.Setter;

public class CuiMockServletContext extends MockServletContext {

    @Override
    public Dynamic addJspFile(String servletName, String jspFile) {
        throw new UnsupportedOperationException();
    }

    @Getter
    @Setter
    private int sessionTimeout = 200;

    @Getter
    @Setter
    private String requestCharacterEncoding = StandardCharsets.UTF_8.name();

    @Getter
    @Setter
    private String responseCharacterEncoding = StandardCharsets.UTF_8.name();

    @Getter
    @Setter
    private String virtualServerName = "virtual";

    @Getter
    @Setter
    private String contextPath = "mock-context";

}
