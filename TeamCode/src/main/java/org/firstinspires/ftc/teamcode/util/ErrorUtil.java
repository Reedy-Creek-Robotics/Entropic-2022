package org.firstinspires.ftc.teamcode.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorUtil {

    public static String convertToString(Throwable error) {
        StringWriter out = new StringWriter();
        error.printStackTrace(new PrintWriter(out));
        return out.toString();
    }

}
