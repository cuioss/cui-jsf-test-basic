package de.cuioss.test.jsf.converter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AbstractSanitizingConverterTestTest extends AbstractSanitizingConverterTest<FakeSanitizingConverter, String> {

    @Override
    protected String createTestObjectWithMaliciousContent(String content) {
        return content;
    }

    @Override
    public void populate(TestItems<String> testItems) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void shouldSanitizeJavaScript() {
        // ignore, the tests are separate;
    }

    @Test
    void shouldDetectInvalidEscaping() {
        super.getConverter().setFakeEscaping(false);
        assertThrows(AssertionError.class, () -> super.shouldSanitizeJavaScript());
    }

    @Test
    void shouldDetectValidEscaping() {
        super.getConverter().setFakeEscaping(true);
        assertDoesNotThrow(() -> super.shouldSanitizeJavaScript());
    }

}
