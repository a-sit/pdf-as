package at.gv.egiz.pdfas.web.status;

import lombok.NonNull;
import at.gv.egiz.status.Test;
import at.gv.egiz.status.TestResult;
import at.gv.egiz.status.TestStatus;
import at.gv.egiz.status.impl.BaseTestResult;

public class SimpleAliveTest implements Test {

	@Override
	public @NonNull String getName() {
		return "ALIVE";
	}

	@Override
	public long getCacheDelay() {
		return 0;
	}

	@Override
	public @NonNull TestResult runTest() {
		BaseTestResult result = new BaseTestResult();
		result.setStatus(TestStatus.OK);
		return result;
	}

}
