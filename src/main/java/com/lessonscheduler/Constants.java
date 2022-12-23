package com.lessonscheduler;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class Constants {
    public static final long JWT_EXPIRATION = 3600000;
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
}
