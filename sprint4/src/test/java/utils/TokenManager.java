package utils;

public class TokenManager {
    private static String accessToken;

    public static String getAccessToken() {
        if (accessToken == null) {
            throw new IllegalStateException("Access token is not set. Please run the authentication test first.");
        }
        return accessToken;
    }

    public static void setAccessToken(String token) {
        accessToken = token;
    }
}