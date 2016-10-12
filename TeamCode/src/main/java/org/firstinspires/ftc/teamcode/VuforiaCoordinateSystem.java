package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TBDName;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.VuforiaSystem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 10/1/2016.
 */
@Autonomous(name = "VuforiaCoordinateTest", group = "Testing")
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
