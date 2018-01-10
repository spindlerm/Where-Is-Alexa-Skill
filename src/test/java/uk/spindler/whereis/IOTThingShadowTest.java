package uk.spindler.whereis;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;


public class IOTThingShadowTest extends IOTThingShadow {

	@Test
	public void testIOTThingShadow() {
		IOTThingShadow shObj= new IOTThingShadow();
		assertTrue(shObj != null);	
	}

	@Test(expected = IOException.class)
	public void GivenNewObjectWhenInvalidJSonFileThenThrowException() throws IOException  {
		
		String testJSONfileName = ".\\src\\test\\java\\uk\\spindler\\sdfdfdfdeis\\sample_device_1_JSON.json";
		IOTThingShadow shObj = new IOTThingShadow();
		
		shObj.loadIOTThingShadowFromFile(testJSONfileName);
	}
	
	@Test
	public void GivenNewObjectWhenValidJSonFileThenLoad() throws IOException  {
		
		String testJSONfileName = ".\\src\\test\\java\\uk\\spindler\\whereis\\sample_device_1_JSON.json";
		IOTThingShadow shObj = new IOTThingShadow();
		
		IOTThingShadowDocument doc = shObj.loadIOTThingShadowFromFile(testJSONfileName);
		assertTrue(doc !=null);
	}

	@Test
	public void GivenNewObjectWhenValidJSonFileThenParseDocument() throws IOException  {
		
		String testJSONfileName = ".\\src\\test\\java\\uk\\spindler\\whereis\\sample_device_1_JSON.json";
		IOTThingShadow shObj = new IOTThingShadow();
		
		IOTThingShadowDocument doc = shObj.loadIOTThingShadowFromFile(testJSONfileName);
		
		assertTrue(doc.getDuration().equals("1 min"));
		assertTrue(doc.getDistance().equals("1 m"));
		
	}
}
