package at.gv.egiz.pdfas.web.db.status;

import lombok.NonNull;
import java.util.ArrayList;
import java.util.List;

import at.gv.egiz.status.Test;
import at.gv.egiz.status.TestFactory;

public class DBTestFactory implements TestFactory {

	@Override
	public @NonNull List<Test> createTests() {
		List<Test> testList = new ArrayList<Test>();
		testList.add(new DBTest());
		return testList;
	}

}
