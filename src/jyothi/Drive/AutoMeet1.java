// Scan the 3 regions for color cube: Color code finished, just need to determine the color we're looking for
// Drop first pixel on correct tape: Lift movement + encoder movement - Encoder movement basis created, lift is an unknown
// Move to board: Drive to april tag dependent on original color code - repurpose the example code
// drop pixel: lift arm to needed height and release - lift movement is unknown
// retract lift: lift movement - lift movement is unknown
// park: encoder movement - encoder movement basis created
//total points possible: 45
// issues: dropping pixel on board may not be feasible for first comp, rest should be possible brings total points to 25 in auto




//encoder movement basis:
public static void EncoderFB(double LF, double LB, double RF, double RB){
      RightFEC+=RF * COUNTS_PER_TILE;
      RightBEC+=RB * COUNTS_PER_TILE;
      LeftFEC+=LF * COUNTS_PER_TILE;
      LeftBEC+=LB * COUNTS_PER_TILES;

      RightBack.setTargetPosition(RightBEC);
      RightFront.setTargetPosition(RightFEC);
      LeftBack.setTargetPosition(LeftBEC);
      LeftFront.setTargetPosition(LeftFEC);

      RightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      LeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

      LeftFront.setPower(0.3);
      LeftBack.setPower(0.3);
      RightFront.setPower(0.3);
      RightBack.setPower(0.3);

      while(RightBack.isBusy() && RightFront.isBusy() && LeftBack.isBusy() && LeftFront.isBusy()){
          telemetry.addData("Currently running or something like that yk");
          telemetry.update();

      }

      LeftFront.setPower(0);
      LeftBack.setPower(0);
      RightFront.setPower(0);
      RightBack.setPower(0);

      LeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      LeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      RightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      RightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      

}
public static void EncoderTurn(double degrees){
  if(degrees > 0){
    //turn left
    RightFEC+=RF * COUNTS_PER_ROT;
      RightBEC+=RB * COUNTS_PER_ROT;
      LeftFEC-=LF * COUNTS_PER_ROT;
      LeftBEC-=LB * COUNTS_PER_ROT;

     
  } else {
    RightFEC-=RF * COUNTS_PER_ROT;
      RightBEC-=RB * COUNTS_PER_ROT;
      LeftFEC+=LF * COUNTS_PER_ROT;
      LeftBEC+=LB * COUNTS_PER_ROT;

      
  }
 RightBack.setTargetPosition(RightBEC);
      RightFront.setTargetPosition(RightFEC);
      LeftBack.setTargetPosition(LeftBEC);
      LeftFront.setTargetPosition(LeftFEC);

      RightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      LeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      RightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      LeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

      LeftFront.setPower(0.3);
      LeftBack.setPower(0.3);
      RightFront.setPower(0.3);
      RightBack.setPower(0.3);

      while(RightBack.isBusy() && RightFront.isBusy() && LeftBack.isBusy() && LeftFront.isBusy()){
          telemetry.addData("Currently running or something like that yk");
          telemetry.update();

      }

      LeftFront.setPower(0);
      LeftBack.setPower(0);
      RightFront.setPower(0);
      RightBack.setPower(0);

      LeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      LeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      RightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      RightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      
}
// color code scan basis: 
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
