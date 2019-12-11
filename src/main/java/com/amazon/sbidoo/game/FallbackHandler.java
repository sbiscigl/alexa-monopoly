package com.amazon.sbidoo.game;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import com.google.inject.Inject;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

public class FallbackHandler implements RequestHandler {

    @Inject
    public FallbackHandler() {
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("AMAZON.HelpIntent")
                .or(intentName("AMAZON.NavigateHomeIntent"))
                .or(intentName("AMAZON.CancelIntent"))
                .or(intentName("AMAZON.YesIntent"))
                .or(intentName("AMAZON.FallBackIntent"))
                .or(intentName("AMAZON.NoIntent"))
                .or(intentName("AMAZON.StopIntent"))
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
