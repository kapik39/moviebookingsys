package vn.jpringboot.cinemaBooking.security;

import java.time.Instant;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JwtBlacklistService {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    private final ConcurrentHashMap<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public JwtBlacklistService() {
        scheduler.scheduleAtFixedRate(this::cleanupBlacklistToken, 0, 5, TimeUnit.MINUTES);
    }

    public void blacklistToken(String token, Long expirationTimeMs) {
        blacklistedTokens.put(token, expirationTimeMs);
        logger.info("Token has been added to blacklist, expires at: {}", Instant.ofEpochMilli(expirationTimeMs));
    }

    public boolean isTokenBlacklisted(String token) {
        Long expiration = blacklistedTokens.get(token);
        if (expiration == null) {
            return false;
        }
        return expiration > System.currentTimeMillis();
    }

    public void cleanupBlacklistToken() {
        Long currentTime = System.currentTimeMillis();
        int removedCount = 0;
        for (Entry<String, Long> entry : blacklistedTokens.entrySet()) {
            if (entry.getValue() <= currentTime) {
                blacklistedTokens.remove(entry.getKey());
                removedCount++;
            }
        }
        if (removedCount > 0) {
            logger.info("Removed {} expired token from blacklist", removedCount);
        }
    }
}
