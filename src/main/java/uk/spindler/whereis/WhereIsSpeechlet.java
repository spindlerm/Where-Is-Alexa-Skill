package uk.spindler.whereis;

/**
Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

    http://aws.amazon.com/apache2.0/

or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.OutputSpeech;


/**
* This sample shows how to create a simple speechlet for handling speechlet requests.
*/
public class WhereIsSpeechlet implements SpeechletV2{
private static final Logger log = LoggerFactory.getLogger(WhereIsSpeechlet.class);
private static final String endpoint = "a1w822yziksjvf.iot.eu-west-1.amazonaws.com";
private static final String region = "eu-west-1";
private static final String thingName = "Device3";

@Override
public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
    log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
            requestEnvelope.getSession().getSessionId());
    // any initialization logic goes here
}

@Override
public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
    log.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
            requestEnvelope.getSession().getSessionId());
    return getWelcomeResponse();
}

@Override
public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
    IntentRequest request = requestEnvelope.getRequest();
    log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
            requestEnvelope.getSession().getSessionId());
    
    BasicConfigurator.configure();
    

    Intent intent = request.getIntent();
    String intentName = (intent != null) ? intent.getName() : null;

    SpeechletResponse response = null;
    
    if ("WhereIsIntent".equals(intentName)) {
    	try {
    		response = getWhereIsResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return response;
    } else if ("HowLongAwayIntent".equals(intentName)) {
    		response = getHowLongAwayResponse();
    	return response;
    } else if ("HowFarAwayIntent".equals(intentName)) {
    	try {
    		response = getHowFarAwayResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return response;
    } else if ("AMAZON.HelpIntent".equals(intentName)) {
        return getHelpResponse();
    } else {
        return getAskResponse("Where Is Dad", "This is unsupported.  Please try something else.");
    }
}

@Override
public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
    log.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
            requestEnvelope.getSession().getSessionId());
    // any cleanup logic goes here
}

/**
 * Creates and returns a {@code SpeechletResponse} with a welcome message.
 *
 * @return SpeechletResponse spoken and visual response for the given intent
 */
private SpeechletResponse getWelcomeResponse() {
    String speechText = "Welcome to the wheres dad skill, you can say where is dad";
    return getAskResponse("Where Is Dad", speechText);
}

private SpeechletResponse getWhereIsResponse() {
   
	String speechText = "I cannot locate Dad at this time, please try again later";
	try {
		speechText = sendWhereIsDadRequest();
	} catch (Exception e) {
		e.printStackTrace();
	}
   
    // Create the Simple card content.
    SimpleCard card = getSimpleCard("Where Is Dad", speechText);

    // Create the plain text output.
    PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

    return SpeechletResponse.newTellResponse(speech, card);
}

private SpeechletResponse getHowFarAwayResponse() {
	   
	String speechText = "I cannot locate Dad at this time, please try again later";
	try {
		speechText = sendHowFarIsDadRequest();
	} catch (Exception e) {
		e.printStackTrace();
	}
   
    // Create the Simple card content.
    SimpleCard card = getSimpleCard("Where Is Dad", speechText);

    // Create the plain text output.
    PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

    return SpeechletResponse.newTellResponse(speech, card);
}

private SpeechletResponse getHowLongAwayResponse() {
	   
	String speechText = "I cannot locate Dad at this time, please try again later";
	try {
		speechText = sendHowLongAwayIsDadRequest();
	} catch (Exception e) {
		e.printStackTrace();
	}
   
    // Create the Simple card content.
    SimpleCard card = getSimpleCard("Where Is Dad", speechText);

    // Create the plain text output.
    PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

    return SpeechletResponse.newTellResponse(speech, card);
}



/**
 * Creates a {@code SpeechletResponse} for the help intent.
 *
 * @return SpeechletResponse spoken and visual response for the given intent
 */
private SpeechletResponse getHelpResponse() {
    String speechText = "You can say Where is Dad!";
    return getAskResponse("WhereIs", speechText);
}

/**
 * Helper method that creates a card object.
 * @param title title of the card
 * @param content body of the card
 * @return SimpleCard the display card to be sent along with the voice response.
 */
private SimpleCard getSimpleCard(String title, String content) {
    SimpleCard card = new SimpleCard();
    card.setTitle(title);
    card.setContent(content);

    return card;
}

/**
 * Helper method for retrieving an OutputSpeech object when given a string of TTS.
 * @param speechText the text that should be spoken out to the user.
 * @return an instance of SpeechOutput.
 */
private PlainTextOutputSpeech getPlainTextOutputSpeech(String speechText) {
    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
    speech.setText(speechText);

    return speech;
}

/**
 * Helper method that returns a reprompt object. This is used in Ask responses where you want
 * the user to be able to respond to your speech.
 * @param outputSpeech The OutputSpeech object that will be said once and repeated if necessary.
 * @return Reprompt instance.
 */
private Reprompt getReprompt(OutputSpeech outputSpeech) {
    Reprompt reprompt = new Reprompt();
    reprompt.setOutputSpeech(outputSpeech);

    return reprompt;
}

/**
 * Helper method for retrieving an Ask response with a simple card and reprompt included.
 * @param cardTitle Title of the card that you want displayed.
 * @param speechText speech text that will be spoken to the user.
 * @return the resulting card and speech text.
 */
private SpeechletResponse getAskResponse(String cardTitle, String speechText) {
    SimpleCard card = getSimpleCard(cardTitle, speechText);
    PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
    Reprompt reprompt = getReprompt(speech);

    return SpeechletResponse.newAskResponse(speech, reprompt, card);
}

private  String sendWhereIsDadRequest() throws Exception {
	
	String result = "Dad is currently in %s";
	
	IOTThingShadow iotsh = new IOTThingShadow(endpoint,region, thingName);
	IOTThingShadowDocument document = iotsh.getIOTThingShadowDocument();
	
	log.info(document.getOriginAddress());
	log.info(document.getDestinationAddress());
	
	return String.format(result, document.getOriginAddress());
}

private  String sendHowFarIsDadRequest() throws Exception {
	
	String result = "Dad is currently %s away";
	IOTThingShadow iotsh = new IOTThingShadow(endpoint,region, thingName);
	
	IOTThingShadowDocument document = iotsh.getIOTThingShadowDocument();
	
	log.info(document.getDistance());
	 
	return String.format(result, document.getDistance());
}

private  String sendHowLongAwayIsDadRequest() throws Exception {
	String result = "Dad is currently %s away";
	IOTThingShadow iotsh = new IOTThingShadow(endpoint,region, thingName);
	
	IOTThingShadowDocument document = iotsh.getIOTThingShadowDocument();
	
	log.info(document.getDuration());
	 
	return String.format(result, document.getDuration());
}
}
