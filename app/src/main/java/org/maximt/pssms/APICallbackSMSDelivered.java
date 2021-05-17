package org.maximt.pssms;

import android.content.Context;

/**
 * Created by maxim on 02.10.17.
 */

public class APICallbackSMSDelivered extends APICallback {

    public APICallbackSMSDelivered(String url, Context context) {
        super(url, "delivered", context);
    }

}
