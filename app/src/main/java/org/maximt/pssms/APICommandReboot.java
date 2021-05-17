package org.maximt.pssms;

import android.content.Context;

/**
 * Created by maxim on 29.09.17.
 */

public class APICommandReboot extends APICommand {
    private static final String TAG = "APICommandReboot";

    public APICommandReboot(Context context) {
        super(context);
    }

    @Override
    public void run() {
        Root root = new Root();
        if (root.IsRooted()) {
            root.RunCommand("reboot");
        } else {
            setResult("ERROR", "NoRootAccessDenied");
        }

    }

}
