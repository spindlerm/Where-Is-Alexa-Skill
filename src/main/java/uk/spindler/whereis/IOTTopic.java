package uk.spindler.whereis;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.http.SdkHttpMetadata;
import com.amazonaws.services.iotdata.*;
import com.amazonaws.services.iotdata.model.PublishRequest;
import com.amazonaws.services.iotdata.model.PublishResult;


public class IOTTopic {

	private String topic;
	private String endpointName;
	private String region;
	
    IOTTopic()
	{
		
	}
	
	IOTTopic(String endpointName, String region, String topic){
		this.topic = topic;
		this.endpointName = endpointName;
		this.region = region;
	}


	public int Publish(String payload) {
		
		EndpointConfiguration epc = new EndpointConfiguration(endpointName, region);
		AWSIotData iotd = AWSIotDataClient.builder().withEndpointConfiguration(epc).build();
        PublishRequest req = new PublishRequest();
        
        ByteBuffer payloadBuffer = null;
		try {
			payloadBuffer = ByteBuffer.wrap(payload.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		req.setPayload(payloadBuffer);
        req.setQos(0);
        req.setTopic(topic);

        PublishResult res = iotd.publish(req);
        
        SdkHttpMetadata httpMd = res.getSdkHttpMetadata();
        
        return httpMd.getHttpStatusCode();
	}
}
