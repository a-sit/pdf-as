package at.gv.egiz.pdfas.web.status;

import lombok.NonNull;
import java.util.ArrayList;
import java.util.List;

import at.gv.egiz.status.Test;
import at.gv.egiz.status.TestFactory;

public class PdfAsWebTestFactory implements TestFactory {

	@Override
	public @NonNull List<Test> createTests() {
		List<Test> testList = new ArrayList<Test>();
		testList.add(new SimpleAliveTest());
		return testList;
	}

}
