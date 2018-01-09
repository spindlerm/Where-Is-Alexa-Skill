package uk.spindler.whereis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.http.SdkHttpMetadata;
import com.amazonaws.services.iotdata.AWSIotData;
import com.amazonaws.services.iotdata.AWSIotDataClient;
import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;

public class IOTThingShadow {

	private String thingName;
	private String endpointName;
	private String region;
	private String deviceShadowJSONDocument;
	private JSONObject deviceShadowJSONObject;
	private String testJSONfileName = ".\\src\\main\\java\\uk\\spindler\\helloworld\\sample_device_1_JSON.json";
	private boolean loadFromFile = true;

	IOTThingShadow()
	{
		
	}
	
	IOTThingShadow(String endpointName, String region, String thingName){
		this.thingName = thingName;
		this.endpointName = endpointName;
		this.region = region;
	}
	
	public String getShadowDocument() {
		String result = "";
		EndpointConfiguration epc = new EndpointConfiguration(endpointName,region);
        AWSIotData iotd = AWSIotDataClient.builder().withEndpointConfiguration(epc).build();
		GetThingShadowRequest shreq = new  GetThingShadowRequest(); 
		shreq.setThingName(thingName);
       
    
        GetThingShadowResult shres = iotd.getThingShadow(shreq);
        
        SdkHttpMetadata httpMd = shres.getSdkHttpMetadata();
        
        if(httpMd.getHttpStatusCode() == 200)
        {
        	ByteBuffer respayload = shres.getPayload();
        	result = new String(respayload.array());
        }
  
        return result;
	}
	
	
	JSONObject LoadShadowDocument(String fileName) throws IOException {
		if(fileName!=null)
			loadDeviceShadowDocumentFromFile(fileName);
		else
			loadDeviceShadowDocumentOverHTTP();
  
        return deviceShadowJSONObject;
		
	}
	
	private  void  loadDeviceShadowDocumentOverHTTP() {
		EndpointConfiguration epc = new EndpointConfiguration(endpointName,region);
        AWSIotData iotd = AWSIotDataClient.builder().withEndpointConfiguration(epc).build();
		GetThingShadowRequest shreq = new  GetThingShadowRequest(); 
		shreq.setThingName(thingName);
       
    
        GetThingShadowResult shres = iotd.getThingShadow(shreq);
        
        SdkHttpMetadata httpMd = shres.getSdkHttpMetadata();
        
        if(httpMd.getHttpStatusCode() == 200)
        {
        	ByteBuffer respayload = shres.getPayload();
        	deviceShadowJSONDocument = new String(respayload.array());
        	
        	deviceShadowJSONObject = new JSONObject(deviceShadowJSONDocument);
        }
	}
	
    void  loadDeviceShadowDocumentFromFile(String fileName) throws IOException {
    	InputStream is = new FileInputStream(fileName);
    	deviceShadowJSONDocument = IOUtils.toString(is, "UTF-8");
		deviceShadowJSONObject = new JSONObject(deviceShadowJSONDocument);
    }
}
