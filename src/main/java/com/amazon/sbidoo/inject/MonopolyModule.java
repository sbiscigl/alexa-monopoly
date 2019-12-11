package com.amazon.sbidoo.inject;

import com.amazon.sbidoo.alexa.AlexaTurnHandler;
import com.amazon.sbidoo.alexa.MonopolyAlexaPlayerGameStatus;
import com.amazon.sbidoo.chance.ChanceDao;
import com.amazon.sbidoo.chance.MonopolyChanceDao;
import com.amazon.sbidoo.community.CommunityChestDao;
import com.amazon.sbidoo.community.MonopolyCommunityChestDao;
import com.amazon.sbidoo.game.DieRollHandler;
import com.amazon.sbidoo.game.EndTurnHandler;
import com.amazon.sbidoo.game.FallbackHandler;
import com.amazon.sbidoo.game.HotelPurchaseHandler;
import com.amazon.sbidoo.game.HousePurchaseHandler;
import com.amazon.sbidoo.game.MonopolyDieHandler;
import com.amazon.sbidoo.game.MonopolyEndHandler;
import com.amazon.sbidoo.game.MonopolyEndTurnHandler;
import com.amazon.sbidoo.game.MonopolyHotelPurchaseHandler;
import com.amazon.sbidoo.game.MonopolyHousePurchaseHandler;
import com.amazon.sbidoo.game.MonopolyPropertyPurchaseHandler;
import com.amazon.sbidoo.game.MonopolyStartHandler;
import com.amazon.sbidoo.game.OnEndHandler;
import com.amazon.sbidoo.game.OnStartHandler;
import com.amazon.sbidoo.game.PropertyPurchaseHandler;
import com.amazon.sbidoo.game.StopHandler;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.S3GameStatusDao;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MonopolyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GameStatusDao.class).to(S3GameStatusDao.class);
        bind(Gson.class)
                .toInstance(new GsonBuilder()
                        .enableComplexMapKeySerialization()
                        .create());
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
        bind(AlexaTurnHandler.class)
                .to(MonopolyAlexaPlayerGameStatus.class);
        bind(PropertyPurchaseHandler.class)
                .to(MonopolyPropertyPurchaseHandler.class);
        bind(HousePurchaseHandler.class)
                .to(MonopolyHousePurchaseHandler.class);
        bind(HotelPurchaseHandler.class)
                .to(MonopolyHotelPurchaseHandler.class);
        bind(ChanceDao.class)
                .to(MonopolyChanceDao.class);
        bind(CommunityChestDao.class)
                .to(MonopolyCommunityChestDao.class);
        bind(FallbackHandler.class);
        bind(StopHandler.class);
        bind(Logger.class)
                .annotatedWith(Names.named("S3GameStatusDaoLogger"))
                .toInstance(LogManager.getLogger(S3GameStatusDao.class));
        bind(Logger.class)
                .annotatedWith(Names.named("MonopolyDieHandlerLogger"))
                .toInstance(LogManager.getLogger(MonopolyDieHandler.class));
        bind(Logger.class)
                .annotatedWith(Names.named("MonopolyEndTurnHandlerLogger"))
                .toInstance(LogManager.getLogger(MonopolyEndTurnHandler.class));
        bind(Logger.class)
                .annotatedWith(Names.named("MonopolyAlexaTurnHandlerLogger"))
                .toInstance(LogManager.getLogger(MonopolyAlexaPlayerGameStatus.class));
        bind(Logger.class)
                .annotatedWith(Names.named("MonopolyHotelPurchaseHandlerLogger"))
                .toInstance(LogManager.getLogger(MonopolyHotelPurchaseHandler.class));
        bind(Logger.class)
                .annotatedWith(Names.named("MonopolyHousePurchaseHandlerLogger"))
                .toInstance(LogManager.getLogger(MonopolyHousePurchaseHandler.class));
        bind(Logger.class)
                .annotatedWith(Names.named("MonopolyPropertyPurchaseHandlerLogger"))
                .toInstance(LogManager.getLogger(MonopolyPropertyPurchaseHandler.class));
    }
}
