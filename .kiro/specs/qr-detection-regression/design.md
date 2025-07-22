# Design Document

## Overview

This document outlines the investigation and fix strategy for the QR code placeholder detection regression in PDF-AS. The regression was introduced by dependency updates and causes the wrong placeholder to be selected from PDFs containing multiple image objects.

## Architecture

### Current System Flow
1. **PDF Processing**: PDDocument loads the PDF and identifies XObject resources
2. **Image Processing**: SignaturePlaceholderExtractor processes each image via processOperator()
3. **QR Detection**: checkImage() uses ZXing library to detect and decode QR codes
4. **Placeholder Selection**: Matching logic selects the appropriate placeholder based on ID and mode
5. **Result**: Returns SignaturePlaceholderData with the selected placeholder

### Problem Analysis
- **Expected**: Im48 should be detected as a valid QR placeholder and selected
- **Actual**: Im48 fails QR detection, Im52 is selected instead
- **Root Cause**: Dependency updates changed QR code detection behavior or processing order

## Components and Interfaces

### Investigation Components

#### 1. Dependency Analysis Component
**Purpose**: Identify which dependency changes caused the regression
**Methods**:
- Analyze build.gradle changes to identify updated dependencies
- Test individual dependency rollbacks to isolate the cause
- Focus on ZXing, slf4j, commons-io, logback, and ognl updates

#### 2. QR Code Detection Analyzer
**Purpose**: Deep dive into QR code detection behavior for each image
**Methods**:
- Extract each image (Im48, Im50, Im52) individually for analysis
- Test QR detection with different ZXing parameters and hints
- Compare image properties and QR code content between images
- Analyze why Im48 now fails detection

#### 3. Processing Order Investigator
**Purpose**: Understand why image processing order changed
**Methods**:
- Trace the order in which images are processed
- Identify what changed in PDF object iteration
- Determine if HashMap/collection iteration order changed

### Fix Strategy

#### Option 1: Fix QR Detection for Im48
**Approach**: Restore Im48's QR code detection capability
- Adjust ZXing detection parameters for better compatibility
- Add fallback detection strategies
- Implement more robust QR code detection logic

#### Option 2: Ensure Deterministic Processing Order
**Approach**: Process images in a consistent, predictable order
- Sort images by name before processing
- Implement stable ordering regardless of dependency behavior
- Ensure Im48 is always processed first

#### Option 3: Enhanced Placeholder Selection Logic
**Approach**: Improve the logic that selects which placeholder to use
- Prioritize placeholders by position or ID
- Add validation to ensure the "best" placeholder is selected
- Implement fallback selection if primary placeholder fails

## Data Models

### Investigation Results
```java
public class PlaceholderInvestigationResult {
    private Map<String, QRDetectionResult> imageAnalysis;
    private List<String> processingOrder;
    private String selectedPlaceholder;
    private String expectedPlaceholder;
    private List<String> failureReasons;
}
```

### QR Detection Result
```java
public class QRDetectionResult {
    private String imageName;
    private boolean detected;
    private String content;
    private String errorMessage;
    private long detectionTime;
}
```

## Error Handling

### QR Detection Failures
- Log detailed error information for failed detections
- Implement graceful fallback to alternative detection methods
- Provide clear diagnostic information for debugging

### Processing Order Issues
- Ensure consistent behavior across different environments
- Add logging to track image processing sequence
- Handle edge cases where expected images are missing

## Testing Strategy

### Unit Tests
- Test QR detection for each individual image
- Validate placeholder selection logic with known inputs
- Test with different dependency versions

### Integration Tests
- Test complete placeholder extraction workflow
- Validate behavior with real PDF documents
- Ensure backward compatibility with existing PDFs

### Regression Tests
- Create tests that specifically check for this regression
- Test with the problematic PDF document
- Validate that Im48 is correctly selected as the placeholder

## Implementation Phases

### Phase 1: Investigation
1. Create diagnostic tools to analyze the current behavior
2. Identify the root cause of the regression
3. Document findings and determine the best fix approach

### Phase 2: Fix Implementation
1. Implement the chosen fix strategy
2. Update existing code to handle the regression
3. Add additional error handling and logging

### Phase 3: Testing and Validation
1. Create comprehensive tests for the fix
2. Validate that the fix doesn't break other functionality
3. Test with various PDF documents and scenarios

### Phase 4: Documentation and Cleanup
1. Update documentation to reflect changes
2. Clean up any temporary diagnostic code
3. Ensure the fix is maintainable and well-documented