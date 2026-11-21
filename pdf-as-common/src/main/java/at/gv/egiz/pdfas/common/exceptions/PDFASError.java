package at.gv.egiz.pdfas.common.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import at.gv.egiz.pdfas.common.messages.ErrorCodeResolver;
import lombok.Getter;
import lombok.NonNull;

/**
 * The Class PDFASError.
 */
public class PDFASError extends PdfAsException implements ErrorConstants {

	@Getter
    private long code;

	@Getter
	private final @NonNull Map<String, String> processInformations = new HashMap<String, String>();

	public PDFASError(long code) {
		super(ErrorCodeResolver.resolveMessage(code));
		this.code = code;
	}

	public PDFASError(long code, Throwable e) {
		super(ErrorCodeResolver.resolveMessage(code), e);
		this.code = code;
	}

	public PDFASError(long code, String info, Throwable e) {
		super(Objects.requireNonNullElseGet(info, () -> ErrorCodeResolver.resolveMessage(code)), e);
		this.code = code;
	}

	public PDFASError(long code, String info) {
		super(Objects.requireNonNullElseGet(info, () -> ErrorCodeResolver.resolveMessage(code)));
		this.code = code;
	}

	public static @NonNull PDFASError wrap(long code, Throwable cause) {
		return cause instanceof PDFASError error ? error : new PDFASError(code, cause);
	}

	@Override
	protected @NonNull String localizeMessage(@NonNull String message) {
		// Numeric-code messages and explicit details are already resolved.
		return message;
	}

	public @NonNull String getMessage() { return super.getMessage(); }

	/** @deprecated use getMessage() instead */
	@Deprecated(forRemoval = true)
	public @NonNull String getInfo() {
		return this.getMessage();
	}

	/**
	 * Gets the code info.
	 *
	 * @return the code info
	 */
	public @NonNull String getCodeInfo() {
		return ErrorCodeResolver.resolveMessage(code);
	}

	public static @NonNull String buildInfoString(long code, Object... args) {
		return String.format(ErrorCodeResolver.resolveMessage(code), args);
	}
}
