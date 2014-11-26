
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestDate {
	
	public static void main(String[] args) {
		
		Calendar cal = new GregorianCalendar();
		cal.set(2014,11,25);
		Date today = cal.getTime();
		System.out.println(today);
	}
}


