# Design Document

## Overview

This design outlines the modernization approach for the PDF-AS project, a multi-module Java library for PDF signing. The project currently uses Java 8, Gradle 6.8.3, and various outdated dependencies. The modernization will upgrade to Java 17, Gradle 8.x, and current stable versions of all dependencies while maintaining backward compatibility and minimizing changes to the pdf-as-legacy module.

## Architecture

### Current Architecture Analysis

The PDF-AS project consists of 14 modules organized as follows:

**Core Modules:**
- `pdf-as-common`: Shared utilities and common functionality
- `pdf-as-lib`: Core PDF-AS library with main signing logic
- `pdf-as-pdfbox-2`: PDFBox 2.x backend implementation

**Signature Standards:**
- `signature-standards:sigs-pades`: PAdES signature implementation
- `signature-standards:sigs-pkcs7detached`: PKCS#7 detached signature implementation

**Integration Modules:**
- `pdf-as-moa`: MOA (Modular Object-oriented Architecture) integration
- `pdf-as-legacy`: Legacy compatibility layer (minimal changes)

**Application Modules:**
- `pdf-as-cli`: Command-line interface
- `pdf-as-web`: Web application with Tomcat integration
- `pdf-as-web-db`: Database integration for web module
- `pdf-as-web-client`: Web service client
- `pdf-as-web-status`: Web status monitoring
- `pdf-as-web-statistic-api`: Statistics API

**Testing:**
- `pdf-as-tests`: Integration and system tests

### Target Architecture

The modernized architecture will maintain the same modular structure but with updated technology stack:

- **Java Version**: Upgrade from Java 8 to Java 17 LTS
- **Gradle Version**: Upgrade from 6.8.3 to 8.14.3 (current stable)
- **Build System**: Modernize Gradle build scripts and plugin usage
- **Dependencies**: Update all dependencies to latest stable versions
- **Application Server**: Update Tomcat from 9.0.93 to 10.1.x for Jakarta EE support

## Components and Interfaces

### Java Version Migration Strategy

**Target Java Version**: Java 17 LTS
- Provides long-term support until September 2029
- Includes performance improvements and security enhancements
- Maintains reasonable compatibility with existing code

**Migration Approach**:
1. Update `sourceCompatibility` and `targetCompatibility` to 17
2. Update Gradle wrapper to support Java 17
3. Review and update any Java 8 specific code patterns
4. Test all modules for Java 17 compatibility

### Gradle Build System Modernization

**Current State**: Gradle 6.8.3 with older plugin syntax
**Target State**: Gradle 8.14.3 with modern plugin management

**Key Changes**:
1. **Plugin Management**: Migrate from legacy `apply plugin` to plugins DSL
2. **Dependency Management**: Use version catalogs for centralized dependency management
3. **Build Script Modernization**: Update deprecated APIs and configurations
4. **Task Configuration**: Modernize task definitions and configurations

### Dependency Update Strategy

**Core Dependencies**:

| Component | Current Version | Target Version | Notes |
|-----------|----------------|----------------|-------|
| Apache PDFBox | 2.0.32 | Latest 3.0.x | Major version upgrade, requires compatibility testing |
| SLF4J | 1.7.36 | Latest 2.0.x | Minor API changes, mostly backward compatible |
| Commons IO | 2.16.1 | Latest 2.x | Patch updates |
| Commons Collections | 3.2.2 | Latest 4.x | Major version upgrade |
| Apache CXF | 3.5.9 | Latest 4.x | Major version with Jakarta EE migration |
| Tomcat | 9.0.93 | Latest 10.1.x | Jakarta EE namespace migration required |

**Build Tool Dependencies**:
- Gradle Versions Plugin: 0.28.0 → Latest stable
- OWASP Dependency Check: 6.5.0.1 → Latest stable
- Lombok: 1.18.28 → Latest stable

### Legacy Module Handling

**pdf-as-legacy Module Strategy**:
- Minimize changes to maintain stability
- Update only essential dependencies for security
- Ensure compilation compatibility with Java 17
- Maintain existing API contracts
- Focus on build system compatibility rather than code modernization

## Data Models

### Configuration Management

**Current Configuration Approach**:
- Properties-based configuration files
- Profile-based signature block definitions
- Font and image resource management

**Modernization Approach**:
- Maintain existing configuration file formats for backward compatibility
- Update configuration parsing to handle new dependency versions
- Ensure resource loading works with updated classpath handling

### Build Output Structure

**Current Build Outputs**:
- JAR files for each module
- WAR file for web application
- Distribution archives (ZIP/TAR)
- Source JARs
- Maven repository structure

**Modernized Build Outputs**:
- Maintain same output structure
- Update manifest attributes for new versions
- Ensure compatibility with existing deployment processes
- Update distribution packaging for new Tomcat version

## Error Handling

### Dependency Compatibility Issues

**Strategy for Handling Breaking Changes**:

1. **PDFBox 3.x Migration**:
   - Create compatibility layer if needed
   - Update PDF processing code for API changes
   - Maintain existing public API contracts

2. **Jakarta EE Migration**:
   - Update servlet imports from `javax.servlet` to `jakarta.servlet`
   - Update web.xml configurations
   - Ensure Tomcat 10.x compatibility

3. **Commons Collections 4.x**:
   - Update deprecated method calls
   - Handle API changes in collection utilities

**Fallback Strategies**:
- Maintain intermediate dependency versions if major upgrades cause issues
- Implement adapter patterns for significant API changes
- Document any breaking changes with migration guides

### Build System Error Handling

**Gradle Migration Issues**:
- Update deprecated Gradle APIs
- Handle plugin compatibility issues
- Resolve dependency resolution conflicts

**Java 17 Compatibility**:
- Address any reflection or module system issues
- Update any Java 8 specific code patterns
- Handle classpath and module path configurations

## Testing Strategy

### Compatibility Testing Approach

**Phase 1: Build System Testing**
1. Verify all modules compile with Java 17
2. Ensure Gradle 8.x builds complete successfully
3. Validate all existing Gradle tasks work correctly
4. Test distribution generation and packaging

**Phase 2: Dependency Compatibility Testing**
1. Run existing unit tests with updated dependencies
2. Perform integration testing for PDF operations
3. Test web application deployment on updated Tomcat
4. Validate signature generation and verification

**Phase 3: End-to-End Testing**
1. Test CLI functionality with sample PDFs
2. Verify web service operations
3. Test different signature profiles and configurations
4. Validate backward compatibility with existing configurations

### Test Environment Setup

**Development Environment**:
- Java 17 JDK installation
- Gradle 8.x wrapper
- Updated IDE configurations
- Local Tomcat 10.x for web testing

**CI/CD Considerations**:
- Update build pipelines for Java 17
- Configure dependency vulnerability scanning
- Maintain compatibility testing with multiple Java versions if needed

### Regression Testing

**Critical Functionality Tests**:
1. PDF signature creation and verification
2. Multiple signature profile support
3. Configuration file processing
4. Web service API compatibility
5. CLI command functionality

**Performance Testing**:
- Compare PDF processing performance before/after upgrade
- Memory usage analysis with new dependency versions
- Startup time comparison for web applications

### Legacy Module Testing

**Minimal Testing Approach for pdf-as-legacy**:
- Verify compilation with Java 17
- Run basic functionality tests
- Ensure integration points still work
- Avoid extensive refactoring or testing

## Implementation Phases

### Phase 1: Build System Foundation
- Update Gradle wrapper to 8.14.3
- Modernize root build.gradle configuration
- Update plugin management and versions
- Ensure basic compilation works

### Phase 2: Java Version Migration
- Update source/target compatibility to Java 17
- Address any immediate compilation issues
- Update IDE configurations and documentation

### Phase 3: Core Dependency Updates
- Update SLF4J and logging dependencies
- Update Commons libraries
- Update build tool plugins
- Test core functionality

### Phase 4: Major Dependency Updates
- Update Apache PDFBox to 3.x
- Handle API compatibility issues
- Update PDF processing code as needed

### Phase 5: Web Stack Modernization
- Update Tomcat to 10.x
- Migrate to Jakarta EE namespaces
- Update servlet and web dependencies
- Test web application deployment

### Phase 6: Integration and Testing
- Run comprehensive test suites
- Perform end-to-end testing
- Validate backward compatibility
- Update documentation

### Phase 7: Legacy Module Integration
- Ensure pdf-as-legacy builds correctly
- Minimal updates for compatibility
- Integration testing with other modules