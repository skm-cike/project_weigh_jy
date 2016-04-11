package com.est.testcase;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class BaseSpringTestCase extends
		AbstractDependencyInjectionSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		return new String[]{"/*.xml|properties"};
	}

	@Override
	protected void injectDependencies() throws Exception {
		super.injectDependencies();
	}

	@Override
	protected void prepareTestInstance() throws Exception {
		super.prepareTestInstance();
	}
	
	public void testSample() {
		assertEquals(1, 1);
	}
	
	

	
	

}
