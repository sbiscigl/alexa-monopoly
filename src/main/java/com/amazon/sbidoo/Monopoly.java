package com.amazon.sbidoo;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.sbidoo.game.DieRollHandler;
import com.amazon.sbidoo.game.EndTurnHandler;
import com.amazon.sbidoo.game.FallbackHandler;
import com.amazon.sbidoo.game.OnEndHandler;
import com.amazon.sbidoo.game.OnStartHandler;
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
                        injector.getInstance(FallbackHandler.class))
                .withSkillId(System.getenv("SKILL_ID"))
                .build();
    }
}
