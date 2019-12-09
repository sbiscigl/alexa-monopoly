package com.amazon.sbidoo.game.status;

import com.amazonaws.services.s3.AmazonS3;
import com.google.inject.Inject;

public class S3GameStatusDao implements GameStatusDao {

    private final AmazonS3 amazonS3;

    @Inject
    public S3GameStatusDao(final AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public GameStatus getGameStatusForUserId(final String userId) {
        this.amazonS3.listBuckets().forEach(System.out::println);
        return null;
    }

    @Override
    public void updateGameStatusForUserId(final GameStatus gameStatus,
                                          final String userId) {
    }
}
