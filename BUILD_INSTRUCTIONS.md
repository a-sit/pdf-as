# PDF-AS Build Instructions

**Version:** 4.4.2  
**Date:** January 2025  
**Java Version:** 17 LTS  
**Gradle Version:** 8.14.3

## Quick Start

```bash
# Prerequisites: Java 17 JDK installed
git clone <repository-url>
cd pdf-as-4
./gradlew clean build
```

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Environment Setup](#environment-setup)
3. [Building the Project](#building-the-project)
4. [Module-Specific Builds](#module-specific-builds)
5. [Testing](#testing)
6. [Distribution Generation](#distribution-generation)
7. [IDE Configuration](#ide-configuration)
8. [Troubleshooting](#troubleshooting)
9. [Advanced Build Options](#advanced-build-options)

## Prerequisites

### Required Software

#### Java Development Kit (JDK) 17
- **Minimum Version**: OpenJDK 17 or Oracle JDK 17
- **Recommended**: Latest OpenJDK 17 LTS

**Installation Options**:

**Ubuntu/Debian**:
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**CentOS/RHEL/Fedora**:
```bash
sudo dnf install java-17-openjdk-devel
# or
sudo yum install java-17-openjdk-devel
```

**macOS (Homebrew)**:
```bash
brew install openjdk@17
```

**Windows**:
- Download from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/)
- Install and set JAVA_HOME environment variable

#### Git
- **Version**: Any recent version
- **Purpose**: Source code management

#### Internet Connection
- **Purpose**: Download dependencies from Maven repositories
- **Bandwidth**: Initial build may download ~500MB of dependencies

### Optional Software

#### IDE Support
- **IntelliJ IDEA**: 2021.3 or later (recommended)
- **Eclipse**: 2021-12 or later with Gradle plugin
- **Visual Studio Code**: With Java Extension Pack

#### Application Servers (for web deployment)
- **Apache Tomcat**: 10.1.x or later (for Jakarta EE support)
- **Eclipse Jetty**: 11.x or later

## Environment Setup

### Java Configuration

#### 1. Verify Java Installation
```bash
java -version
# Should show: openjdk version "17.x.x" or java version "17.x.x"

javac -version
# Should show: javac 17.x.x
```

#### 2. Set JAVA_HOME (if not set)

**Linux/macOS**:
```bash
# Add to ~/.bashrc or ~/.zshrc
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
# or find with:
export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")
```

**Windows**:
```cmd
# Set environment variable
set JAVA_HOME=C:\Program Files\Java\jdk-17
# or use System Properties → Environment Variables
```

#### 3. Verify JAVA_HOME
```bash
echo $JAVA_HOME
# Should point to Java 17 installation directory
```

### Git Configuration

#### 1. Clone Repository
```bash
git clone <repository-url>
cd pdf-as-4
```

#### 2. Verify Repository Structure
```bash
ls -la
# Should show: build.gradle, gradle/, settings.gradle, etc.
```

### Network Configuration

#### 1. Proxy Settings (if behind corporate firewall)

**Gradle Proxy Configuration** (`~/.gradle/gradle.properties`):
```properties
systemProp.http.proxyHost=proxy.company.com
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=proxy.company.com
systemProp.https.proxyPort=8080
systemProp.http.proxyUser=username
systemProp.http.proxyPassword=password
```

#### 2. Repository Access
Verify access to required repositories:
- Maven Central: https://repo1.maven.org/maven2/
- Spring Milestone: https://repo.spring.io/milestone/
- EGIZ Maven: https://apps.egiz.gv.at/maven/

## Building the Project

### Basic Build Commands

#### 1. Clean Build
```bash
# Clean all previous build artifacts and build from scratch
./gradlew clean build
```

#### 2. Quick Build (Skip Tests)
```bash
# Faster build for development
./gradlew clean build -x test
```

#### 3. Build with Verbose Output
```bash
# Detailed build information for troubleshooting
./gradlew clean build --info
```

#### 4. Build with Stack Traces
```bash
# Full error details if build fails
./gradlew clean build --stacktrace
```

### Build Phases

The build process consists of several phases:

#### 1. **Dependency Resolution** (~2-5 minutes first time)
- Downloads required dependencies from Maven repositories
- Subsequent builds use cached dependencies

#### 2. **Compilation** (~1-3 minutes)
- Compiles Java source code for all modules
- Processes resources and configuration files

#### 3. **Testing** (~2-5 minutes)
- Runs unit tests for all modules
- Generates test reports

#### 4. **Packaging** (~1-2 minutes)
- Creates JAR files for each module
- Generates source JARs
- Creates WAR file for web application

#### 5. **Verification** (~1 minute)
- Runs additional verification tasks
- Validates build outputs

### Build Output

Successful build produces:

```
BUILD SUCCESSFUL in 8m 32s
142 actionable tasks: 142 executed
```

Build artifacts are located in:
- **Module JARs**: `{module}/build/libs/`
- **Web Application**: `pdf-as-web/build/libs/pdf-as-web-4.4.2.war`
- **Distribution Archives**: `{module}/build/distributions/`
- **Test Reports**: `{module}/build/reports/tests/`

## Module-Specific Builds

### Core Modules

#### pdf-as-lib (Core Library)
```bash
# Build core library only
./gradlew :pdf-as-lib:build

# Run core library tests
./gradlew :pdf-as-lib:test

# Generate API documentation
./gradlew :pdf-as-lib:apidocs
```

#### pdf-as-common (Common Utilities)
```bash
# Build common utilities
./gradlew :pdf-as-common:build

# Test common utilities
./gradlew :pdf-as-common:test
```

### Web Components

#### pdf-as-web (Web Application)
```bash
# Build web application
./gradlew :pdf-as-web:build

# Generate WAR file
./gradlew :pdf-as-web:war

# Start development server
./gradlew :pdf-as-web:grettyRun

# Build Tomcat distribution
./gradlew :pdf-as-web:buildTomcat
```

#### pdf-as-web-db (Database Integration)
```bash
# Build database module
./gradlew :pdf-as-web-db:build

# Test database functionality
./gradlew :pdf-as-web-db:test
```

### Application Modules

#### pdf-as-cli (Command Line Interface)
```bash
# Build CLI application
./gradlew :pdf-as-cli:build

# Create distribution archive
./gradlew :pdf-as-cli:distZip

# Run CLI application
./gradlew :pdf-as-cli:run --args="-p SIGNATURBLOCK_DE -c bku -m sign test.pdf"
```

### PDF Processing

#### pdf-as-pdfbox-2 (PDFBox Backend)
```bash
# Build PDFBox module
./gradlew :pdf-as-pdfbox-2:build

# Test PDF processing
./gradlew :pdf-as-pdfbox-2:test
```

### Signature Standards

#### Signature Standards Modules
```bash
# Build PAdES signature module
./gradlew :signature-standards:sigs-pades:build

# Build PKCS#7 detached signature module
./gradlew :signature-standards:sigs-pkcs7detached:build

# Build all signature standards
./gradlew :signature-standards:build
```

### Legacy Support

#### pdf-as-legacy (Legacy Compatibility)
```bash
# Build legacy module (minimal changes)
./gradlew :pdf-as-legacy:build

# Test legacy compatibility
./gradlew :pdf-as-legacy:test
```

## Testing

### Test Execution

#### 1. Run All Tests
```bash
# Execute all unit and integration tests
./gradlew test
```

#### 2. Run Specific Module Tests
```bash
# Test specific module
./gradlew :pdf-as-lib:test

# Test multiple modules
./gradlew :pdf-as-lib:test :pdf-as-web:test
```

#### 3. Run Specific Test Classes
```bash
# Run specific test class
./gradlew :pdf-as-lib:test --tests "at.gv.egiz.pdfas.lib.test.SignTest"

# Run tests matching pattern
./gradlew test --tests "*SignTest"
```

#### 4. Integration Tests
```bash
# Run integration test suite
./gradlew :pdf-as-tests:test

# Run with specific test profile
./gradlew :pdf-as-tests:test -Dtest.profile=SIGNATURBLOCK_DE
```

### Test Reports

Test reports are generated in:
- **HTML Reports**: `{module}/build/reports/tests/test/index.html`
- **XML Reports**: `{module}/build/test-results/test/`
- **JUnit Reports**: Compatible with CI/CD systems

#### View Test Reports
```bash
# Open test report in browser (macOS)
open pdf-as-lib/build/reports/tests/test/index.html

# Open test report in browser (Linux)
xdg-open pdf-as-lib/build/reports/tests/test/index.html
```

### Test Configuration

#### Skip Tests
```bash
# Skip all tests
./gradlew build -x test

# Skip specific module tests
./gradlew build -x :pdf-as-web:test
```

#### Test with System Properties
```bash
# Run tests with custom properties
./gradlew test -Dpdf-as-web.conf=/path/to/config.properties
```

## Distribution Generation

### Standard Distributions

#### 1. Generate All Distributions
```bash
# Create all distribution formats
./gradlew jar sourcesJar war distZip distTar releases
```

#### 2. JAR Files
```bash
# Generate JAR files for all modules
./gradlew jar

# Generate source JARs
./gradlew sourcesJar
```

#### 3. Web Application
```bash
# Generate WAR file
./gradlew war

# Build complete Tomcat distribution
./gradlew :pdf-as-web:buildTomcat
```

#### 4. Distribution Archives
```bash
# Generate ZIP distributions
./gradlew distZip

# Generate TAR distributions
./gradlew distTar
```

### Release Generation

#### 1. Full Release Build
```bash
# Generate complete release package
./gradlew doFullRelease
```

#### 2. Release Contents
The release process creates:
- **JAR Files**: All module JARs and source JARs
- **WAR File**: Web application
- **Documentation**: API docs and user documentation
- **Configuration**: Default configuration files
- **Tomcat Distribution**: Complete Tomcat with PDF-AS deployed
- **CLI Distribution**: Command-line interface package

#### 3. Release Location
Release artifacts are created in:
```
releases/4.4.2/
├── docs/                    # Documentation
├── cfg/                     # Configuration files
├── licenses/                # License files
├── pdf-as-*.jar            # Module JARs
├── pdf-as-web-4.4.2.war    # Web application
└── apache-tomcat-*.zip     # Tomcat distribution
```

### Maven Repository Publishing

#### 1. Publish to Local Repository
```bash
# Install to local Maven repository (~/.m2/repository)
./gradlew publishToMavenLocal
```

#### 2. Publish to Remote Repository
```bash
# Publish to configured remote repository
./gradlew publish
```

## IDE Configuration

### IntelliJ IDEA

#### 1. Import Project
1. **File** → **Open**
2. Select `build.gradle` file
3. Choose **Open as Project**
4. Wait for Gradle sync to complete

#### 2. Configure Java Version
1. **File** → **Project Structure**
2. **Project** → **Project SDK**: Select Java 17
3. **Project** → **Project language level**: 17
4. **Modules** → Select each module → **Language level**: 17

#### 3. Configure Gradle
1. **File** → **Settings** → **Build, Execution, Deployment** → **Gradle**
2. **Gradle JVM**: Select Java 17
3. **Build and run using**: Gradle
4. **Run tests using**: Gradle

#### 4. Code Style
1. **File** → **Settings** → **Editor** → **Code Style** → **Java**
2. **Scheme**: Default or create custom scheme
3. **Tabs and Indents**: Use spaces, indent size 4

### Eclipse

#### 1. Import Project
1. **File** → **Import** → **Gradle** → **Existing Gradle Project**
2. Select project root directory
3. Click **Finish**

#### 2. Configure Java Version
1. Right-click project → **Properties**
2. **Java Build Path** → **Libraries**
3. Remove old JRE, add **Modulepath** → **JRE System Library** → Java 17

#### 3. Configure Gradle
1. **Window** → **Preferences** → **Gradle**
2. **Java home**: Point to Java 17 installation
3. **Gradle distribution**: Use Gradle wrapper

### Visual Studio Code

#### 1. Install Extensions
- **Extension Pack for Java** (Microsoft)
- **Gradle for Java** (Microsoft)

#### 2. Configure Java
1. **Ctrl+Shift+P** → **Java: Configure Runtime**
2. Set Java 17 as default runtime

#### 3. Configure Gradle
1. **Ctrl+Shift+P** → **Java: Configure Classpath**
2. Ensure Gradle wrapper is used

## Troubleshooting

### Common Build Issues

#### 1. Java Version Problems

**Error**: `UnsupportedClassVersionError`
```
java.lang.UnsupportedClassVersionError: ... has been compiled by a more recent version
```

**Solution**:
```bash
# Check Java version
java -version
javac -version

# Update JAVA_HOME
export JAVA_HOME=/path/to/java17

# Verify Gradle uses correct Java
./gradlew -version
```

#### 2. Gradle Wrapper Issues

**Error**: `Could not find or load main class org.gradle.wrapper.GradleWrapperMain`

**Solution**:
```bash
# Regenerate Gradle wrapper
gradle wrapper --gradle-version 8.14.3

# Or download manually
curl -L https://services.gradle.org/distributions/gradle-8.14.3-bin.zip -o gradle.zip
```

#### 3. Dependency Resolution Issues

**Error**: `Could not resolve dependency`

**Solution**:
```bash
# Clear Gradle cache
rm -rf ~/.gradle/caches/

# Refresh dependencies
./gradlew clean build --refresh-dependencies

# Check network connectivity
curl -I https://repo1.maven.org/maven2/
```

#### 4. Memory Issues

**Error**: `OutOfMemoryError` during build

**Solution**:
```bash
# Increase Gradle memory
export GRADLE_OPTS="-Xmx4g -XX:MaxMetaspaceSize=1g"

# Or edit gradle.properties
echo "org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=1g" >> gradle.properties
```

#### 5. Test Failures

**Error**: Tests fail after environment changes

**Solution**:
```bash
# Run tests with verbose output
./gradlew test --info

# Run specific failing test
./gradlew :module:test --tests "FailingTest" --info

# Skip tests temporarily
./gradlew build -x test
```

### Network Issues

#### 1. Proxy Configuration

**Corporate Firewall**:
```bash
# Configure Gradle proxy
mkdir -p ~/.gradle
cat > ~/.gradle/gradle.properties << EOF
systemProp.http.proxyHost=proxy.company.com
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=proxy.company.com
systemProp.https.proxyPort=8080
EOF
```

#### 2. Repository Access

**Test Repository Access**:
```bash
# Test Maven Central
curl -I https://repo1.maven.org/maven2/

# Test Spring Repository
curl -I https://repo.spring.io/milestone/

# Test EGIZ Repository
curl -I https://apps.egiz.gv.at/maven/
```

### Performance Issues

#### 1. Slow Builds

**Optimization**:
```bash
# Enable parallel builds
echo "org.gradle.parallel=true" >> gradle.properties

# Enable build cache
echo "org.gradle.caching=true" >> gradle.properties

# Use daemon
echo "org.gradle.daemon=true" >> gradle.properties
```

#### 2. IDE Performance

**IntelliJ IDEA**:
- Increase heap size: **Help** → **Edit Custom VM Options** → `-Xmx4g`
- Exclude build directories: **File** → **Settings** → **Build** → **Compiler** → **Excludes**

## Advanced Build Options

### Security Scanning

#### 1. OWASP Dependency Check
```bash
# Run security vulnerability scan
./gradlew dependencyCheckAnalyze

# View security report
open build/reports/dependency-check-report.html
```

#### 2. Configure NVD API Key
```bash
# Set environment variable for faster updates
export NVD_API_KEY=your-api-key-here

# Or configure in gradle.properties
echo "nvdApiKey=your-api-key-here" >> gradle.properties
```

### Dependency Management

#### 1. Check for Updates
```bash
# Check for dependency updates
./gradlew dependencyUpdates

# View update report
open build/dependencyUpdates/report.html
```

#### 2. Dependency Insights
```bash
# Analyze specific dependency
./gradlew dependencyInsight --dependency org.slf4j:slf4j-api

# Show all dependencies
./gradlew dependencies
```

### Custom Build Configurations

#### 1. Build Profiles
```bash
# Build with specific profile
./gradlew build -Pprofile=production

# Build with custom properties
./gradlew build -Dcustom.property=value
```

#### 2. Environment-Specific Builds
```bash
# Development build
./gradlew build -Penv=dev

# Production build
./gradlew build -Penv=prod
```

### Continuous Integration

#### 1. CI-Friendly Commands
```bash
# Clean build for CI
./gradlew clean build --no-daemon --stacktrace

# Generate CI reports
./gradlew build test jacocoTestReport
```

#### 2. Docker Build
```dockerfile
# Dockerfile example
FROM openjdk:17-jdk-slim
COPY . /app
WORKDIR /app
RUN ./gradlew clean build -x test
```

## Performance Benchmarks

### Build Performance (Typical Development Machine)

| Task | Time (Clean) | Time (Incremental) |
|------|-------------|-------------------|
| **Clean Build** | 8-12 minutes | N/A |
| **Incremental Build** | N/A | 30-60 seconds |
| **Test Execution** | 3-5 minutes | 1-2 minutes |
| **Distribution Generation** | 2-3 minutes | 1 minute |
| **Security Scan** | 5-8 minutes | 2-3 minutes |

### System Requirements

| Component | Minimum | Recommended |
|-----------|---------|-------------|
| **RAM** | 4 GB | 8 GB |
| **CPU** | 2 cores | 4+ cores |
| **Disk Space** | 2 GB | 5 GB |
| **Network** | Broadband | Broadband |

---

## Summary

The PDF-AS build system provides:

- ✅ **Modern Build System**: Gradle 8.14.3 with Java 17 support
- ✅ **Modular Architecture**: Independent module builds
- ✅ **Comprehensive Testing**: Unit and integration tests
- ✅ **Security Scanning**: OWASP dependency check integration
- ✅ **Multiple Output Formats**: JARs, WARs, distributions
- ✅ **IDE Integration**: Support for major IDEs
- ✅ **CI/CD Ready**: Suitable for automated builds

For additional help or issues not covered in this guide, please refer to the project documentation or contact the development team.

---

**Document Version**: 1.0  
**Last Updated**: January 2025  
**Next Review**: When build system changes