package de.uniwue.jpp.mensabot.retrieval;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.errorhandling.OptionalWithMessageVal;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.mensabot.retrieval.htmlparsing.HttpFetcher;
import de.uniwue.jpp.mensabot.util.MensabotUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.*;

public interface Fetcher {

    OptionalWithMessage<String> fetchCurrentData();

    static Fetcher createDummyCsvFetcher() {
        return new FetcherClass();
    }

    /* Falls Sie Aufgabenteil 2 nicht bearbeiten, kann diese Methode ignoriert werden */
    static Fetcher createHttpFetcher(HttpClient client, String url) {
        //handle exceptions
        if(client == null || url == null)
            throw new IllegalArgumentException("An argument was null!");

        URL myurl;
        try{
            myurl = new URL(url);
        }catch (MalformedURLException x){
            throw new IllegalArgumentException("Invalid url!");
        }
        return new HttpFetcher(client,url);
    }
}

