package org.firstinspires.ftc.teamcode.util;

import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

public class RectUtil {

    public static void clip(Rect rect, Size bounds) {
        int width = (int) bounds.width;
        int height = (int) bounds.height;

        Point tl = rect.tl();
        Point br = rect.br();

        if (tl.x < 0) tl.x = 0;
        if (br.x > width) br.x = width;
        if (tl.y < 0) tl.y = 0;
        if (br.y > height) br.y = height;

        rect.x = (int) tl.x;
        rect.width = (int) (br.x - tl.x);
        rect.y = (int) tl.y;
        rect.height = (int) (br.y - tl.y);
    }

}
