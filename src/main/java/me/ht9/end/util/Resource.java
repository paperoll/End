package me.ht9.end.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public final class Resource
{
    private final static ArrayList<String> PROXY_URLs = new ArrayList<>(Arrays.asList(
            "http://resourceproxy.pymcl.net/MinecraftResources/",
            "http://mcresources.modification-station.net/MinecraftResources/",
            "https://betacraft.pl/MinecraftResources/"
    ));

    private static final Logger logger = LogManager.getLogger(Resource.class);

    private Resource() {}

    private static boolean isDown(String url) {
        try {
            URL urlObject = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlObject.openConnection();
            httpURLConnection.connect();

            int statusCode = httpURLConnection.getResponseCode();
            if (!(statusCode == 404)) {
                return false;
            }
        } catch (IOException ignored) {

        }

        return true;
    }

    public static String getResources() {
        for (String url : PROXY_URLs) {
            if (!isDown(url)) {
                logger.info("Resources proxy found: " + url);
                return url;
            }
        }

        return null;
    }
}