package com.amazon.sbidoo.game.status;

import com.amazon.sbidoo.exception.NoGameExistsException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class S3GameStatusDao implements GameStatusDao {

    public static final String BUCKET_NAME = "alexa-monopoly";

    private final AmazonS3 amazonS3;
    private final Logger logger;
    private final Gson gson;
    private final Comparator<S3ObjectSummary> gameStatusNameComparator = (o1, o2) -> {
        final String version1 = o1.getKey().split("-")[1];
        final String version2 = o2.getKey().split("-")[1];
        return Integer.compare(Integer.parseInt(version1), Integer.parseInt(version2));
    };

    @Inject
    public S3GameStatusDao(final AmazonS3 amazonS3,
                           @Named("S3GameStatusDaoLogger") final Logger logger,
                           final Gson gson) {
        this.amazonS3 = amazonS3;
        this.logger = logger;
        this.gson = gson;
    }

    @Override
    public GameStatus getGameStatusForUserId(final String userId) {
        this.amazonS3.listBuckets().forEach(logger::info);
        return getListOfGameStatusForUser(userId);
    }

    @Override
    public void updateGameStatusForUserId(final GameStatus gameStatus,
                                          final String userId) {
        final String serializedGameStatus = gson.toJson(gameStatus);
        logger.info("writing object: " + serializedGameStatus);
        amazonS3.putObject(BUCKET_NAME,
                String.join("-", userId, String.valueOf(gameStatus.getVersion())),
                serializedGameStatus);
    }

    @Override
    public void deleteAllGameStatusForUserId(String userId) {
        final List<S3ObjectSummary> s3ObjectSummaries = getS3ObjectSummaries(userId);
        s3ObjectSummaries.forEach(s3ObjectSummary -> this.amazonS3.deleteObject(BUCKET_NAME, s3ObjectSummary.getKey()));
    }

    private GameStatus getListOfGameStatusForUser(String userId) {
        logger.info("looking in S3 for object");
        List<S3ObjectSummary> summaries = getS3ObjectSummaries(userId);
        final Optional<S3ObjectSummary> currentVersion = summaries.stream().max(gameStatusNameComparator);
        if (currentVersion.isPresent()) {
            final S3ObjectSummary s3ObjectSummary = currentVersion.get();
            logger.info("found object: " + s3ObjectSummary);
            return getGameStatusFromS3Object(s3ObjectSummary);
        } else {
            throw new NoGameExistsException("There is no latest game for user");
        }
    }

    private List<S3ObjectSummary> getS3ObjectSummaries(String userId) {
        ObjectListing listing = amazonS3.listObjects(BUCKET_NAME, userId );
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();
        while (listing.isTruncated()) {
            listing = amazonS3.listNextBatchOfObjects (listing);
            summaries.addAll (listing.getObjectSummaries());
        }
        logger.info("found objects: ");
        summaries.forEach(logger::info);
        return summaries;
    }

    private GameStatus getGameStatusFromS3Object(final S3ObjectSummary s3ObjectSummary) {
        final S3Object object = this.amazonS3.getObject(s3ObjectSummary.getBucketName(), s3ObjectSummary.getKey());
        final InputStreamReader streamReader = new InputStreamReader(object.getObjectContent(), StandardCharsets.UTF_8);
        final BufferedReader reader = new BufferedReader(streamReader);
        final String gameStatusString = reader.lines().collect(Collectors.joining());
        return gson.fromJson(gameStatusString, GameStatus.class);
    }
}
