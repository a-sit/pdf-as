# Apache PDFBox 3.x Migration Analysis

## Overview

This document provides a comprehensive analysis of the effort required to upgrade Apache PDFBox from version 2.0.34 to 3.0.5 in the PDF-AS project. The analysis was conducted as part of task 17 in the PDF-AS modernization effort.

## Current Status

- **Current Version**: Apache PDFBox 2.0.34
- **Target Version**: Apache PDFBox 3.0.5
- **Migration Status**: Analysis completed, implementation reverted due to complexity
- **Project Build Status**: ✅ Builds successfully with PDFBox 2.0.34

## Key Breaking Changes in PDFBox 3.x

### 1. Document Loading API Changes

**PDFBox 2.x:**
```java
PDDocument doc = PDDocument.load(inputStream);
```

**PDFBox 3.x:**
```java
byte[] pdfBytes = IOUtils.toByteArray(inputStream);
PDDocument doc = Loader.loadPDF(pdfBytes);
```

**Impact**: All document loading calls need to be updated to use byte arrays instead of InputStreams.

### 2. Font Constants API Changes

**PDFBox 2.x:**
```java
PDFont font = PDType1Font.HELVETICA;
```

**PDFBox 3.x:**
```java
PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
```

**Impact**: All standard font references need to be updated to use constructor-based approach.

### 3. Drawing Methods API Changes

**PDFBox 2.x:**
```java
contentStream.drawLine(x1, y1, x2, y2);
contentStream.fillRect(x, y, width, height);
```

**PDFBox 3.x:**
```java
contentStream.moveTo(x1, y1);
contentStream.lineTo(x2, y2);
contentStream.stroke();

contentStream.addRect(x, y, width, height);
contentStream.fill();
```

**Impact**: All drawing operations need to be refactored to use path construction API.

### 4. Signature Field API Changes

**PDFBox 2.x:**
```java
signatureField.getWidget().setPage(page);
signatureField.getWidget().setRectangle(rect);
signatureField.getWidget().setAppearance(appearance);
```

**PDFBox 3.x:**
```java
// Widget methods have been integrated directly into signature field
signatureField.setPage(page);        // Method removed
signatureField.setRectangle(rect);   // Method removed  
signatureField.setAppearance(appearance); // Method removed
```

**Impact**: Signature field handling requires complete refactoring.

### 5. OperatorProcessor Architecture Changes

**PDFBox 2.x:**
```java
public class MyOperator extends OperatorProcessor {
    public MyOperator(PDFPage context) {
        setContext(context);
    }
}
```

**PDFBox 3.x:**
```java
public class MyOperator extends OperatorProcessor {
    public MyOperator(PDFStreamEngine engine) {
        super(engine);
    }
}
```

**Impact**: Complete refactoring of custom operator processors required.

### 6. Matrix API Changes

**PDFBox 2.x:**
```java
Matrix matrix = new Matrix();
matrix.setFromAffineTransform(transform);
float x = matrix.getXPosition();
float y = matrix.getYPosition();
```

**PDFBox 3.x:**
```java
Matrix matrix = new Matrix(transform);
float x = matrix.getTranslateX();
float y = matrix.getTranslateY();
```

**Impact**: Matrix operations need to be updated throughout the codebase.

### 7. COSNumber API Changes

**PDFBox 2.x:**
```java
double value = cosNumber.doubleValue();
```

**PDFBox 3.x:**
```java
float value = cosNumber.floatValue();
```

**Impact**: All COSNumber value extractions need to be updated.

### 8. PreflightParser API Changes

**PDFBox 2.x:**
```java
PreflightParser parser = new PreflightParser(dataSource);
parser.parse();
PreflightDocument document = parser.getPreflightDocument();
ValidationResult result = document.getResult();
```

**PDFBox 3.x:**
```java
// API has changed significantly - requires research for new approach
```

**Impact**: PDF/A validation code needs complete rewrite.

## Files Requiring Changes

### Core PDF Processing Files
- `pdf-as-pdfbox-2/src/main/java/at/gv/egiz/pdfas/lib/impl/pdfbox2/PDFBOXObject.java`
- `pdf-as-pdfbox-2/src/main/java/at/gv/egiz/pdfas/lib/impl/signing/pdfbox2/PADESPDFBOXSigner.java`
- `pdf-as-pdfbox-2/src/main/java/at/gv/egiz/pdfas/lib/impl/verify/pdfbox2/PDFBOXVerifier.java`

### Font and Drawing Files
- `pdf-as-pdfbox-2/src/main/java/at/gv/egiz/pdfas/lib/impl/stamping/pdfbox2/PDFAsFontCache.java`
- `pdf-as-pdfbox-2/src/main/java/at/gv/egiz/pdfas/lib/impl/stamping/pdfbox2/PDFBoxFont.java`
- `pdf-as-pdfbox-2/src/main/java/at/gv/egiz/pdfas/lib/impl/stamping/pdfbox2/TableDrawUtils.java`

### Signature Processing Files
- `pdf-as-pdfbox-2/src/main/java/at/gv/egiz/pdfas/lib/impl/stamping/pdfbox2/PDFAsVisualSignatureBuilder.java`
- `pdf-as-pdfbox-2/src/main/java/at/gv/egiz/pdfas/lib/impl/stamping/pdfbox2/PDFAsTemplateCreator.java`

### Operator Processor Files (Complete Refactoring Required)
- `pdf-as-pdfbox-2/src/main/java/at/knowcenter/wag/egov/egiz/pdfbox2/pdf/PDFPage.java`
- `pdf-as-pdfbox-2/src/main/java/at/knowcenter/wag/egov/egiz/pdfbox2/pdf/operator/path/**/*.java`

### Placeholder Processing Files
- `pdf-as-pdfbox-2/src/main/java/at/gv/egiz/pdfas/lib/impl/pdfbox2/placeholder/SignaturePlaceholderExtractor.java`

### Test Files
- All test files in `pdf-as-pdfbox-2/src/test/java/**/*.java`

## Migration Effort Estimation

### High Complexity (Major Refactoring Required)
1. **OperatorProcessor Architecture** - Complete rewrite needed
2. **Signature Field Handling** - API completely changed
3. **PDF/A Validation** - PreflightParser API changed
4. **Custom Drawing Operations** - Path construction API migration

### Medium Complexity (Systematic Updates Required)
1. **Document Loading** - Update all Loader.loadPDF() calls
2. **Font Management** - Update font constant usage
3. **Matrix Operations** - Update API calls
4. **XMP Metadata Processing** - API changes

### Low Complexity (Simple Find/Replace)
1. **COSNumber API** - doubleValue() to floatValue()
2. **Basic Drawing Methods** - Update method calls
3. **Import Statements** - Add new imports

## Recommended Migration Strategy

### Phase 1: Foundation (2-3 weeks)
1. Update build dependencies to PDFBox 3.0.5
2. Fix compilation errors for basic API changes
3. Update font constants and simple API calls
4. Update document loading mechanisms

### Phase 2: Core Functionality (4-6 weeks)
1. Refactor drawing operations to use path construction API
2. Update Matrix and COSNumber API usage
3. Fix basic PDF processing functionality
4. Update test cases

### Phase 3: Advanced Features (6-8 weeks)
1. Complete OperatorProcessor architecture refactoring
2. Rewrite signature field handling
3. Update PDF/A validation code
4. Rewrite placeholder processing

### Phase 4: Testing and Validation (2-3 weeks)
1. Comprehensive testing of all PDF operations
2. Performance testing and optimization
3. Integration testing with existing systems
4. Documentation updates

## Risks and Considerations

### Technical Risks
- **Breaking Changes**: Extensive API changes may introduce subtle bugs
- **Performance Impact**: New APIs may have different performance characteristics
- **Feature Parity**: Some features may not have direct equivalents in 3.x
- **Third-party Dependencies**: Other libraries may not be compatible with PDFBox 3.x

### Business Risks
- **Development Time**: Significant development effort required (12-20 weeks)
- **Testing Overhead**: Extensive testing needed to ensure compatibility
- **Rollback Complexity**: Difficult to rollback once migration is started
- **Resource Allocation**: Requires dedicated development resources

## Alternative Approaches

### Option 1: Gradual Migration
- Maintain PDFBox 2.x for critical functionality
- Migrate non-critical features to 3.x incrementally
- Use adapter patterns to bridge API differences

### Option 2: Wrapper Layer
- Create abstraction layer over PDFBox APIs
- Implement both 2.x and 3.x backends
- Switch backends based on configuration

### Option 3: Delayed Migration
- Continue with PDFBox 2.x for now
- Monitor PDFBox 3.x ecosystem maturity
- Plan migration for future major release

## Conclusion

The PDFBox 3.x migration represents a significant undertaking due to extensive breaking changes in the API. While technically feasible, it requires substantial development effort and carries inherent risks.

**Recommendation**: Consider this migration as part of a major version release cycle rather than a routine dependency update. The current PDFBox 2.0.34 version is stable and meets current requirements.

## References

- [Apache PDFBox 3.0.0 Migration Guide](https://pdfbox.apache.org/3.0/migration.html)
- [PDFBox 3.x API Documentation](https://pdfbox.apache.org/docs/3.0.5/javadocs/)
- [PDFBox GitHub Release Notes](https://github.com/apache/pdfbox/releases/tag/3.0.0)

---

**Document Version**: 1.0  
**Last Updated**: December 2024  
**Author**: PDF-AS Modernization Team