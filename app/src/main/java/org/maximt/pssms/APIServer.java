package org.maximt.pssms;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class APIServer implements Runnable {

    private static final String TAG = "APIServer";


    private final int mPort;
    private boolean mIsRunning;
    private ServerSocket mServerSocket;
    private Map<String, APICommand> mCommandList;

    public APIServer(int port) {
        mPort = port;
    }

    public void AddCommand(String route, APICommand command) {
        if (mCommandList == null) {
            mCommandList = new HashMap<String, APICommand>();
        }

        mCommandList.put(route, command);
    }

    public void start() {
        mIsRunning = true;
        new Thread(this).start();
    }

    public void stop() {
        try {
            mIsRunning = false;
            if (null != mServerSocket) {
                mServerSocket.close();
                mServerSocket = null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error closing the server socket.", e);
        }
    }

    @Override
    public void run() {
        try {
            mServerSocket = new ServerSocket(mPort);
            while (mIsRunning) {
                Socket socket = mServerSocket.accept();
                handle(socket);
                socket.close();
            }
        } catch (SocketException e) {
            Log.e(TAG, "Web server error.", e);
        } catch (IOException e) {
            Log.e(TAG, "Web server error.", e);
        }
    }

    private void handle(Socket socket) throws IOException {
        BufferedReader reader = null;
        PrintStream output = null;
        try {
            String sUri = null;

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while (!TextUtils.isEmpty(line = reader.readLine())) {
                if (line.startsWith("GET /")) {
                    int start = line.indexOf('/') + 1;
                    int end = line.indexOf(' ', start);
                    sUri = line.substring(start, end);
                    break;
                }
            }

            output = new PrintStream(socket.getOutputStream());

            if (null == sUri) {
                writeServerError(output);
                return;
            }

            Uri uri = Uri.parse(sUri);
            String result = processCommand(uri);
            if (result.isEmpty()) {
                writeServerError(output);
                return;
            }

            writeData(output, result.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            Log.e(TAG, "Web server error (Handle).", e);
        } finally {
            if (null != output) {
                output.close();
            }
            if (null != reader) {
                reader.close();
            }
        }
    }

    private void writeData(PrintStream output, byte[] bytes) throws IOException {
        output.println("HTTP/1.0 200 OK");
        output.println("Content-Type: application/json");
        output.println("Content-Length: " + bytes.length);
        output.println();
        output.write(bytes);
        output.flush();
    }

    private void writeServerError(PrintStream output) {
        output.println("HTTP/1.0 500 Internal Server Error");
        output.flush();
    }

    private String processCommand(Uri uri) throws Exception {
        String route = uri.getPath();

        String output;

        if (mCommandList == null || !mCommandList.containsKey(route)) {
            return "{'status': 'ERROR', 'message': 'unknown command'}";
        }

        APICommand cmd = mCommandList.get(route);

        cmd.clearParams();
        for (String k : uri.getQueryParameterNames()) {
            cmd.addParam(k, uri.getQueryParameter(k));
        }

        cmd.run();
        return cmd.getResult();
    }

}
