package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TBDName;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.VuforiaSystem;

import java.util.List;

/**
 * Created by Leo on 10/1/2016.
 */
public class VuforiaCoordinateSystem extends OpMode {

    TBDName tbdName;
    VuforiaSystem vuforiaSystem;

    OpenGLMatrix lastRobotLocation = null;

    @Override
    public void init() {

        tbdName = new TBDName(hardwareMap, telemetry);
        vuforiaSystem = tbdName.vuforiaSystem;

    }

    @Override
    public void start() {

        vuforiaSystem.loadFieldDatabase();
    }

    @Override
    public void loop() {

        List<VuforiaTrackable> allTrackables = vuforiaSystem.allTrackables;

        for (VuforiaTrackable currentTrackable : allTrackables) {
            VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener) currentTrackable.getListener();
            if (listener.getRobotLocation() != null) {

                OpenGLMatrix robotLocation = listener.getRobotLocation();
                lastRobotLocation = robotLocation;
                telemetry.addData("robot", String.valueOf(robotLocation.formatAsTransform()));
                //need update telemetry

            }
        }


    }
}
