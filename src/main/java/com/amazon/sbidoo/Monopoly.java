package com.amazon.sbidoo;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.sbidoo.game.DieRollHandler;
import com.amazon.sbidoo.game.EndTurnHandler;
import com.amazon.sbidoo.game.FallbackHandler;
import com.amazon.sbidoo.game.HotelPurchaseHandler;
import com.amazon.sbidoo.game.HousePurchaseHandler;
import com.amazon.sbidoo.game.OnEndHandler;
import com.amazon.sbidoo.game.OnStartHandler;
import com.amazon.sbidoo.game.PropertyPurchaseHandler;
import com.amazon.sbidoo.game.StopHandler;
import com.amazon.sbidoo.inject.MonopolyModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Monopoly extends SkillStreamHandler {

    private static final Injector injector = Guice.createInjector(new MonopolyModule());

    public Monopoly() {
        super(getSkill());
    }

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(injector.getInstance(OnStartHandler.class),
                        injector.getInstance(OnEndHandler.class),
                        injector.getInstance(DieRollHandler.class),
                        injector.getInstance(EndTurnHandler.class),
                        injector.getInstance(FallbackHandler.class),
                        injector.getInstance(HousePurchaseHandler.class),
                        injector.getInstance(HotelPurchaseHandler.class),
                        injector.getInstance(PropertyPurchaseHandler.class),
                        injector.getInstance(StopHandler.class))
                .withSkillId(System.getenv("SKILL_ID"))
                .build();
    }
}
