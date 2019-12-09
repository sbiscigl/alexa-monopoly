package com.amazon.sbidoo.inject;

import com.amazon.sbidoo.game.GameHandler;
import com.amazon.sbidoo.game.MonopolyGameHandler;
import com.amazon.sbidoo.game.status.GameStatusDao;
import com.amazon.sbidoo.game.status.S3GameStatusDao;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MonopolyModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GameStatusDao.class).to(S3GameStatusDao.class);
        bind(GameHandler.class).to(MonopolyGameHandler.class);
        bind(AmazonS3.class).toInstance(AmazonS3Client.builder()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(Regions.US_EAST_1)
                .build());
        bind(Logger.class)
                .annotatedWith(Names.named("S3GameStatusDaoLogger"))
                .toInstance(LogManager.getLogger(S3GameStatusDao.class));
    }
}
