# ZXing Version Testing Results

## Testing Plan
Test ZXing versions to find the latest working version:
- 3.4.1 ✅ (confirmed working)
- 3.5.0 ✅ (tested - working)
- 3.5.1 ❌ (tested - broken) 
- 3.5.2 ❌ (tested - broken)
- 3.5.3 ❌ (confirmed broken)

## Expected Behavior
All three placeholders should be detected:
- Im48: QR Detected: true (Content: PDF-AS-POS;id=1;profile=SIGNATURBLOCK_SMALL_EN_NOTE_PDFA)
- Im50: QR Detected: true (Content: PDF-AS-POS;id=2;profile=SIGNATURBLOCK_SMALL_DE_NOTE_PDFA)
- Im52: QR Detected: true (Content: PDF-AS-POS;id=1;profile=SIGNATURBLOCK_SMALL_DE)

## Test Results

### ZXing 3.4.1 ✅ WORKING
- Im48: QR Detected: true ✅
- Im50: QR Detected: true ✅  
- Im52: QR Detected: true ✅
- Test passes: Im48 correctly selected

### ZXing 3.5.0 ✅ WORKING  
- Im48: QR Detected: true ✅
- Im50: QR Detected: true ✅
- Im52: QR Detected: true ✅
- Test passes: Im48 correctly selected

### ZXing 3.5.1 ❌ BROKEN
- Im48: QR Detected: false ❌ (NotFoundException)
- Im50: QR Detected: false ❌ (NotFoundException)
- Im52: QR Detected: true ✅
- Test fails: Im52 selected instead of Im48

### ZXing 3.5.2 ❌ BROKEN
- Im48: QR Detected: false ❌ (NotFoundException)
- Im50: QR Detected: false ❌ (NotFoundException)  
- Im52: QR Detected: true ✅
- Test fails: Im52 selected instead of Im48

### ZXing 3.5.3 ❌ BROKEN
- Im48: QR Detected: false ❌ (NotFoundException)
- Im50: QR Detected: false ❌ (NotFoundException)
- Im52: QR Detected: true ✅  
- Test fails: Im52 selected instead of Im48

## Conclusion
**Regression introduced in ZXing 3.5.1**
**Latest working version: ZXing 3.5.0**