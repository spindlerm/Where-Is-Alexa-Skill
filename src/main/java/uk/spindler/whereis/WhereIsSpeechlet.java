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
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.speechlet.interfaces.system.SystemInterface;
import com.amazon.speech.speechlet.interfaces.system.SystemState;
import com.amazon.speech.speechlet.services.DirectiveEnvelope;
import com.amazon.speech.speechlet.services.DirectiveEnvelopeHeader;
import com.amazon.speech.speechlet.services.DirectiveService;
import com.amazon.speech.speechlet.services.SpeakDirective;
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
private static String thingName = "";
private static String person = "";
/**
 * Service to send progressive response directives.
 */
private DirectiveService directiveService;

@Override
public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
    log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
            requestEnvelope.getSession().getSessionId());

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
    

    // Dispatch a progressive response to engage the user while fetching events
    SystemState systemState = getSystemState(requestEnvelope.getContext());
    String apiEndpoint = systemState.getApiEndpoint();
    dispatchProgressiveResponse(request.getRequestId(), String.format("Attempting to locate %s current position",person), systemState, apiEndpoint);
    
    if(systemState.getApplication().getApplicationId().equals("amzn1.ask.skill.76b419fd-8c03-426f-8c76-88e3488e75ea"))
    {
    	person = "Dad";
    	thingName = "Device3";
    } else if(systemState.getApplication().getApplicationId().equals("amzn1.ask.skill.8dcccec9-3769-4d6c-a550-31df17fc3a08")){
    	person = "Caroline";
    	thingName = "Caroline";
    }
    
    log.info(person);
    log.info(thingName);
    log.info(systemState.getApplication().getApplicationId());
    
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
        return getAskResponse(String.format("Where Is %s", person), "This is unsupported.  Please try something else.");
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
    String speechText = String.format("Welcome to the wheres %s skill, you can say where is %s", person, person);
    return getAskResponse(String.format("Where Is %s", person), speechText);
}

private SpeechletResponse getWhereIsResponse() {
   
	 String speechText = String.format("I cannot locate %s at this time, please try again later",person);
	try {
		speechText = sendWhereIsDadRequest();
	} catch (Exception e) {
		e.printStackTrace();
	}
   
    // Create the Simple card content.
    SimpleCard card = getSimpleCard(String.format("Where Is %s", person), speechText);

    // Create the plain text output.
    PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

    return SpeechletResponse.newTellResponse(speech, card);
}

private SpeechletResponse getHowFarAwayResponse() {
	   
	String speechText = String.format("I cannot locate %s at this time, please try again later", person);
	try {
		speechText = sendHowFarIsDadRequest();
	} catch (Exception e) {
		e.printStackTrace();
	}
   
    // Create the Simple card content.
    SimpleCard card = getSimpleCard(String.format("Where Is %s", person), speechText);

    // Create the plain text output.
    PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

    return SpeechletResponse.newTellResponse(speech, card);
}

private SpeechletResponse getHowLongAwayResponse() {
	   
	String speechText = String.format("I cannot locate %s at this time, please try again later",person);
	try {
		speechText = sendHowLongAwayIsDadRequest();
	} catch (Exception e) {
		e.printStackTrace();
	}
   
    // Create the Simple card content.
    SimpleCard card = getSimpleCard(String.format("Where Is %s", person), speechText);

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
    String speechText = String.format("You can say Where is %s!", person);
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
	String result = "%s is currently in %s";
	
	IOTThingShadow iotsh = new IOTThingShadow(endpoint,region, thingName);
	IOTThingShadowDocument document = iotsh.getIOTThingShadowDocument();
	
	log.info(document.getOriginAddress());
	log.info(document.getDestinationAddress());
	
	return String.format(result, person, document.getOriginAddress());
}

private  String sendHowFarIsDadRequest() throws Exception {
	
	String result = "%s is currently %s away";
	IOTThingShadow iotsh = new IOTThingShadow(endpoint,region, thingName);
	
	IOTThingShadowDocument document = iotsh.getIOTThingShadowDocument();
	
	log.info(document.getDistance());
	 
	return String.format(result, person, document.getDistance());
}

private  String sendHowLongAwayIsDadRequest() throws Exception {
	String result = "%s is currently %s away";
	IOTThingShadow iotsh = new IOTThingShadow(endpoint,region, thingName);
	
	IOTThingShadowDocument document = iotsh.getIOTThingShadowDocument();
	
	log.info(document.getDuration());
	 
	return String.format(result, person, document.getDuration());
}

/**
 * Dispatches a progressive response.
 *
 * @param requestId
 *            the unique request identifier
 * @param text
 *            the text of the progressive response to send
 * @param systemState
 *            the SystemState object
 * @param apiEndpoint
 *            the Alexa API endpoint
 */
private void dispatchProgressiveResponse(String requestId, String text, SystemState systemState, String apiEndpoint) {
    DirectiveEnvelopeHeader header = DirectiveEnvelopeHeader.builder().withRequestId(requestId).build();
    SpeakDirective directive = SpeakDirective.builder().withSpeech(text).build();
    DirectiveEnvelope directiveEnvelope = DirectiveEnvelope.builder()
            .withHeader(header).withDirective(directive).build();

    if(systemState.getApiAccessToken() != null && !systemState.getApiAccessToken().isEmpty()) {
        String token = systemState.getApiAccessToken();
        try {
            directiveService.enqueue(directiveEnvelope, apiEndpoint, token);
        } catch (Exception e) {
            log.error("Failed to dispatch a progressive response", e);
        }
    }
}

/**
 * Helper method that retrieves the system state from the request context.
 * @param context request context.
 * @return SystemState the systemState
 */
private SystemState getSystemState(Context context) {
    return context.getState(SystemInterface.class, SystemState.class);
}

}
