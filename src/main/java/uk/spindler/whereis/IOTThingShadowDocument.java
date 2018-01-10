package uk.spindler.whereis;

import org.json.JSONArray;
import org.json.JSONObject;

public class IOTThingShadowDocument {

	private JSONObject jsonObj = null;
	private JSONArray destinationAddresses = null;
	private JSONArray originAddresses = null;
	private String distance = null;
	private String duration = null;
	public  IOTThingShadowDocument(String JSONDoc) {

		jsonObj = new JSONObject(JSONDoc);
		jsonObj = jsonObj.getJSONObject("reported"); // remove the state & Reported containers
		
		setDestinationAddresses(jsonObj.getJSONArray("destination_addresses"));
		setOriginAddresses(jsonObj.getJSONArray("origin_addresses"));
		setDistance(jsonObj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text"));
		setDuration(jsonObj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text"));
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
}

