package at.gv.egiz.status;

import java.util.List;
import lombok.NonNull;

/**
 * A factory for creating Test objects.
 */
public interface TestFactory {
	
	/**
	 * Creates a new Test object.
	 *
	 * @return the list< test>
	 */
	public @NonNull List<Test> createTests();
}
