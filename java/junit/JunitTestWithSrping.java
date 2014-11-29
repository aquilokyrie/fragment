package base;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(Junit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext*.xml")
@TransactionConfiguration(transactionManager="transactionManager")  
public class JunitTestWithSrping {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
}
