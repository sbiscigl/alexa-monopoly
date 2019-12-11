package com.amazon.sbidoo.alexa;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AlexaTurnResult {
    private final int dieOne;
    private final int dieTwo;
    private final String endPositionName;
    private final String purchaseMessage;
    private final String chargedStatement;
    private final String chanceResult;
}
