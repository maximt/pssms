package org.maximt.pssms;

import android.content.Context;

/**
 * Created by maxim on 02.10.17.
 */

public class APICallbackSMSSent extends APICallback {

    public APICallbackSMSSent(String url, Context context) {
        super(url, "sent", context);

    }


}
