package uk.spindler.whereis;


import java.io.FileInputStream;
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

	IOTThingShadow()
	{
		
	}
	
	IOTThingShadow(String endpointName, String region, String thingName){
		this.thingName = thingName;
		this.endpointName = endpointName;
		this.region = region;
	}
	
	public IOTThingShadowDocument getIOTThingShadowDocument() {
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
  
        return new IOTThingShadowDocument(result);
	}
	
	
	JSONObject LoadShadowDocument(String fileName) throws IOException {
		if(fileName!=null)
			loadIOTThingShadowFromFile(fileName);
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
	
    IOTThingShadowDocument  loadIOTThingShadowFromFile(String fileName) throws IOException {
    	InputStream is = new FileInputStream(fileName);
    	deviceShadowJSONDocument = IOUtils.toString(is, "UTF-8");
    	return new IOTThingShadowDocument(deviceShadowJSONDocument);
    }
}
