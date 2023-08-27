
package jyothi;


import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static jdk.internal.org.jline.utils.AttributedStyle.CYAN;
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
    public static Mat Detect(Mat img){


        Rect sec1 = new Rect(100, 100, 100, 100);
        Rect sec2 = new Rect(200, 200, 100, 100);
        Rect sec3 = new Rect(300, 300, 100, 100);

        Mat sec1MAT = new Mat(img, sec1);
        Mat sec2MAT = new Mat(img, sec2);
        Mat sec3MAT = new Mat(img, sec3);

        Core.inRange(sec1MAT, new Scalar(150, 0, 0), new Scalar(255, 255, 255), sec1MAT);
        Core.inRange(sec2MAT, new Scalar(150, 0, 0), new Scalar(255, 255, 255), sec2MAT);
        Core.inRange(sec3MAT, new Scalar(150, 0, 0), new Scalar(255, 255, 255), sec3MAT);
        Core.inRange(img, new Scalar(150, 0, 0), new Scalar(255,255,255), img);

        Scalar sec1count = Core.sumElems(sec1MAT);
        Scalar sec2count = Core.sumElems(sec2MAT);
        Scalar sec3count = Core.sumElems(sec3MAT);

        Point sec1top = new Point(
                0,
                0
        );
        Point sec1bot = new Point(
                50,
                50
        );
        Point sec2top = new Point(
                100,
                100
        );
        Point sec2bot = new Point(
                150,
                150
        );
        Point sec3top = new Point(
                200,
                200
        );
        Point sec3bot = new Point(
                250,
                250
        );

        if(sec1count.val[0] > sec2count.val[0] && sec1count.val[0]>sec3count.val[0]){
            System.out.println("1");
            Imgproc.rectangle(
                    img,
                    sec1top,
                    sec1bot,
                    Scalar.all(CYAN),
                    2

            );
            return img;
        } else if(sec3count.val[0]>sec2count.val[0]){
            System.out.println("2");
            Imgproc.rectangle(
                    img,
                    sec2top,
                    sec2bot,
                    Scalar.all(CYAN),
                    2

            );
            return img;
        } else {
            System.out.println("3");
            Imgproc.rectangle(
                    img,
                    sec3top,
                    sec3bot,
                    Scalar.all(CYAN),
                    2

            );
            return img;
        }


    }
    public static void showResult(Mat img) {


        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte m = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, m);
        byte[] byteArray = m.toArray();
        BufferedImage bufImage = null;


        try {



            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();




            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}