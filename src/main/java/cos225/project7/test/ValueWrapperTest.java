package cos225.project7.test;

import static org.junit.Assert.*;

import org.junit.Test;

import cos225.project7.math.ValueWrapper;

public class ValueWrapperTest {

	@Test
	public void test() {
		ValueWrapper vw = new ValueWrapper(-100, 100);
		assertEquals(vw.wrap(200), 0, 0.001);
		assertEquals(vw.wrap(-150.5), 49.5, 0.001);
		
		assertEquals(vw.wrap(10000), 0, 0.00001);
		
		ValueWrapper v1 = new ValueWrapper(-25.5, 25.5);
		ValueWrapper v2 = new ValueWrapper(-255, 255);
		
		for (int i=-1000; i<1000; i++) {
			double d1 = v1.wrap(i+0.5);
			double d2 = v2.wrap((i+0.5) * 10.0) / 10.0;
			
			//System.out.println(i + " : " + d1 + " = " + d2);
			assertEquals(d1, d2, 0.0001);
		}
	}

}
