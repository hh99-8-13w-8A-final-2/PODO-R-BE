package be.podor.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheKey {
    THEATER_SEAT(Key.THEATER_SEAT_KEY, 60 * 60 * 24),
    MUSICAL(Key.MUSICAL_KEY, 60 * 30),
    TAG(Key.TAG_KEY, 60 * 10);

    public static final int DEFAULT_EXP = 60;

    public class Key {
        public static final String THEATER_SEAT_KEY = "seats";
        public static final String MUSICAL_KEY = "musicals";
        public static final String TAG_KEY = "popularTags";
    }

    private final String key;
    private final int exp;
}
