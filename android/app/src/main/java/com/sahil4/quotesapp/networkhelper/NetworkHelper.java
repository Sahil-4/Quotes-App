package com.sahil4.quotesapp.networkhelper;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sahil4.quotesapp.models.MyPreference;
import com.sahil4.quotesapp.models.Quote;
import com.sahil4.quotesapp.roomdb.QuoteDAO;
import com.sahil4.quotesapp.roomdb.QuoteDatabase;
import com.sahil4.quotesapp.viewmodels.PreferencesViewModel;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NetworkHelper {
    private static CronetEngine cronetEngine = null;
    private final Executor executor;
    private static UrlRequest request;
    public final QuoteDAO quoteDAO;
    String _id, content, author;
    PreferencesViewModel preferencesViewModel;
    final List<MyPreference> allPreferences;

    public NetworkHelper(Application application) {
        // Initialize Cronet
        CronetEngine.Builder builder = new CronetEngine.Builder(application.getApplicationContext());
        builder.enableHttpCache(CronetEngine.Builder.HTTP_CACHE_IN_MEMORY, 5 * 1024);
        cronetEngine = builder.build();
        executor = Executors.newSingleThreadExecutor();

        // initialise DAO
        quoteDAO = QuoteDatabase.getInstance(application.getApplicationContext()).quoteDAO();

        // get preferences to get quote
        preferencesViewModel = new PreferencesViewModel(application);
        allPreferences = preferencesViewModel.getPreferences();
    }

    public void makeRequest(String url) {
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(url, new UrlRequest.Callback() {
            @Override
            public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
                request.cancel();
            }

            @Override
            public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
                if (info.getHttpStatusCode() == 200) {
                    request.read(ByteBuffer.allocateDirect(32 * 1024));
                } else {
                    request.cancel();
                }
            }

            @Override
            public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
                try {
                    // read response here
                    byteBuffer.flip();
                    String resp = StandardCharsets.UTF_8.decode(byteBuffer).toString();
                    Gson gson = new Gson();
                    JsonObject jsonresp = gson.fromJson(resp, JsonObject.class);

                    // extracting data
                    _id = jsonresp.get("_id").getAsString();
                    content = jsonresp.get("content").getAsString();
                    author = jsonresp.get("author").getAsString();

                    // save in room db
                    Quote quote = new Quote(author, content);
                    QuoteDatabase.databaseWriteExecutor.execute(() -> quoteDAO.insertQuote(quote));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    byteBuffer.clear();
                    request.read(byteBuffer);
                }
            }

            @Override
            public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            }

            @Override
            public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
                error.printStackTrace();
            }
        }, executor);

        requestBuilder.addHeader("Content-Type", "application/json; charset=UTF-8");
        requestBuilder.setHttpMethod("GET");
        request = requestBuilder.build();

        // starting the request
        request.start();
    }

    public void getNewQuote() {
        int pref_count = 0;
        String base_url = "https://api.quotable.io";
        String path_random = "/random"; // for any one random quote

        // for favourite or preferred quote
        StringBuilder path_preference = new StringBuilder("?tags=");
        for (MyPreference preference : allPreferences) {
            if (preference.getChecked()) {
                path_preference.append(preference.getTitle());
                path_preference.append("|");
                pref_count++;
            }
        }

        String URL = base_url + path_random;
        if (pref_count != 0) {
            path_preference.deleteCharAt(path_preference.length());
            URL = base_url + path_random + path_preference;
        }

        makeRequest(URL);
    }

    public static void shutdownCronetEngine() {
        // Shutdown Cronet when the app is closed
        if (cronetEngine != null) {
            request.cancel();
            cronetEngine.shutdown();
        }
    }
}
