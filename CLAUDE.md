# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

cui-jsf-test-basic is a JSF testing framework that extends MyFaces-Test with mock-based infrastructure for testing JSF components, validators, converters, and renderers in Jakarta EE 10 (JSF 4.1+) environments. It provides base test classes, HTML tree generation/assertion tools, and JUnit 5 integration.

## Logging Standards for This Project

**Important:** This is a small library focused on testing utilities. LogRecords are **not needed** for this project.

If the rewrite process introduces TODO markers regarding LogRecords (e.g., `// TODO: Use LogRecord API for structured logging`), **always suppress them immediately** using the proposed suppression annotation at **class-level**:

```java
@SuppressWarnings("squid:S2629")  // or whatever suppression is proposed
public class MyClass {
    // class implementation
}
```

Do not create LogRecord constants or migrate to structured logging for this library.

## Build Commands

```bash
# Build and install (default - includes rewrite profile)
./mvnw -Prewrite clean install

# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ClassName

# Run specific test method
./mvnw test -Dtest=ClassName#methodName

# Generate site documentation
./mvnw site

# Clean up Javadoc warnings/errors
./mvnw clean install -Pjavadoc
# Review console output and fix all reported issues

# Run code cleanup with rewrite only (without full build)
./mvnw -Prewrite rewrite:run
./mvnw -Prewrite clean install  # Verify changes
```


## Git Workflow

All cuioss repositories have branch protection on `main`. Direct pushes to `main` are never allowed. Always use this workflow:

1. Create a feature branch: `git checkout -b <branch-name>`
2. Commit changes: `git add <files> && git commit -m "<message>"`
3. Push the branch: `git push -u origin <branch-name>`
4. Create a PR: `gh pr create --repo cuioss/cui-jsf-test-basic --head <branch-name> --base main --title "<title>" --body "<body>"`
5. Wait for CI + Gemini review (check every ~60s until checks complete): `while ! gh pr checks --repo cuioss/cui-jsf-test-basic <pr-number> --watch; do sleep 60; done`
6. **Handle Gemini review comments** — fetch with `gh api repos/cuioss/cui-jsf-test-basic/pulls/<pr-number>/comments` and for each:
   - If clearly valid and fixable: fix it, commit, push, then reply explaining the fix and resolve the comment
   - If disagree or out of scope: reply explaining why, then resolve the comment
   - If uncertain (not 100% confident): **ask the user** before acting
   - Every comment MUST get a reply (reason for fix or reason for not fixing) and MUST be resolved
7. Do **NOT** enable auto-merge unless explicitly instructed. Wait for user approval.
8. Return to main: `git checkout main && git pull`

## Project Architecture

### Core Testing Framework (`src/main/java/de/cuioss/test/jsf`)

- **junit5/**: JUnit 5 integration
  - `@EnableJsfEnvironment`: Main annotation to enable JSF test context
  - `JsfSetupExtension`: Extension that manages JSF lifecycle
  - `JsfParameterResolver`: Resolves JSF objects as test method parameters

- **component/**: Component testing infrastructure
  - `AbstractComponentTest<T>`: Base for testing JSF component classes
  - `AbstractUiComponentTest<T>`: Base for UI component testing
  - Use `@VerifyComponentProperties` to declaratively test component attributes

- **validator/**: Validator testing
  - `AbstractValidatorTest<V, T>`: Base for validator tests
  - Use `TestItems<T>` fluent API to define valid/invalid test data

- **converter/**: Converter testing
  - `AbstractConverterTest<C, T>`: Base for converter tests
  - `TestItems<T>` fluent API for roundtrip, valid, and invalid conversion tests

- **renderer/**: Renderer testing utilities
  - `AbstractComponentRendererTest<R>`: Base for renderer tests
  - `HtmlTreeBuilder`: Programmatic HTML tree construction
  - `HtmlTreeAsserts`: Assertions for rendered HTML output

- **config/**: Configuration infrastructure
  - **decorator/**: Configuration decorators for programmatic setup
    - `ApplicationConfigDecorator`: Configure JSF application (navigation, resource bundles)
    - `ComponentConfigDecorator`: Register components, renderers, converters
    - `RequestConfigDecorator`: Configure mock request/response
  - `@JsfTestConfiguration`: Annotation to specify configuration classes

- **mocks/**: Extended mocks beyond MyFaces-Test
  - Mock implementations for ViewHandler, ResourceHandler, NavigationHandler, etc.

- **util/**: Utility classes
  - `JsfEnvironmentHolder`: Container for JSF test environment
  - `NavigationAsserts`: Assertions for navigation outcomes and redirects

### Package Structure
```
de.cuioss.test.jsf
├── component/          # Component testing base classes
├── config/             # Configuration system
│   ├── component/      # Component-specific config
│   ├── decorator/      # Programmatic configuration decorators
│   └── renderer/       # Renderer-specific config
├── converter/          # Converter testing
├── defaults/           # Default configurations
├── generator/          # Test data generators
├── junit5/             # JUnit 5 integration
├── mocks/              # Mock implementations
├── producer/           # Test object producers
├── renderer/           # Renderer testing
│   └── util/           # HTML rendering utilities
├── util/               # General utilities
└── validator/          # Validator testing
```

## Testing Approach

### Parameter Resolution (Recommended)

Use JUnit 5 parameter resolution to inject JSF objects directly into test methods:

```java
@EnableJsfEnvironment
class MyTest {

    @Test
    void testWithJsfObjects(FacesContext facesContext,
                           ComponentConfigDecorator componentConfig) {
        // Configure components
        componentConfig.registerRenderer(MyRenderer.class);

        // Test code using facesContext
    }
}
```

**Available parameter types:**
- `JsfEnvironmentHolder` - Main holder for all JSF objects
- `FacesContext` - Central JSF context
- `ExternalContext` - External environment access
- `Application` - JSF application
- `RequestConfigDecorator` - Request configuration
- `ApplicationConfigDecorator` - Application configuration
- `ComponentConfigDecorator` - Component configuration
- `MockHttpServletRequest/Response` - Mock HTTP objects
- `NavigationAsserts` - Navigation testing utilities

### Testing Validators

```java
class MyValidatorTest extends AbstractValidatorTest<MyValidator, String> {

    @Override
    public void populate(TestItems<String> testItems) {
        testItems.addValid("validValue")
                 .addInvalidWithMessage("tooLong", "my.validator.MAX_LENGTH");
    }

    @Override
    public void configure(MyValidator validator) {
        validator.setMaxLength(10);
    }
}
```

### Testing Converters

```java
class MyConverterTest extends AbstractConverterTest<MyConverter, MyType> {

    @Override
    public void populate(TestItems<MyType> testItems) {
        testItems.addRoundtripValues("value1", "value2")
                 .addValidStringWithObjectResult("input", expectedObject)
                 .addInvalidString("badInput")
                 .addInvalidStringWithMessage("bad", "my.converter.ERROR_KEY");
    }
}
```

### Testing Components

```java
@VerifyComponentProperties(of = {"value", "rendered", "disabled"})
@JsfTestConfiguration(MyTestConfiguration.class)
class MyComponentTest extends AbstractComponentTest<MyComponent> {

    @Test
    void shouldHandleCustomLogic() {
        MyComponent component = anyComponent();
        // Test component-specific behavior
    }
}
```

### Testing Renderers

```java
@JsfTestConfiguration(MyTestConfiguration.class)
class MyRendererTest extends AbstractComponentRendererTest<MyRenderer> {

    @Test
    void shouldRenderMinimal(ComponentConfigDecorator componentConfig) {
        componentConfig.registerUIComponent(MyComponent.class);

        UIComponent component = getComponent();
        HtmlTreeBuilder expected = new HtmlTreeBuilder()
            .withNode(Node.DIV)
            .withAttributeNameAndId("componentId")
            .withAttribute(AttributeName.CLASS, "my-class");

        assertRenderResult(component, expected.getDocument());
    }

    @Override
    protected UIComponent getComponent() {
        // Return configured component instance
    }
}
```

### Configuration Patterns

**Type-based configuration:**
```java
@EnableJsfEnvironment
@JsfTestConfiguration(MyApplicationConfiguration.class)
class MyTest { }
```

**Method-level configuration (recommended):**
```java
@EnableJsfEnvironment
class MyTest {

    @Test
    void test(ComponentConfigDecorator config) {
        config.registerRenderer(MyRenderer.class);
        // Test code
    }
}
```

**Navigation testing:**
```java
@Test
void testNavigation(FacesContext facesContext,
                   ApplicationConfigDecorator appConfig,
                   NavigationAsserts navigationAsserts) {
    appConfig.registerNavigationCase("outcome", "targetView.xhtml");

    facesContext.getApplication().getNavigationHandler()
        .handleNavigation(facesContext, null, "outcome");

    navigationAsserts.assertNavigatedWithOutcome("outcome");
}
```

## Resource Bundle Handling

By default, `@EnableJsfEnvironment` uses `IdentityResourceBundle`, which returns message keys as-is instead of resolving them. This is useful for testing that correct message keys are used without testing the ResourceBundle mechanism itself.

To disable: `@EnableJsfEnvironment(useIdentityResourceBundle = false)`

## Important Dependencies

- **Jakarta EE 10** (JSF 4.1+)
- **MyFaces Test** - Must use `myfaces-api` (not Mojarra) due to classloader issues in parallel execution
- **JUnit 5** - All tests use JUnit Jupiter
- **Lombok** - Used throughout for builders, value objects, etc.
- **cui-test-value-objects** - Integration for value object testing
- **cui-test-generator** - Test data generation
- **EasyMock** - Mocking framework (compiled scope)

## Migration Notes

**Deprecated approaches:**
- `JsfEnvironmentConsumer` interface - Use parameter resolution instead
- `JsfEnabledTestEnvironment` base class - Use parameter resolution instead
- Configurator interfaces (`ApplicationConfigurator`, `ComponentConfigurator`, `RequestConfigurator`) - Use decorator parameters instead

See `doc/migration.adoc` for detailed migration guide.

## Documentation References

- Usage guide: `doc/usage.adoc`
- Migration guide: `doc/migration.adoc`
- Generated docs: https://cuioss.github.io/cui-jsf-test-basic/about.html
- MyFaces Test: http://myfaces.apache.org/test/index.html

## Version Information

- Current version: 4.0-SNAPSHOT
- JSF 4+ support: main branch (4.x releases)
- JSF 2.3 support: Release 2.0.2 on v2 branch
- Parent POM: `de.cuioss:cui-java-parent:0.9.9.6`
