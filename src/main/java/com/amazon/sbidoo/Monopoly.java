package com.amazon.sbidoo;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.sbidoo.game.GameHandler;
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
                .addRequestHandlers(injector.getInstance(GameHandler.class))
                .withSkillId(System.getenv("SKILL_ID"))
                .build();
    }
}
