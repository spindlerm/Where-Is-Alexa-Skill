package uk.spindler.whereis;

import org.json.JSONArray;
import org.json.JSONObject;

public class IOTThingShadowDocument {

	private JSONObject jsonObj = null;
	private JSONArray destinationAddresses = null;
	private JSONArray originAddresses = null;
	private String originAddress = null;
	private String destinationAddress = null;
	private String distance = null;
	private String duration = null;
	public  IOTThingShadowDocument(String JSONDoc) {

		jsonObj = new JSONObject(JSONDoc);
		jsonObj = jsonObj.getJSONObject("state").getJSONObject("reported"); // remove the state & Reported containers
		
		setDestinationAddresses(jsonObj.getJSONArray("destination_addresses"));
		setOriginAddresses(jsonObj.getJSONArray("origin_addresses"));
		setOriginAddress(jsonObj.getJSONArray("origin_addresses").getString(0));
		setDestinationAddress(jsonObj.getJSONArray("destination_addresses").getString(0));
		
		int distanceValue = jsonObj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getInt("value");
		String distance = jsonObj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text");
		
		
		if(distanceValue < 2)
		{
	
			distance = distance.replace(" m", " metre");
			distance = distance.replace(" km", " Kilometer");
		}
		else
		{
			distance = distance.replace(" m", " metres");
			distance = distance.replace(" km", " Kilometers");
		}
		
		setDistance(distance);
		
		int durationValue = jsonObj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getInt("value");
		String duration = jsonObj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text");
		
		if(durationValue <2 )
		{
			duration = duration.replace(" mins", " minute");
			duration = duration.replace(" h", " hour");
		}
		else
		{	
			duration = duration.replace(" mins", " minutes");
			duration = duration.replace(" h", " hours");
		}
		setDuration(duration);
	}
	
	public JSONArray getDestinationAddresses() {
		return destinationAddresses;
	}
	
	public void setDestinationAddresses(JSONArray destinationAddresses) {
		this.destinationAddresses = destinationAddresses;
	}
	
	public JSONArray getOriginAddresses() {
		return originAddresses;
	}
	
	public void setOriginAddresses(JSONArray originAddresses) {
		this.originAddresses = originAddresses;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getOriginAddress() {
		return originAddress;
	}

	public void setOriginAddress(String originAddress) {
		this.originAddress = originAddress;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
}

