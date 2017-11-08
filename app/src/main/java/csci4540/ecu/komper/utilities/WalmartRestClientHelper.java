package csci4540.ecu.komper.utilities;

/**
 * Created by ryansummerlin on 11/5/17.
 */

import com.loopj.android.http.*;

import csci4540.ecu.komper.R;

public class WalmartRestClientHelper {
    public static final String BASE_URL = "http://api.walmartlabs.com/v1/search?apikey="
                                            + R.string.WalMartAPIKey;

    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void GET(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void POST(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
