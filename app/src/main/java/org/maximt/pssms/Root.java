package org.maximt.pssms;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Root {
    Process m_SU;

    public Root() {
        initSU();
    }

    boolean initSU() {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.redirectErrorStream(true);
            pb.command("su");

            m_SU = pb.start();
            if (m_SU != null) {
                return true;
            }
        } catch (Exception e) {
            Log.e("ROOT", "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        return false;
    }

    boolean IsRooted() {
        return (m_SU != null);
    }

    boolean RunCommand(String cmd) {
        try {
            if (m_SU != null) {
                writeIn(cmd);
                String s = readOut();

                if (s != null) {
                    Log.d("ROOT", s);
                }

                return true;
            }
        } catch (Exception e) {
            Log.e("ROOT", "Can run '" + cmd + "' [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        return false;
    }

    String GetCommand(String cmd) {
        try {
            if (m_SU != null) {
                writeIn(cmd);
                String s = readOut();

                if (s != null) {
                    Log.d("ROOT", s);
                }

                return s;
            }
        } catch (Exception e) {
            Log.e("ROOT", "Can run '" + cmd + "' [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        return null;
    }

    private void writeIn(String str) throws IOException {
        DataOutputStream stream = new DataOutputStream(m_SU.getOutputStream());
        stream.writeBytes(str + "\n");
        stream.flush();
    }

    private String readOut() throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader stream = new BufferedReader(new InputStreamReader(m_SU.getInputStream()));
        String line;

        try {


            while (true) {
                Thread.sleep(1000);

                if (!stream.ready())
                    break;

                line = stream.readLine();
                if (line == null)
                    break;

                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            Log.e("ROOT", "Can read stream [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        return sb.toString();
    }
}
