/*
Copyright (c) 2024 Limelight Vision
All rights reserved.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@TeleOp(name = "Limelight AprilTag Cluster Test", group = "Sensor")
public class Rerun extends LinearOpMode {

    private Limelight3A limelight;

    // numbers in meters
    private static final double CAMERA_LENS_HEIGHT_METERS = 0.027;
    private static final double TARGET_TAG_HEIGHT_METERS  = 0.062;
    private static final double CAMERA_PITCH_DEGREES      = 0.0;

    //  horizontal offset in meters from each tag's center to the Cluster Center
    private double getTagOffsetMeters(int tagId) {
        int slot = (tagId - 30) % 4;
        switch (slot) {
            case 0: return -0.165;  // Far left tag (6.5 in left of center)
            case 1: return -0.070;  // Inner left tag (2.75 in left of center)
            case 2: return  0.070;  // Inner right tag (2.75 in right of center)
            case 3: return  0.165;  // Far right tag (6.5 in right of center)
            default: return 0.0;
        }
    }

    @Override
    public void runOpMode() throws InterruptedException
    {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        telemetry.setMsTransmissionInterval(11);

        limelight.pipelineSwitch(0);
        limelight.start();

        telemetry.addData(">", "Robot Ready. Press Play.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            LLStatus status = limelight.getStatus();
            telemetry.addData("Name", "%s", status.getName());
            telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                    status.getTemp(), status.getCpu(), (int)status.getFps());

            LLResult result = limelight.getLatestResult();
            if (result != null && result.isValid()) {
                Pose3D botpose = result.getBotpose();
                if (botpose != null) {
                    telemetry.addData("Botpose", botpose.toString());
                }

                // Get vertical angle ty from the Limelight
                double ty = result.getTy();


                // Floor Distance = (Target Height - Camera Lens Height) / tan(Camera Pitch Angle + ty)
                double floorDistanceMeters = (TARGET_TAG_HEIGHT_METERS - CAMERA_LENS_HEIGHT_METERS)
                        / Math.tan(Math.toRadians(CAMERA_PITCH_DEGREES + ty));

                telemetry.addData("Calculated Floor Dist (m)", "%.3f", floorDistanceMeters);
                telemetry.addData("Live ty (deg)", "%.2f", ty);

                // Access fiducial results (AprilTags) and calculate cluster center offset
                List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fr : fiducialResults) {
                    int id = fr.getFiducialId();
                    double tx = fr.getTargetXDegrees();

                    double offsetMeters = getTagOffsetMeters(id);

                    telemetry.addData("Tag ID", id);
                    telemetry.addData("Raw tx", tx);
                    telemetry.addData("Cluster Offset (m)", offsetMeters);
                }
            } else {
                telemetry.addData("Limelight", "No data available");
            }

            telemetry.update();
        }
        limelight.stop();
    }
}