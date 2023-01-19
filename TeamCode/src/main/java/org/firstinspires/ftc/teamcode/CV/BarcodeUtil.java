package org.firstinspires.ftc.teamcode.CV;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

public class BarcodeUtil {
    private Telemetry telemetry;

    private OpenCvWebcam webcam;
    private BarcodePositionDetector pipeline;
	static final int WAIT_LIMIT = 50;

    public BarcodeUtil(HardwareMap hardwareMap, String webcamName, Telemetry telemetry) {
        //telemetry.setAutoClear(false);
        this.telemetry = telemetry;
        int cameraMonitorViewId = hardwareMap.appContext.getResources()
			.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance()
			.createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        pipeline = new BarcodePositionDetector(telemetry);
        webcam.setPipeline(pipeline);
    }


    public void init( ) {
        openCameraDevice( );
    }

    public void setTimeoutTime( int milliseconds ) {
        // Timeout for obtaining permission is configurable. Set before opening.
        webcam.setMillisecondsPermissionTimeout( milliseconds );
    }

    public void openCameraDevice( ) {
        //testr

        webcam.openCameraDeviceAsync( new OpenCvCamera.AsyncCameraOpenListener( ) {
            @Override
            public void onOpened( ) {
                webcam.startStreaming( 320, 240, OpenCvCameraRotation.UPRIGHT ); // Camera dimensions are set here
            }

            @Override
            public void onError( int errorCode ) {
                //This will be called if the camera could not be opened
                Log.e( "CAMERA_DEVICE", "Camera could not be opened. Error code: " + errorCode );
            }
        } );
    }

    /* 1/18/2023 - commented out because it was causing code to not build
    public BarcodePositionDetector.BarcodePosition getBarcodePosition() throws InterruptedException {
		int tries = 0;
		BarcodePositionDetector.BarcodePosition ret;
		do {
			Thread.sleep(10);
			ret = pipeline.getBarcodePostiion();
		} while (ret == BarcodePositionDetector.BarcodePosition.NOT_READ && i++ < WAIT_LIMIT);
		webcam.stopStreaming();
		return ret;
    }

     */
}
