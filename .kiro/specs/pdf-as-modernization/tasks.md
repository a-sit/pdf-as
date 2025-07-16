# Implementation Plan

**Note**: After completing each task, commit all changes to the `feature/gradle-modernization` git branch with a descriptive commit message.

- [x] 1. Update Gradle build system foundation
  - Research Gradle 6.8.3 to 8.14.3 migration guide and breaking changes
  - Update Gradle wrapper to version 8.14.3
  - Modernize root build.gradle with current syntax and plugin management
  - Update gradle.properties if needed for new Gradle version
  - Test basic project compilation with new Gradle version
  - Run `./gradlew build` to verify all modules compile successfully
  - **Commit changes**: `git add . && git commit -m "feat: modernize Gradle build system to version 8.14.3"`
  - _Requirements: 4.1, 4.2, 4.3_

- [x] 2. Upgrade Java version compatibility
  - Research Java 8 to Java 17 migration guide and breaking changes
  - Update sourceCompatibility and targetCompatibility to Java 17 in all build.gradle files
  - Update any Java 8 specific code patterns that are incompatible with Java 17
  - Update IDE configuration files for Java 17
  - Test compilation of all modules with Java 17
  - Run `./gradlew build test` to verify compilation and basic tests pass
  - **Commit changes**: `git add . && git commit -m "feat: upgrade Java version compatibility from Java 8 to Java 17"`
  - _Requirements: 1.1, 1.2, 1.4_

- [x] 3. Update build tool plugins and configurations
  - Research migration guides for Gradle Versions Plugin and OWASP Dependency Check updates
  - Update Gradle Versions Plugin to latest stable version
  - Update OWASP Dependency Check plugin to latest stable version
  - Modernize plugin application syntax from legacy apply plugin to plugins DSL
  - Update deprecated Gradle API usage in build scripts
  - Test all Gradle tasks work correctly with updated plugins
  - Run `./gradlew build test` to verify plugins work correctly
  - **Commit changes**: `git add . && git commit -m "feat: update build tool plugins and configurations"`
  - _Requirements: 4.2, 4.3, 4.4_

- [x] 4. Update core common dependencies
  - Research SLF4J 1.7.x to 2.0.x migration guide and API changes
  - Research Commons Collections 3.x to 4.x migration guide and breaking changes
  - Update SLF4J to latest 2.0.x version in pdf-as-common module
  - Update Commons IO to latest 2.x version
  - Update Commons Collections from 3.2.2 to latest 4.x version
  - Update Lombok to latest stable version
  - Handle any API compatibility issues from Commons Collections upgrade
  - Run `./gradlew build test` to verify all modules compile and tests pass
  - **Commit changes**: `git add . && git commit -m "feat: update core common dependencies (SLF4J, Commons Collections, Lombok)"`
  - _Requirements: 2.1, 2.6_

- [x] 5. Update PDF processing dependencies
  - Research Apache PDFBox 2.x to 3.x migration guide and API breaking changes
  - Update Apache PDFBox from 2.0.32 to latest stable 2.0.34 version in pdf-as-pdfbox-2 module
  - Update PDFBox Tools and Preflight dependencies to match PDFBox version
  - Identified PDFBox 3.x requires extensive code changes due to breaking API changes
  - Test PDF signing and verification functionality with updated PDFBox version
  - Run `./gradlew build test` to verify PDF processing modules work correctly
  - **Commit changes**: `git add . && git commit -m "feat: update PDF processing dependencies (Apache PDFBox to 2.0.34)"`
  - _Requirements: 2.1, 2.3, 6.4_
  - **Note**: PDFBox 3.x upgrade deferred to separate task due to complexity

- [x] 6. Update web application dependencies
  - Research Apache CXF 3.x to 4.x migration guide and Jakarta EE changes
  - Research Java EE to Jakarta EE migration guide and namespace changes
  - Update Apache CXF to latest 4.x version for Jakarta EE compatibility
  - Update Tomcat version configuration to latest 10.1.x
  - Update servlet API dependencies from javax.servlet to jakarta.servlet
  - Update web.xml configurations for Jakarta EE namespaces
  - Run `./gradlew build` to verify web modules compile with updated dependencies
  - **Commit changes**: `git add . && git commit -m "feat: update web application dependencies (CXF 4.x, Jakarta EE)"`
  - _Requirements: 2.1, 3.1, 3.2, 3.3_

- [x] 7. Migrate web modules to Jakarta EE
  - Update servlet imports from javax.servlet to jakarta.servlet in pdf-as-web module
  - Update JSP and web configuration files for Jakarta EE
  - Update any other Java EE to Jakarta EE namespace migrations needed
  - Test web application deployment on updated Tomcat version
  - Run `./gradlew build war` to verify web application builds successfully
  - **Commit changes**: `git add . && git commit -m "feat: migrate web modules to Jakarta EE namespace"`
  - _Requirements: 3.1, 3.2, 3.3, 3.4_

- [x] 8. Handle pdf-as-legacy module with minimal changes
  - Ensure module compiles and builds successfully
  - Avoid extensive code changes to maintain stability
  - Test integration points with other modules still work
  - Run `./gradlew :pdf-as-legacy:build` to verify legacy module builds correctly
  - **Commit changes**: `git add . && git commit -m "feat: update pdf-as-legacy module with minimal changes"`
  - _Requirements: 5.4, 1.2_

- [x] 9. Update signature standard modules
  - Update dependencies in signature-standards:sigs-pades module
  - Update dependencies in signature-standards:sigs-pkcs7detached module
  - Test signature generation and verification with updated dependencies
  - Ensure compatibility with updated pdf-as-lib module
  - Run `./gradlew :signature-standards:sigs-pades:build :signature-standards:sigs-pkcs7detached:build test` to verify modules work correctly
  - **Commit changes**: `git add . && git commit -m "feat: update signature standard modules dependencies"`
  - _Requirements: 2.1, 6.4_

- [-] 10. Update MOA integration module
  - Update dependencies in pdf-as-moa module
  - Update WSDL and XML schema processing for new dependency versions
  - Test MOA service integration functionality
  - Handle any XML processing API changes
  - Run `./gradlew :pdf-as-moa:build test` to verify MOA module works correctly
  - **Commit changes**: `git add . && git commit -m "feat: update MOA integration module dependencies"`
  - _Requirements: 2.1, 6.3_

- [ ] 11. Update CLI and client modules
  - Update dependencies in pdf-as-cli module
  - Update dependencies in pdf-as-web-client module
  - Test command-line interface functionality
  - Test web service client operations
  - Ensure backward compatibility of CLI commands
  - Run `./gradlew :pdf-as-cli:build :pdf-as-web-client:build test` to verify CLI and client modules work correctly
  - **Commit changes**: `git add . && git commit -m "feat: update CLI and client modules dependencies"`
  - _Requirements: 2.1, 5.1, 6.3_

- [ ] 12. Update web support modules
  - Update dependencies in pdf-as-web-db module
  - Update dependencies in pdf-as-web-status module
  - Update dependencies in pdf-as-web-statistic-api module
  - Test database integration functionality
  - Test status monitoring and statistics API
  - Run `./gradlew :pdf-as-web-db:build :pdf-as-web-status:build :pdf-as-web-statistic-api:build test` to verify web support modules work correctly
  - **Commit changes**: `git add . && git commit -m "feat: update web support modules dependencies"`
  - _Requirements: 2.1, 3.4_

- [ ] 13. Run comprehensive test suite
  - Execute all existing unit tests with updated dependencies
  - Run integration tests for PDF signing and verification
  - Test web application end-to-end functionality
  - Test CLI operations with sample PDF files
  - Verify all signature profiles and configurations work correctly
  - Run `./gradlew test` to execute full test suite across all modules
  - Run `./gradlew :pdf-as-tests:test` to execute integration tests
  - **Commit changes**: `git add . && git commit -m "test: run comprehensive test suite and fix any issues"`
  - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5_

- [ ] 14. Perform security vulnerability assessment
  - Run OWASP dependency check with updated plugin version
  - Address any high or critical security vulnerabilities found
  - Verify cryptographic libraries meet current security standards
  - Document any remaining low-risk vulnerabilities with justification
  - Run `./gradlew dependencyCheckAnalyze` to perform security vulnerability scan
  - Run `./gradlew build test` to verify fixes don't break functionality
  - **Commit changes**: `git add . && git commit -m "security: perform vulnerability assessment and address issues"`
  - _Requirements: 7.1, 7.2, 7.3, 7.4_

- [ ] 15. Update build and release processes
  - Test jar, war, and distribution archive generation
  - Verify Maven repository publishing works correctly
  - Test release task execution with updated build system
  - Update any deployment scripts for new Tomcat version
  - Ensure backward compatibility of generated artifacts
  - Run `./gradlew jar sourcesJar war distZip distTar releases` to verify all build outputs generate correctly
  - Run `./gradlew publishToMavenLocal` to test Maven publishing
  - **Commit changes**: `git add . && git commit -m "feat: update build and release processes"`
  - _Requirements: 4.5, 5.2, 5.3_

- [ ] 16. Create comprehensive documentation
  - Document all dependency version changes with rationale
  - Create migration guide for users upgrading from previous versions
  - Update build instructions for Java 17 and new Gradle version
  - Document any API changes or breaking changes
  - Update configuration migration guidance
  - Verify documentation accuracy by testing documented build procedures
  - **Commit changes**: `git add . && git commit -m "docs: create comprehensive documentation and migration guide"`
  - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5_

- [ ] 17. Upgrade Apache PDFBox to 3.x (Future Enhancement)
  - Research comprehensive PDFBox 2.x to 3.x migration guide and breaking changes
  - Update font constants from PDType1Font.HELVETICA to new PDType1Font(Standard14Fonts.FontName.HELVETICA) syntax
  - Update PDDocument.load() calls to use new Loader.loadPDF() API
  - Refactor drawing methods (drawLine, drawXObject, fillRect) to use new path construction API
  - Update signature field API calls (getWidget() method removed)
  - Update Matrix API calls (setFromAffineTransform, getXPosition, getYPosition methods changed)
  - Update OperatorProcessor constructor and context handling
  - Update COSNumber API usage (doubleValue() method changed)
  - Update PreflightParser constructor and validation API
  - Test all PDF processing functionality with PDFBox 3.x
  - Run comprehensive test suite to verify no regressions
  - **Commit changes**: `git add . && git commit -m "feat: upgrade Apache PDFBox from 2.0.34 to 3.x"`
  - _Requirements: 2.1, 2.3, 6.4_
  - **Note**: This is a complex migration requiring extensive code changes due to significant API breaking changes in PDFBox 3.x

- [ ] 18. Perform final integration testing
  - Test complete build process from clean checkout
  - Verify all modules integrate correctly with updated dependencies
  - Test deployment of web application on target Tomcat version
  - Perform end-to-end testing of PDF signing workflows
  - Validate backward compatibility with existing configurations and integrations
  - Run `./gradlew clean build test` from fresh checkout to verify complete build process
  - Run `./gradlew doFullRelease` to test full release process
  - **Commit changes**: `git add . && git commit -m "test: perform final integration testing and validation"`
  - _Requirements: 5.1, 5.2, 5.3, 6.1, 6.2, 6.3, 6.4, 6.5_