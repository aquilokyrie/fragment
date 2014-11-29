package base;

import java.io.FileNotFoundException;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

public class Junit4ClassRunner extends SpringJUnit4ClassRunner{
	static {  
        try {  
            Log4jConfigurer.initLogging("classpath:log4junit.properties");
        } catch (FileNotFoundException ex) {  
            System.err.println("Cannot Initialize log4j");
            ex.printStackTrace();
        }  
    }
	
	public Junit4ClassRunner(Class<?> clazz) throws Exception {
		super(clazz);
	}
}
