package uk.spindler.whereis;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IOTThingShadowTest extends IOTThingShadow {

	@Test
	public void testIOTThingShadow() {
		IOTThingShadow shObj= new IOTThingShadow();
		assertTrue(shObj != null);	
	}

	@Test
	public void testIOTThingShadowStringStringString() {
		String testJSONfileName = ".\\src\\test\\java\\uk\\spindler\\shereis\\sample_device_1_JSON.json";
		IOTThingShadow shObj= new IOTThingShadow();
		
		try{
			ExpectedException(IOException.class);
			shObj.loadDeviceShadowDocumentFromFile(testJSONfileName);
	        fail("Expected exception");
	    } catch(IOException e) {
	    	assertTrue(e==IOException);	   
	    } 
	
	}

	@Test
	public void testGetShadowDocument() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoadShadowDocument() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoadShadowDocumentBoolean() {
		fail("Not yet implemented");
	}

}
