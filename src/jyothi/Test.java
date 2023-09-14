
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

public class Test {
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
        final Scalar
                YELLOW  = new Scalar(0, 255, 255),
                BLACK = new Scalar(0, 0, 0) ;

        Rect sec1 = new Rect(100, 100, 100, 100);
        Rect sec2 = new Rect(300, 100, 100, 100);
        Rect sec3 = new Rect(500, 100, 100, 100);

        Mat sec1MAT = new Mat(img, sec1);
        Mat sec2MAT = new Mat(img, sec2);
        Mat sec3MAT = new Mat(img, sec3);



        //Core.inRange(sec1MAT, new Scalar(150 , 90, 40), new Scalar(210, 150, 90), sec1MAT);
        // Core.inRange(sec2MAT, new Scalar(150, 90, 40), new Scalar(210, 150, 90), sec2MAT);
        // Core.inRange(sec3MAT, new Scalar(150, 90, 40), new Scalar(210, 150, 90), sec3MAT);
        // Core.inRange(img, new Scalar(150, 90, 40), new Scalar(210,150,90), img);
        //  Scalar sec1count = Core.sumElems(sec1MAT);
        // Scalar sec2count = Core.sumElems(sec2MAT);
        //  Scalar sec3count = Core.sumElems(sec3MAT);
        Scalar sec1Scalar = Core.mean(sec1MAT);
        Scalar sec2Scalar = Core.mean(sec2MAT);
        Scalar sec3Scalar = Core.mean(sec3MAT);



        Scalar green = new Scalar(0,255,0);
        double Sec1AvgDist = Math.abs(100-sec1Scalar.val[0]) + (255-sec1Scalar.val[1]) + Math.abs(100-sec1Scalar.val[2]);
        double Sec2AvgDist = (Math.abs(100-sec2Scalar.val[0]) + (255-sec2Scalar.val[1]) + Math.abs(100-sec2Scalar.val[2]));
        double Sec3AvgDist = (Math.abs(100-sec3Scalar.val[0]) + (255-sec3Scalar.val[1]) + Math.abs(100-sec3Scalar.val[2]));









        Point sec1top = new Point(
                100,
                100
        );
        Point sec1bot = new Point(
                200,
                200
        );
        Point sec2top = new Point(
                300,
                100
        );
        Point sec2bot = new Point(
                400,
                200
        );
        Point sec3top = new Point(
                500,
                100
        );
        Point sec3bot = new Point(
                600,
                200
        );


        if(Sec1AvgDist < Sec2AvgDist && Sec1AvgDist<Sec3AvgDist){
            System.out.println("1");
            Imgproc.rectangle(
                    img,
                    sec1top,
                    sec1bot,
                    YELLOW,
                    2

            );
            Imgproc.rectangle(
                    img,
                    sec2top,
                    sec2bot,
                    BLACK,
                    2

            );
            Imgproc.rectangle(
                    img,
                    sec3top,
                    sec3bot,
                    BLACK,
                    2

            );
            return img;
        } else if(Sec2AvgDist<Sec3AvgDist){
            System.out.println("2");
            Imgproc.rectangle(
                    img,
                    sec2top,
                    sec2bot,
                    YELLOW,
                    2

            );
            Imgproc.rectangle(
                    img,
                    sec3top,
                    sec3bot,
                    BLACK,
                    2

            );
            Imgproc.rectangle(
                    img,
                    sec1top,
                    sec1bot,
                    BLACK,
                    2

            );
            return img;
        } else {
            System.out.println("3");
            Imgproc.rectangle(
                    img,
                    sec3top,
                    sec3bot,
                    YELLOW,
                    2

            );
            Imgproc.rectangle(
                    img,
                    sec1top,
                    sec1bot,
                    BLACK,
                    2

            );
            Imgproc.rectangle(
                    img,
                    sec2top,
                    sec2bot,
                    BLACK,
                    2

            );
            return img;
        }


    }
    public static void showResult(Mat img) {
            HighGui.imshow("pic", img);
            HighGui.waitKey(1);

    }
}
