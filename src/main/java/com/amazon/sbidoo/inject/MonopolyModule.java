package com.amazon.sbidoo.inject;

import com.amazon.sbidoo.game.DieRollHandler;
import com.amazon.sbidoo.game.EndTurnHandler;
import com.amazon.sbidoo.game.MonopolyDieHandler;
import com.amazon.sbidoo.game.MonopolyEndHandler;
import com.amazon.sbidoo.game.MonopolyEndTurnHandler;
import com.amazon.sbidoo.game.MonopolyStartHandler;
import com.amazon.sbidoo.game.OnEndHandler;
import com.amazon.sbidoo.game.OnStartHandler;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.S3GameStatusDao;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MonopolyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GameStatusDao.class).to(S3GameStatusDao.class);
        bind(AmazonS3.class).toInstance(AmazonS3ClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build());
        bind(OnStartHandler.class)
                .to(MonopolyStartHandler.class);
        bind(OnEndHandler.class)
                .to(MonopolyEndHandler.class);
        bind(DieRollHandler.class)
                .to(MonopolyDieHandler.class);
        bind(EndTurnHandler.class)
                .to(MonopolyEndTurnHandler.class);

        bind(Logger.class)
                .annotatedWith(Names.named("S3GameStatusDaoLogger"))
                .toInstance(LogManager.getLogger(S3GameStatusDao.class));
    }
}
