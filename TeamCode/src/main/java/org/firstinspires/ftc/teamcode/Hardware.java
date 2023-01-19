package org.firstinspires.ftc.teamcode;

import android.transition.Slide;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.CV.BarcodeUtil;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.util.Util;
// import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


public class Hardware {
    HardwareMap hwMap = null;
    public Drivetrain dt = null;
    DcMotor slides = null;
    Servo elbow1 = null;
    Servo elbow2 = null;
    public Grabber grabber = null;
    public static SlidesTarget slidesPosition = null;
    public SampleMecanumDrive drive;
    BarcodeUtil cvUtil;


    private Telemetry telemetry;

    public static final double elbow_min = 0.40;
    public static final double elbow_max = 1.00;

    public Hardware(HardwareMap ahwMap, Telemetry telemetry) {
        hwMap = ahwMap;
        this.telemetry = telemetry;
        dt = new Drivetrain(ahwMap, telemetry);
        drive = new SampleMecanumDrive(ahwMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        cvUtil = new BarcodeUtil(hwMap, "Webcam1", telemetry);

        //dt = new org.firstinspires.ftc.teamcode.Drivetrain(hwMap);

        slides = ahwMap.get(DcMotor.class, "slides"); // expansion hub port 0
        slides.setDirection(DcMotor.Direction.REVERSE);
        slides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slidesPosition = SlidesTarget.FRONT_GROUND;

        elbow1 = ahwMap.get(Servo.class, "elbow1"); // control hub port 0
        elbow2 = ahwMap.get(Servo.class, "elbow2"); // control hub port 1
        elbow1.setDirection(Servo.Direction.FORWARD);
        elbow2.setDirection(Servo.Direction.REVERSE);

        grabber = new Grabber(ahwMap);
    }

    //Inits hardware for opmode
    public void init() throws InterruptedException {

        int slides_reset_position = 1000;

        grabber.closeClaw();
        grabber.rightGrabberFace();
        Thread.sleep(500);
        /*
        slides.setTargetPosition(slides_reset_position);
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slides.setPower(.2);
        Thread.sleep(1500);
         */
        elbowMove(SlidesTarget.FRONT_GROUND.elbow_position);
        slides.setTargetPosition(0);
        slides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (slides.getCurrentPosition() > 0) {
            slides.setPower(.2);
        }
        telemetry.addData("claw: ", grabber.claw.getPosition());
        telemetry.addData("wrist: ", grabber.wrist.getPosition());
        telemetry.addData("slides: ", slides.getCurrentPosition());
        telemetry.addData("elbow: ", elbow1.getPosition());

        //cvUtil = new BarcodeUtil(hwMap, "Webcam1", telemetry);
        //colorFront = hardwareMap.get(NormalizedColorSensor.class, "color_front");
        //hwMap = ahwMap;
        //dt = new Drivetrain(hwMap);     // FL, FR, BL, BR
        // intake = new Intake(hwMap);  // intakeFront, intakeBack
        //cvUtil = new BarcodeUtil(hwMap,"Webcam1",telemetry);

        //drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //vel = new Pose2d(0,0,0);


    }

    public void elbowMove(double position)
    {
        position = Util.clamp(position, elbow_min, elbow_max);
        elbow1.setPosition(position);
        elbow2.setPosition(position);
    }

    public void flipElbowAndWrist(boolean raiseClaw) {
        if (raiseClaw) {
            this.elbowMove(Hardware.elbow_min);
            this.grabber.flipGrabberFace();
        } else {
            this.elbowMove(Hardware.elbow_max);
            this.grabber.rightGrabberFace();
        }

    }

    public Drivetrain getDrivetrain() {
        return dt;
    }
}
