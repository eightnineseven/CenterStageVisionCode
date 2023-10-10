
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
import java.util.Timer;
import java.util.TimerTask;

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
    public enum ParkingPosition {
        LEFT,
        CENTER,
        RIGHT
    }
    public static String position;
    public static Mat Detect(Mat img1) {


       Mat img = new Mat();
        Imgproc.cvtColor(img1, img, Imgproc.COLOR_BGR2HSV);
        int viewPortX = 640;
        int viewPortY = 720;


        int sec1X = 100;
        int sec1Y = 180;
        int sec1Width = 100;
        int sec1Height = 90;

        int sec2X = 300;
        int sec2Y = 180;
        int sec2Width = 100;
        int sec2Height = 90;

        int sec3X = 500;
        int sec3Y = 180;
        int sec3Width = 100;
        int sec3Height = 90;




        Rect sec1 = new Rect(sec1X, sec1Y, sec1Width, sec1Height);
        Rect sec2 = new Rect(sec2X , sec2Y, sec2Width, sec2Height);
        Rect sec3 = new Rect(sec3X, sec3Y, sec3Width, sec3Height);

        Mat sec1MAT = new Mat(img, sec1);
        Mat sec2MAT = new Mat(img, sec2);
        Mat sec3MAT = new Mat(img, sec3);


        Scalar sec1Scalar = Core.mean(sec1MAT);
        Scalar sec2Scalar = Core.mean(sec2MAT);
        Scalar sec3Scalar = Core.mean(sec3MAT);


        Scalar ColorToMatch = new Scalar(0, 183, 219);
        if(sec1Scalar.val[0] > 180){
            sec1Scalar.val[0] =360 - sec1Scalar.val[0];
        }
        if(sec2Scalar.val[0] > 180){
            sec2Scalar.val[0] = 360 - sec2Scalar.val[0];
        }
        if(sec3Scalar.val[0] > 180){
            sec3Scalar.val[0] = 360 - sec3Scalar.val[0];
        }
        double Sec1AvgDist = 2 *Math.abs(ColorToMatch.val[0] -sec1Scalar.val[0]) + Math.abs(ColorToMatch.val[1] - sec1Scalar.val[1]) + Math.abs(ColorToMatch.val[2] - sec1Scalar.val[2]);
        double Sec2AvgDist = (2 * Math.abs(ColorToMatch.val[0] - sec2Scalar.val[0]) + Math.abs(ColorToMatch.val[1] - sec2Scalar.val[1]) + Math.abs(ColorToMatch.val[2] - sec2Scalar.val[2]));
        double Sec3AvgDist = (2 * Math.abs(ColorToMatch.val[0] - sec3Scalar.val[0]) + Math.abs(ColorToMatch.val[1] - sec3Scalar.val[1]) + Math.abs(ColorToMatch.val[2] - sec3Scalar.val[2]));




            Point sec1top = new Point(
                    sec1X,
                    sec1Y
            );
            Point sec1bot = new Point(
                    sec1X + sec1Width,
                    sec1Y + sec1Height
            );
            Point sec2top = new Point(
                    sec2X,
                    sec2Y
            );
            Point sec2bot = new Point(
                    sec2X + sec2Width,
                    sec2Y + sec2Height
            );
            Point sec3top = new Point(
                    sec3X,
                    sec3Y
            );
            Point sec3bot = new Point(
                    sec3X + sec3Width,
                    sec3Y + sec3Height
            );


            if (Sec1AvgDist < Sec2AvgDist && Sec1AvgDist < Sec3AvgDist) {

                position = String.valueOf(ParkingPosition.LEFT);
                Imgproc.rectangle(
                        img,
                        sec1top,
                        sec1bot,
                        new Scalar(sec1Scalar.val[0], sec1Scalar.val[1],
                                sec1Scalar.val[2]),
                        10

                );
                Imgproc.rectangle(
                        img,
                        sec2top,
                        sec2bot,
                        new Scalar(sec2Scalar.val[0], sec2Scalar.val[1],
                                sec2Scalar.val[2]),
                        2

                );
                Imgproc.rectangle(
                        img,
                        sec3top,
                        sec3bot,
                        new Scalar(sec3Scalar.val[0], sec3Scalar.val[1],
                                sec3Scalar.val[2]),
                        2

                );

            } else if (Sec2AvgDist < Sec3AvgDist) {
                position = String.valueOf(ParkingPosition.CENTER);

                Imgproc.rectangle(
                        img,
                        sec2top,
                        sec2bot,
                        new Scalar(sec2Scalar.val[0], sec2Scalar.val[1],
                                sec2Scalar.val[2]),
                        10

                );
                Imgproc.rectangle(
                        img,
                        sec3top,
                        sec3bot,
                        new Scalar(sec3Scalar.val[0], sec3Scalar.val[1],
                                sec3Scalar.val[2]),
                        2

                );
                Imgproc.rectangle(
                        img,
                        sec1top,
                        sec1bot,
                        new Scalar(sec1Scalar.val[0], sec1Scalar.val[1],
                                sec1Scalar.val[2]),
                        2

                );

            } else {
                position = String.valueOf(ParkingPosition.RIGHT);

                Imgproc.rectangle(
                        img,
                        sec3top,
                        sec3bot,
                        new Scalar(sec3Scalar.val[0],sec3Scalar.val[1],sec3Scalar.val[2]),
                        10

                );
                Imgproc.rectangle(
                        img,
                        sec1top,
                        sec1bot,
                        new Scalar(sec1Scalar.val[0],sec1Scalar.val[1],sec1Scalar.val[2]),
                        2

                );
                Imgproc.rectangle(
                        img,
                        sec2top,
                        sec2bot,
                        new Scalar(sec2Scalar.val[0],sec2Scalar.val[1],sec2Scalar.val[2]),
                        2

                );
            }

            // Release and return input

//                System.out.println("Sec 1 Hue: " + Math.round(sec1Scalar.val[0]) + " "
//                        + "Sec 1 Saturation: " + Math.round(sec1Scalar.val[1]) + " "
//                        + "Sec 1 Value: " + Math.round(sec1Scalar.val[2]));
        System.out.println((int)Sec1AvgDist + " " + (int)Sec2AvgDist + " " + (int)Sec3AvgDist);
            Mat Showimg = new Mat();
        Imgproc.cvtColor(img, Showimg, Imgproc.COLOR_HSV2BGR);
            return Showimg;


        }

        public static void showResult (Mat img){
            HighGui.imshow("pic", img);
            HighGui.waitKey(1);


//        Imgproc.resize(img, img, new Size(640, 480));
//        MatOfByte m = new MatOfByte();
//        Imgcodecs.imencode(".jpg", img, m);
//        byte[] byteArray = m.toArray();
//        BufferedImage bufImage = null;
//
//
//        try {
//
//
//
//            InputStream in = new ByteArrayInputStream(byteArray);
//            bufImage = ImageIO.read(in);
//            JFrame frame = new JFrame();
//
//
//
//
//            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
//            frame.pack();
//            frame.setVisible(true);
//
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        }
    }
