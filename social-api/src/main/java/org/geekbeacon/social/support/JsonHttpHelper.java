package org.geekbeacon.social.support;

import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;

/**
 * Collect of utilities that bring additional functionality beyond the standard library
 * <p>
 * ie. wrapping in try/catch and ensuring a default and other misc tooling
 */
public final class JsonHttpHelper {

    public static <T> T safeExtract(Object doc, String path, T defaultValue) {
        try {
            return JsonPath.read(doc, path);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * validates that request executed successfully
     */
    public static boolean validateResponse(HttpResponse response, boolean useException) throws HttpResponseException {
        if (response.getStatusLine().getStatusCode() != 200) {
            if (useException) {
                throw new HttpResponseException(response.getStatusLine().getStatusCode(), "Invalid Status Code");
            } else {
                return false;
            }
        }
        return true;

    }

}
