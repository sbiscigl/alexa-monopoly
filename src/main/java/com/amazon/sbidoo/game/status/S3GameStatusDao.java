package com.amazon.sbidoo.game.status;

import com.amazonaws.services.s3.AmazonS3;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;

public class S3GameStatusDao implements GameStatusDao {

    private final AmazonS3 amazonS3;
    private final Logger logger;

    @Inject
    public S3GameStatusDao(final AmazonS3 amazonS3,
                           @Named("S3GameStatusDaoLogger") final Logger logger) {
        this.amazonS3 = amazonS3;
        this.logger = logger;
    }

    @Override
    public GameStatus getGameStatusForUserId(final String userId) {
        this.amazonS3.listBuckets().forEach(bucket -> logger.info(bucket.getName()));
        return null;
    }

    @Override
    public void updateGameStatusForUserId(final GameStatus gameStatus,
                                          final String userId) {
    }
}
