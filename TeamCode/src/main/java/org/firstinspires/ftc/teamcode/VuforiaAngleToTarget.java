package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.TBDName;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.VuforiaSystem;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Leo on 10/10/2016.
 */

public class VuforiaAngleToTarget extends OpMode {

    private TBDName tbdName;
    private VuforiaSystem vuforiaSystem;

    private static final DecimalFormat floatFormat = new DecimalFormat("#.#");

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
            if (listener.getPose() != null) {

                VectorF translationData = listener.getPose().getTranslation();

                float Adjacent = Math.abs(translationData.get(2));
                float Opposite = Math.abs(translationData.get(0));

                double unformattedDegrees = Math.toDegrees(Math.atan2(Opposite, Adjacent));
                double formattedDegrees;
                if (translationData.get(0) >= 0) {
                    formattedDegrees = 360 - unformattedDegrees;
                } else {
                    formattedDegrees = unformattedDegrees;
                }
                telemetry.addData(currentTrackable.getName(), String.valueOf(translationData + ", Degrees: " + floatFormat.format(formattedDegrees)));


            }
        }

    }
}
