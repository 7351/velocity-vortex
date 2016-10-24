package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 9/14/2016.
 */
public class VuforiaTest extends OpMode {

    VuforiaLocalizer vuforia;
    List<VuforiaTrackable> allTrackables;

    VuforiaTrackables FTCFieldObjects;

    static final DecimalFormat floatFormat = new DecimalFormat("#.#");

    @Override
    public void init() {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AbXnxf//////AAAAGRuNC5J8ZEyftEBQmHGLn/JRAsckJezlsbt+FqzEIevPs5nHoqNr8RxWAOXkyTKIYfEkL17legkgm4sV7qv3qcJXlVQE1Xlo/UKbwVQBgzEfGZi9M3d3tgaJNLEeDe1VLXCVrGyrGSThbd364UF/+nsZMhnFGcnLavxaH8N0QWS5QiAgdbV71V4SLS2vWzML4leBiAxl8qqitSqHEmlez4xF5BoyADuT3lLanURW+g+guX7jFo8ONDzI+xjBsi5BCnI41USBfJdhRnh272sUgdpJFetdTQKIlvRifwHOzGz9oX1WpFSOid+NE76fLon5sHVRx4ztQrqBtSQN3J9CgaJo0DjkDyTMbJBTTE56n2Yi";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
    }

    @Override
    public void start() {

        FTCFieldObjects = vuforia.loadTrackablesFromAsset("FTC_2016-17");

        VuforiaTrackable wheelsTracker = FTCFieldObjects.get(0);
        wheelsTracker.setName("wheels");
        VuforiaTrackable toolsTracker = FTCFieldObjects.get(1);
        toolsTracker.setName("tools");
        //toolsTracker.setLocation();
        VuforiaTrackable legosTracker = FTCFieldObjects.get(2);
        legosTracker.setName("legos");
        //legosTracker.setLocation();
        VuforiaTrackable gearsTracker = FTCFieldObjects.get(3);
        gearsTracker.setName("gears");
        //gearsTracker.setLocation();

        allTrackables = new ArrayList<>();
        allTrackables.addAll(FTCFieldObjects);

        FTCFieldObjects.activate();
    }

    @Override
    public void loop() {

        for (int i = 0; i < allTrackables.size(); i++) {
            VuforiaTrackable currentTrackable = allTrackables.get(i);
            VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener) currentTrackable.getListener();
            if (!listener.isVisible()) {
                telemetry.addData(currentTrackable.getName(), "Not visible");
            } else {
                if (listener.getPose() != null) {
                    if (listener.getPose().getData() != null) {
                        float[] listenerValue = listener.getPose().getData();
                        for (int e = 0; e > listenerValue.length; e++) {
                            Log.d("Vuforia", String.valueOf(e + ": " + listenerValue[e]));
                        }
                        telemetry.addData(currentTrackable.getName(),


                                floatFormat.format(listenerValue[0]) + "X " +
                                        floatFormat.format(listenerValue[1]) + "Y " +
                                        floatFormat.format(listenerValue[2]) + "Z ");
                    }

                }
            }


        }


    }

    @Override
    public void stop() {

    }
}
