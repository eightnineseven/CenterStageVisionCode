// Scan the 3 regions for color cube: Color code finished, just need to determine the color we're looking for
// Drop first pixel on correct tape: Lift movement + encoder movement - Encoder movement basis created, lift is an unknown
// Move to board: Drive to april tag dependent on original color code - repurpose the example code
// drop pixel: lift arm to needed height and release - lift movement is unknown
// retract lift: lift movement - lift movement is unknown
// park: encoder movement - encoder movement basis created
//total points possible: 45
// issues: dropping pixel on board may not be feasible for first comp, rest should be possible brings total points to 25 in auto


import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
public class AutoMeet1 extends LinearOPMode {
    // all variable decs except to april tag
    public static double RightFEC = 0;
    public static double LeftFEC = 0;
    public static double RightBEC = 0;
    public static double LeftBEC = 0;
    public static double LiftET = 0;

    public static int RandNum = 0;

    // Guess and Check this value
    static final int COUNTS_PER_TILE = 1;
    static final int COUNTS_PER_ROT = 1;
    static final int COUNTS_PER_LIFT_INCH = 1;

    private DcMotor LeftBack = null;
    private DcMotor LeftFront = null;
    private DcMotor RightBack = null;
    private DcMotor RightFront = null;
  //  private DcMotor Lift = null;


//to april tag decs

    final double DESIRED_DISTANCE = 4.0; //  this is how close the camera should get to the target (inches)

    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    final double SPEED_GAIN = 0.02;   //  Forward Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN = 0.015;   //  Strafe Speed Control "Gain".  eg: Ramp up to 25% power at a 25 degree Yaw error.   (0.25 / 25.0)
    final double TURN_GAIN = 0.01;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE = 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_TURN = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)


    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private static int DESIRED_TAG_ID = 0;     // Choose the tag you want to approach or set to -1 for ANY tag.
    private VisionPortal visionPortal;               // Used to manage the video source.
    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
    private AprilTagDetection desiredTag = null;


    //main method scaffold:
    @Override
    public void runOpMode() {


//to april tag code
        boolean targetFound = false;    // Set to true when an AprilTag target is detected
        double drive = 0;        // Desired forward power/speed (-1 to +1)
        double strafe = 0;        // Desired strafe power/speed (-1 to +1)
        double turn = 0;
        initAprilTag();
        if (USE_WEBCAM) setManualExposure(6, 250);


        LeftBack = hardwareMap.get(DcMotor.class, "LeftBack");
        RightBack = hardwareMap.get(DcMotor.class, "RightBack");
        LeftFront = hardwareMap.get(DcMotor.class, "LeftFront");
        RightFront = hardwareMap.get(DcMotor.class, "RightFront");
       // Lift = hardwareMap.get(DcMotor.class, "Lift");

        //reversals check
        LeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    //    Lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);










        //run method sequence
        Detect(img);
        if(DESIRED_TAG_ID == 1) {
            EncoderTurn(30);
            EncoderFB(1, 1, 1, 1);
         //   Lift(2);

            //drop
         //   Lift(-2);
            EncoderTurn(60);
        } else if(DESIRED_TAG_ID == 2){
            EncoderFB(1.5, 1.5, 1.5, 1.5);
          //  Lift(2);
            //drop
          //  Lift(-2);
            EncoderTurn(90);

        } else if(DESIRED_TAG_ID == 3){
            EncoderTurn(-30);
            EncoderFB(1, 1, 1,1);
          //  Lift(2);
            //drop
          //  Lift(-2);
            EncoderTurn(120);
        }
        EncoderFB(2, 2, 2, 2);
        findTag();
       // Lift(6);
        //drop
       // Lift(-6);
        EncoderTurn(90);
        EncoderFB(1,1,1,1);
        EncoderTurn(-90);
        EncoderFB(1,1,1,1);




















    }

    //encoder movement basis:
    public static void EncoderFB(double LF, double LB, double RF, double RB) {
        RightFEC += RF * COUNTS_PER_TILE;
        RightBEC += RB * COUNTS_PER_TILE;
        LeftFEC += LF * COUNTS_PER_TILE;
        LeftBEC += LB * COUNTS_PER_TILE;

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

        while (RightBack.isBusy() && RightFront.isBusy() && LeftBack.isBusy() && LeftFront.isBusy()) {
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

    public static void EncoderTurn(double degrees) {
        if (degrees > 0) {
            //turn left
            RightFEC += degrees * COUNTS_PER_ROT;
            RightBEC += degrees * COUNTS_PER_ROT;
            LeftFEC -= degrees * COUNTS_PER_ROT;
            LeftBEC -= degrees * COUNTS_PER_ROT;


        } else {
            RightFEC -= degrees * COUNTS_PER_ROT;
            RightBEC -= degrees * COUNTS_PER_ROT;
            LeftFEC += degrees * COUNTS_PER_ROT;
            LeftBEC += degrees * COUNTS_PER_ROT;


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

        while (RightBack.isBusy() && RightFront.isBusy() && LeftBack.isBusy() && LeftFront.isBusy()) {
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
    public static void Detect(Mat imgInput) {
        Mat img = new Mat();
        Imgproc.cvtColor(imgInput, img, Imgproc.COLOR_BGR2HSV);
        final Scalar
                YELLOW = new Scalar(0, 255, 255),
                BLACK = new Scalar(0, 0, 0);

        Rect sec1 = new Rect(100, 100, 100, 100);
        Rect sec2 = new Rect(300, 100, 100, 100);
        Rect sec3 = new Rect(500, 100, 100, 100);

        Mat sec1MAT = new Mat(img, sec1);
        Mat sec2MAT = new Mat(img, sec2);
        Mat sec3MAT = new Mat(img, sec3);

        Scalar sec1Scalar = Core.mean(sec1MAT);
        Scalar sec2Scalar = Core.mean(sec2MAT);
        Scalar sec3Scalar = Core.mean(sec3MAT);

        Scalar green = new Scalar(0, 255, 0);
        double Sec1AvgDist = Math.abs(100 - sec1Scalar.val[0]) + (255 - sec1Scalar.val[1]) + Math.abs(100 - sec1Scalar.val[2]);
        double Sec2AvgDist = (Math.abs(100 - sec2Scalar.val[0]) + (255 - sec2Scalar.val[1]) + Math.abs(100 - sec2Scalar.val[2]));
        double Sec3AvgDist = (Math.abs(100 - sec3Scalar.val[0]) + (255 - sec3Scalar.val[1]) + Math.abs(100 - sec3Scalar.val[2]));


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


        if (Sec1AvgDist < Sec2AvgDist && Sec1AvgDist < Sec3AvgDist) {
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
            DESIRED_TAG_ID = 1;
        } else if (Sec2AvgDist < Sec3AvgDist) {
            System.out.println("2");
            RandNum = 2;
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
            DESIRED_TAG_ID = 2;

        } else {
            System.out.println("3");
            RandNum = 3;
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
            DESIRED_TAG_ID = 3;
        }
    }













// to april tag basis:
        public static void findTag (){
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                if ((detection.metadata != null) &&
                        ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID))) {
                    targetFound = true;
                    desiredTag = detection;
                    break;  // don't look any further.
                } else {
                    telemetry.addData("Unknown Target", "Tag ID %d is not in TagLibrary\n", detection.id);
                }
            }
            if (targetFound) {

                // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
                double rangeError = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
                double headingError = desiredTag.ftcPose.bearing;
                double yawError = desiredTag.ftcPose.yaw;

                // Use the speed and turn "gains" to calculate how we want the robot to move.
                drive = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
                turn = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
                strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

                telemetry.addData("Auto", "Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
            }
            telemetry.update();

            // Apply desired axes motions to the drivetrain.
            moveRobot(drive, strafe, turn);
            sleep(10);
        }
        public static void moveRobot(double x, double y, double yaw){
            // Calculate wheel powers.
            double leftFrontPower = x - y - yaw;
            double rightFrontPower = x + y + yaw;
            double leftBackPower = x + y - yaw;
            double rightBackPower = x - y + yaw;

            // Normalize wheel powers to be less than 1.0
            double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }

            // Send powers to the wheels.
            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);
        }

        /**
         * Initialize the AprilTag processor.
         */
        private void initAprilTag () {
            // Create the AprilTag processor by using a builder.
            aprilTag = new AprilTagProcessor.Builder().build();

            // Create the vision portal by using a builder.
            if (USE_WEBCAM) {
                visionPortal = new VisionPortal.Builder()
                        .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                        .addProcessor(aprilTag)
                        .build();
            } else {
                visionPortal = new VisionPortal.Builder()
                        .setCamera(BuiltinCameraDirection.BACK)
                        .addProcessor(aprilTag)
                        .build();
            }
        }

    /*
     Manually set the camera gain and exposure.
     This can only be called AFTER calling initAprilTag(), and only works for Webcams;
    */
        private void setManualExposure ( int exposureMS, int gain){
            // Wait for the camera to be open, then use the controls

            if (visionPortal == null) {
                return;
            }

            // Make sure camera is streaming before we try to set the exposure controls
            if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
                telemetry.addData("Camera", "Waiting");
                telemetry.update();
                while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                    sleep(20);
                }
                telemetry.addData("Camera", "Ready");
                telemetry.update();
            }

            // Set camera controls unless we are stopping.
            if (!isStopRequested()) {
                ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
                if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                    exposureControl.setMode(ExposureControl.Mode.Manual);
                    sleep(50);
                }
                exposureControl.setExposure((long) exposureMS, TimeUnit.MILLISECONDS);
                sleep(20);
                GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
                gainControl.setGain(gain);
                sleep(20);
            }
        }




















// arm lift basis:
//        public static void Lift ( double inches){
//            LiftET += (inches * COUNTS_PER_LIFT_INCH);
//            Lift.setTargetPosition(LiftET);
//            Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION) l
//            Lift.setPower(0.3);
//            while (Lift.isBusy()) {
//                telemetry.addData("Lifting or something");
//                telemetry.update();
//            }
//            Lift.setPower(0);
//            Lift.setMode(DcMotor.RunMode.BRAKE);
//
//
//        }


    }

