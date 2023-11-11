package org.firstinspires.ftc.teamcode.util;

import static android.os.Environment.getExternalStorageDirectory;
import static org.firstinspires.ftc.teamcode.util.TelemetryHolder.telemetry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static File externalStorageFile(String filename) {
        return new File(getExternalStorageDirectory(), filename);
    }

    public static List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();

        File file = externalStorageFile(filename);
        if (file.exists()) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line;
                while ((line = in.readLine()) != null) {
                    lines.add(line);
                }

            } catch (Exception e) {
                if (telemetry != null) {
                    telemetry.log().add("Error: could not load from file: " + ErrorUtil.convertToString(e));
                }
            }
        }

        return lines;
    }

    public static void writeLines(String filename, Object... objects) {
        File file = externalStorageFile(filename);
        try (PrintWriter out = new PrintWriter(new FileOutputStream(file))) {
            for (Object object : objects) {
                out.println(object);
            }

        } catch (Exception e) {
            if (telemetry != null) {
                telemetry.log().add("Error: could not write to file: " + ErrorUtil.convertToString(e));
            }
        }
    }

    public static void removeFile(String filename) {
        File file = externalStorageFile(filename);
        if (!file.delete()) {
            telemetry.log().add("Error: could not remove file: " + file);
        }
    }

}
