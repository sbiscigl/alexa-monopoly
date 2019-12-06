package com.amazon.sbidoo.inject;

import com.amazon.sbidoo.game.GameHandler;
import com.amazon.sbidoo.game.MonopolyGameHandler;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.S3GameStatusDao;
import com.google.inject.AbstractModule;

public class MonopolyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GameStatusDao.class).to(S3GameStatusDao.class);
        bind(GameHandler.class).to(MonopolyGameHandler.class);
    }
}
