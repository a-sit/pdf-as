# Requirements Document

## Introduction

After dependency updates in the PDF-AS project, the QR code placeholder detection logic has regressed. The system now selects the wrong placeholder (Im52 instead of Im48) from a PDF containing three image objects. This is a functional regression that could affect production PDF signing functionality.

## Requirements

### Requirement 1

**User Story:** As a PDF signing system, I want to correctly identify and select the intended QR code placeholder from a PDF document, so that signatures are placed in the correct location.

#### Acceptance Criteria

1. WHEN a PDF contains multiple image objects (Im48, Im50, Im52) THEN the system SHALL identify Im48 as the primary placeholder
2. WHEN the QR code detection runs on Im48 THEN it SHALL successfully decode the QR code and extract placeholder data
3. WHEN the placeholder selection logic runs THEN it SHALL return Im48 as the selected placeholder, not Im52
4. WHEN the system processes the test PDF "platzhalter_en_de_test.pdf" THEN it SHALL return a SignaturePlaceholderData object with placeholderName="Im48"

### Requirement 2

**User Story:** As a developer, I want to understand what dependency changes caused the QR code detection regression, so that I can implement the appropriate fix.

#### Acceptance Criteria

1. WHEN investigating the dependency changes THEN the system SHALL identify which specific dependency update caused the regression
2. WHEN analyzing the QR code detection logic THEN the system SHALL determine why Im48 now fails QR code detection
3. WHEN examining the ZXing library behavior THEN the system SHALL identify any changes in QR code detection parameters or behavior
4. WHEN reviewing the processing order THEN the system SHALL understand why Im52 is now processed before Im48

### Requirement 3

**User Story:** As a quality assurance engineer, I want comprehensive test coverage for placeholder detection, so that future regressions can be prevented.

#### Acceptance Criteria

1. WHEN testing placeholder detection THEN the system SHALL verify that all three image objects (Im48, Im50, Im52) are processed correctly
2. WHEN running QR code detection tests THEN the system SHALL validate the QR code content for each detected placeholder
3. WHEN testing with different dependency versions THEN the system SHALL maintain consistent placeholder selection behavior
4. WHEN validating the fix THEN the system SHALL ensure no other placeholder detection functionality is broken

### Requirement 4

**User Story:** As a system administrator, I want the placeholder detection fix to be backward compatible, so that existing PDF documents continue to work correctly.

#### Acceptance Criteria

1. WHEN the fix is applied THEN existing PDF documents SHALL continue to have their placeholders detected correctly
2. WHEN processing PDFs with different placeholder configurations THEN the system SHALL maintain consistent behavior
3. WHEN the system encounters edge cases THEN it SHALL handle them gracefully without breaking functionality
4. WHEN validating the solution THEN all existing tests SHALL continue to pass