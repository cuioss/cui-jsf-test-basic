# JSF Testing Framework Parameter Resolution Implementation Plan

This document outlines the plan for refactoring the JSF testing framework to use parameter resolution instead of manual injection.

## Problem Statement

Currently, the JSF testing framework manually injects `JsfEnvironmentHolder` and related objects into test classes using the `JsfEnvironmentConsumer` interface. This approach requires test classes to implement specific interfaces and has limited flexibility.

## Proposed Solution

Implement JUnit 5 parameter resolvers to inject JSF-related objects directly into test methods, making tests more concise and easier to write.

## Implementation Tasks

### 1. Create a Parameter Resolver Implementation

- [ ] Create a new `JsfParameterResolver` class implementing `ParameterResolver`
  - [ ] Support resolution of `JsfEnvironmentHolder`
  - [ ] Support resolution of `FacesContext`
  - [ ] Support resolution of `ExternalContext`
  - [ ] Support resolution of `Application`
  - [ ] Support resolution of `RequestConfigDecorator`
  - [ ] Support resolution of `ApplicationConfigDecorator`
  - [ ] Support resolution of `ComponentConfigDecorator`
  - [ ] Support resolution of `MockHttpServletResponse`

**Postconditions:**
- [ ] Comprehensive Javadoc for `JsfParameterResolver` class and methods
- [ ] Package-info.java updated with new parameter resolver information
- [ ] Unit tests covering all supported parameter types
- [ ] Verify code quality with `./mvnw clean -Prewrite rewrite:run verify -Pjavadoc`
- [ ] Fix all Javadoc warnings and errors
- [ ] Git commit with message "Create parameter resolver implementation"

### 2. Integrate Parameter Resolver with JsfSetupExtension

- [ ] Update `JsfSetupExtension` to implement `ParameterResolver`
  - [ ] Implement `supportsParameter` method to identify resolvable parameters
  - [ ] Implement `resolveParameter` method to provide parameter values
  - [ ] Store JSF environment objects in the extension context store
  - [ ] Handle test class lifecycle events correctly

**Postconditions:**
- [ ] Updated Javadoc for `JsfSetupExtension` class
- [ ] Unit tests for parameter resolution functionality in `JsfSetupExtension`
- [ ] Verify code quality with `./mvnw clean -Prewrite rewrite:run verify -Pjavadoc`
- [ ] Fix all Javadoc warnings and errors
- [ ] Git commit with message "Integrate parameter resolver with JsfSetupExtension"

### 3. Maintain Backward Compatibility

- [ ] Keep existing `JsfEnvironmentConsumer` interface working
- [ ] Add deprecation annotations to methods that will be replaced
- [ ] Document the migration path from manual injection to parameter resolution
- [ ] Ensure existing tests continue to function with both approaches

**Postconditions:**
- [ ] Updated Javadoc for `JsfEnvironmentConsumer` interface with deprecation notes
- [ ] Unit tests verifying backward compatibility
- [ ] Migration guide documentation
- [ ] Verify code quality with `./mvnw clean -Prewrite rewrite:run verify -Pjavadoc`
- [ ] Fix all Javadoc warnings and errors
- [ ] Git commit with message "Add backward compatibility support"

### 4. Create Documentation and Examples

- [ ] Update JavaDocs to explain the parameter resolution approach
- [ ] Create example test classes demonstrating parameter resolution
- [ ] Document best practices for the new approach
- [ ] Create a migration guide for users of the existing approach

**Postconditions:**
- [ ] Comprehensive documentation of the parameter resolution approach
- [ ] Example test classes in test package
- [ ] Updated project README with parameter resolution instructions
- [ ] Migration guide in site documentation
- [ ] Verify code quality with `./mvnw clean -Prewrite rewrite:run verify -Pjavadoc`
- [ ] Fix all Javadoc warnings and errors
- [ ] Git commit with message "Add parameter resolution documentation and examples"

### 5. Test Infrastructure

- [ ] Create unit tests for the parameter resolver implementation
- [ ] Test various injection scenarios:
  - [ ] Test with single parameter
  - [ ] Test with multiple parameters
  - [ ] Test with nested test classes
  - [ ] Test with parameterized tests
- [ ] Verify backward compatibility with existing tests

**Postconditions:**
- [ ] Comprehensive test suite for parameter resolution
- [ ] Test coverage for edge cases and special scenarios
- [ ] All tests pass successfully
- [ ] Verify code quality with `./mvnw clean -Prewrite rewrite:run verify -Pjavadoc`
- [ ] Git commit with message "Add test infrastructure for parameter resolution"

### 6. Extend Parameter Resolution Support

- [ ] Add support for additional JSF objects:
  - [ ] `NavigationHandler`
  - [ ] `UIComponent` instances
  - [ ] `ELContext`
  - [ ] Custom beans from the JSF context
- [ ] Create custom annotations for specific injection scenarios
  - [ ] Consider `@JsfInject` for more specific injection control

**Postconditions:**
- [ ] Javadoc for extended parameter resolution support
- [ ] Unit tests for new supported parameter types
- [ ] Documentation of custom annotations
- [ ] Examples demonstrating extended parameter resolution
- [ ] Verify code quality with `./mvnw clean -Prewrite rewrite:run verify -Pjavadoc`
- [ ] Fix all Javadoc warnings and errors
- [ ] Git commit with message "Extend parameter resolution support"

### 7. Refactor Related Components

- [ ] Update `JsfEnabledTestEnvironment` to leverage parameter resolution
- [ ] Create a new base test class optimized for parameter resolution
- [ ] Refactor existing utility methods to work with the parameter resolution approach

**Postconditions:**
- [ ] Updated Javadoc for refactored components
- [ ] Unit tests for refactored components
- [ ] Migration guide for affected components
- [ ] Verify code quality with `./mvnw clean -Prewrite rewrite:run verify -Pjavadoc`
- [ ] Fix all Javadoc warnings and errors
- [ ] Git commit with message "Refactor related components for parameter resolution"

### 8. Performance Optimization

- [ ] Evaluate the performance impact of parameter resolution vs. manual injection
- [ ] Optimize the parameter resolution mechanism if needed
- [ ] Implement caching for frequently resolved parameters

**Postconditions:**
- [ ] Performance analysis documentation
- [ ] Implementation of optimizations if needed
- [ ] Unit tests for performance critical code
- [ ] Verify code quality with `./mvnw clean -Prewrite rewrite:run verify -Pjavadoc`
- [ ] Fix all Javadoc warnings and errors
- [ ] Git commit with message "Optimize parameter resolution performance"

## Additional Components to Consider for Parameter Resolution

- [ ] `FacesServlet`
- [ ] `ViewHandler`
- [ ] `RenderKit`
- [ ] `UIViewRoot`
- [ ] `ELResolver`
- [ ] `ResourceHandler`
- [ ] `MockHttpServletRequest`

## Timeline and Priority

1. Core implementation of parameter resolver
2. Integration with `JsfSetupExtension`
3. Testing and backward compatibility
4. Documentation and examples
5. Extended parameter resolution support
6. Refactoring related components
7. Performance optimization

## Quality Gates for All Tasks

For each completed task:
1. All unit tests must pass
2. Code must be formatted according to project standards
3. No Javadoc errors or warnings
4. No compiler warnings
5. Successful build with `./mvnw clean -Prewrite rewrite:run verify -Pjavadoc`
6. Git commit with descriptive message

## References

- [JUnit 5 Parameter Resolution](https://junit.org/junit5/docs/current/user-guide/#writing-tests-dependency-injection)
- [JSF API Documentation](https://javaee.github.io/javaee-spec/javadocs/javax/faces/package-summary.html)