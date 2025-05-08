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

### 2. Integrate Parameter Resolver with JsfSetupExtension

- [ ] Update `JsfSetupExtension` to implement `ParameterResolver`
  - [ ] Implement `supportsParameter` method to identify resolvable parameters
  - [ ] Implement `resolveParameter` method to provide parameter values
  - [ ] Store JSF environment objects in the extension context store
  - [ ] Handle test class lifecycle events correctly

### 3. Maintain Backward Compatibility

- [ ] Keep existing `JsfEnvironmentConsumer` interface working
- [ ] Add deprecation annotations to methods that will be replaced
- [ ] Document the migration path from manual injection to parameter resolution
- [ ] Ensure existing tests continue to function with both approaches

### 4. Create Documentation and Examples

- [ ] Update JavaDocs to explain the parameter resolution approach
- [ ] Create example test classes demonstrating parameter resolution
- [ ] Document best practices for the new approach
- [ ] Create a migration guide for users of the existing approach

### 5. Test Infrastructure

- [ ] Create unit tests for the parameter resolver implementation
- [ ] Test various injection scenarios:
  - [ ] Test with single parameter
  - [ ] Test with multiple parameters
  - [ ] Test with nested test classes
  - [ ] Test with parameterized tests
- [ ] Verify backward compatibility with existing tests

### 6. Extend Parameter Resolution Support

- [ ] Add support for additional JSF objects:
  - [ ] `NavigationHandler`
  - [ ] `UIComponent` instances
  - [ ] `ELContext`
  - [ ] Custom beans from the JSF context
- [ ] Create custom annotations for specific injection scenarios
  - [ ] Consider `@JsfInject` for more specific injection control

### 7. Refactor Related Components

- [ ] Update `JsfEnabledTestEnvironment` to leverage parameter resolution
- [ ] Create a new base test class optimized for parameter resolution
- [ ] Refactor existing utility methods to work with the parameter resolution approach

### 8. Performance Optimization

- [ ] Evaluate the performance impact of parameter resolution vs. manual injection
- [ ] Optimize the parameter resolution mechanism if needed
- [ ] Implement caching for frequently resolved parameters

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

## References

- [JUnit 5 Parameter Resolution](https://junit.org/junit5/docs/current/user-guide/#writing-tests-dependency-injection)
- [JSF API Documentation](https://javaee.github.io/javaee-spec/javadocs/javax/faces/package-summary.html)