package at.gv.egiz.status;

import lombok.NonNull;

/**
 * The Interface Test.
 */
public interface Test {
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public @NonNull String getName();
	
	/**
	 * Gets the cache delay.
	 *
	 * @return the cache delay
	 */
	public long getCacheDelay();
	
	/**
	 * Run test.
	 *
	 * @return the test result
	 */
	public @NonNull TestResult runTest();
}
