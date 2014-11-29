package suite;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import cn.com.centrin.service.impl.BuyerTransactionServiceTest;
import cn.com.centrin.service.impl.MallSupportServiceTest;
import cn.com.centrin.service.impl.SellerTransactionServiceTest;

@RunWith(Suite.class)
@SuiteClasses({BuyerTransactionServiceTest.class,SellerTransactionServiceTest.class,MallSupportServiceTest.class})
public class MallServiceTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for cn.com.centrin.service.impl");
		//$JUnit-BEGIN$

		//$JUnit-END$
		return suite;
	}

}
