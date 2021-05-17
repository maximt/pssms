package org.maximt.pssms;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by maxim on 28.09.17.
 */

public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    private static final int NOTIFICATION_ID = 1;

    Config m_Cfg;

    final int port = 9999;
    private APIServer mAPIServer;

    @Override
    public void onCreate() {
        super.onCreate();

        startForeground(NOTIFICATION_ID, getStatusIcon());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started.");

        loadPrefs();
        runWebServer();
        setReceivers(true);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mAPIServer != null)
            mAPIServer.stop();

        setReceivers(false);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void runWebServer() {
        if (mAPIServer != null) {
            mAPIServer.stop();
            mAPIServer = null;
        }

        mAPIServer = new APIServer(port);

        mAPIServer.AddCommand("send", new APICommandSendSMS(this.getApplicationContext()));
        mAPIServer.AddCommand("reboot", new APICommandReboot(this.getApplicationContext()));
        mAPIServer.AddCommand("set_callback", new APICommandSetCallbackURL(this.getApplicationContext()));

        mAPIServer.start();
    }

    private void loadPrefs() {
        m_Cfg = new Config(this);
    }

    private void setReceivers(boolean enabled) {
        int flag = enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        ComponentName cSMSReceiver = new ComponentName(this, SMSReceiver.class);
        ComponentName cSMSStatusReceiver = new ComponentName(this, SMSStatusReceiver.class);

        getPackageManager().setComponentEnabledSetting(cSMSReceiver, flag, PackageManager.DONT_KILL_APP);
        getPackageManager().setComponentEnabledSetting(cSMSStatusReceiver, flag, PackageManager.DONT_KILL_APP);

    }

    private Notification getStatusIcon() {
        String appName = getApplicationInfo().loadLabel(getPackageManager()).toString();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(appName).setContentText("Started");

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        return notification;
    }

}
