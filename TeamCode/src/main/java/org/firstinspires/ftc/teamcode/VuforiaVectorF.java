package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TBDName;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.VuforiaSystem;

import java.util.List;

@Autonomous(name = "VuforiaEasy", group = "Testing")
public class VuforiaVectorF extends OpMode {

    private TBDName tbdName;
    private VuforiaSystem vuforiaSystem;

    @Override
    public void init() {

        // We pass through the two live components of an OpMode
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
            if (listener.getPose() != null) {

                VectorF translationData = listener.getPose().getTranslation();
                telemetry.addData(currentTrackable.getName(), String.valueOf(translationData));

                double degrees = Math.toDegrees(Math.atan2(translationData.get(1), translationData.get(2)));
                //RobotLog.a(String.valueOf(currentTrackable.getName() + ": " + degrees));

            }
        }


    }
}
