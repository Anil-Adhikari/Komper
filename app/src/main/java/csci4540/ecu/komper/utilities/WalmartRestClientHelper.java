package csci4540.ecu.komper.utilities;

/**
 * Created by ryansummerlin on 11/5/17.
 */

import android.content.Context;

import com.loopj.android.http.*;

import csci4540.ecu.komper.R;

public class WalmartRestClientHelper {
    private static final String BASE_URL = "http://api.walmartlabs.com/v1/search?apikey=";

    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void GET(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String absolute = getAbsoluteUrl(context, url);
        client.get(absolute, params, responseHandler);
    }

    public static void POST(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(context, url), params, responseHandler);
    }

    private static String getAbsoluteUrl(Context context, String relativeUrl) {
        return BASE_URL + context.getString(R.string.WalMartAPIKey) + "&" + relativeUrl;
    }
}
