package jyothi;


import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static jdk.internal.org.jline.utils.AttributedStyle.*;
import static org.opencv.core.Core.inRange;

public class Color {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture video = new VideoCapture(0);
        Mat f = new Mat();
        while (true){
            video.read(f);


            showResult(Detect(f));
        }
    }
    public static Mat Detect(Mat imgInput){
        Mat img = new Mat();
        Imgproc.cvtColor(imgInput, img, Imgproc.COLOR_BGR2HSV);
        final Scalar BLACK = new Scalar(0, 0, 0) ;

        Rect sec1 = new Rect(100, 100, 100, 100);
        Mat sec1MAT = new Mat(img, sec1);
        
        Scalar sec1Scalar = Core.mean(sec1MAT);
        Point sec1top = new Point(
                100,
                100
        );
        Point sec1bot = new Point(
                200,
                200
        );
        Imgproc.rectangle(
          img,
          sec1top,
          sec1bot,
          BLACK,
          2
        );
      System.out.println(sec1Scalar.val[0] + " " + sec1Scalar.val[1] + " " + sec1Scalar.val[2]);
      return img;


        
        }


    }
    public static void showResult(Mat img) {
            HighGui.imshow("pic", img);
            HighGui.waitKey(1);

    }
}
