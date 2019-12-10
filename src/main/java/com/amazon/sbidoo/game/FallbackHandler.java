package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

public class FallbackHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("AMAZON.HelpIntent")
                .or(intentName("AMAZON.StopIntent"))
                .or(intentName("AMAZON.NavigateHomeIntent"))
                .or(intentName("AMAZON.CancelIntent"))
                .or(requestType(SessionEndedRequest.class)));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return handlerInput.getResponseBuilder()
                .withSpeech("Can you please repeat that?")
                .withShouldEndSession(false)
                .build();
    }
}
