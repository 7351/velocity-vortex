package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 10/1/2016.
 */
@Autonomous(name = "VuforiaCoordinateTest", group = "Testing")
public class VuforiaCoordinateSystem extends OpMode {

    VuforiaLocalizer vuforia;

    List<VuforiaTrackable> allTrackables;

    VuforiaTrackables FTCFieldObjects;

    static final DecimalFormat floatFormat = new DecimalFormat("#.#");

    private final float MM_PER_INCH = 25.4f;
    private final float ROBOT_WIDTH = 18*MM_PER_INCH;               // in mm
    private final float FTC_FIELD_WIDTH = (12*12 - 2)*MM_PER_INCH;  // in mm
    private final float TARGET_HEIGHT = 160.0f;                     // in mm

    OpenGLMatrix phoneLocationMatrix = locationMatrix(90.0f, 0.0f, 0.0f, 0.0f, ROBOT_WIDTH/2.0f, 0.0f);
    OpenGLMatrix lastRobotLocation = null;

    /**
     * Borrowed from FTC Team 492 for OpenGLMatrix Constructor
     * This method creates a location matrix that can be used to relocate an object to its final location
     * <p>
     * by rotating and translating the object from the origin of the field. It is doing the operation in
     * <p>
     * the order of the parameters. In other words, it will first rotate the object on the X-axis, then
     * <p>
     * rotate on the Y-axis, then rotate on the Z-axis, then translate on the X-axis, then translate on
     * <p>
     * the Y-axis and finally translate on the Z-axis.
     *
     * @param rotateX    specifies rotation on the X-axis.
     * @param rotateY    specifies rotation on the Y-axis.
     * @param rotateZ    specifies rotation on the Z-axis.
     * @param translateX specifies translation on the X-axis.
     * @param translateY specifies translation on the Y-axis.
     * @param translateZ specifies translation on the Z-axis.
     * @return returns the location matrix.
     */

    public OpenGLMatrix locationMatrix(float rotateX, float rotateY, float rotateZ, float translateX, float translateY, float translateZ) {
        return OpenGLMatrix.translation(translateX, translateY, translateZ)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, rotateX, rotateY, rotateZ));

    }

    VuforiaLocalizer.Parameters parameters;

    @Override
    public void init() {

        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AbXnxf//////AAAAGRuNC5J8ZEyftEBQmHGLn/JRAsckJezlsbt+FqzEIevPs5nHoqNr8RxWAOXkyTKIYfEkL17legkgm4sV7qv3qcJXlVQE1Xlo/UKbwVQBgzEfGZi9M3d3tgaJNLEeDe1VLXCVrGyrGSThbd364UF/+nsZMhnFGcnLavxaH8N0QWS5QiAgdbV71V4SLS2vWzML4leBiAxl8qqitSqHEmlez4xF5BoyADuT3lLanURW+g+guX7jFo8ONDzI+xjBsi5BCnI41USBfJdhRnh272sUgdpJFetdTQKIlvRifwHOzGz9oX1WpFSOid+NE76fLon5sHVRx4ztQrqBtSQN3J9CgaJo0DjkDyTMbJBTTE56n2Yi";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);

    }

    @Override
    public void start() {

        FTCFieldObjects = vuforia.loadTrackablesFromAsset("FTC_2016-17");

        VuforiaTrackable wheelsTracker = FTCFieldObjects.get(0);
        wheelsTracker.setName("wheels");
        wheelsTracker.setLocation(locationMatrix(90.0f, 0.0f, 0.0f, 12.0f*MM_PER_INCH, FTC_FIELD_WIDTH/2.0f, TARGET_HEIGHT));
        VuforiaTrackable toolsTracker = FTCFieldObjects.get(1);
        toolsTracker.setName("tools");
        toolsTracker.setLocation(locationMatrix(90.0f, 0.0f, 90.0f, -FTC_FIELD_WIDTH/2.0f, 30.0f*MM_PER_INCH, TARGET_HEIGHT));
        VuforiaTrackable legosTracker = FTCFieldObjects.get(2);
        legosTracker.setName("legos");
        legosTracker.setLocation(locationMatrix(90.0f, 0.0f, 0.0f, -30.0f*MM_PER_INCH, FTC_FIELD_WIDTH/2.0f, TARGET_HEIGHT));
        VuforiaTrackable gearsTracker = FTCFieldObjects.get(3);
        gearsTracker.setName("gears");
        gearsTracker.setLocation(locationMatrix(90.0f, 0.0f, 90.0f, -FTC_FIELD_WIDTH/2.0f, -12.0f*MM_PER_INCH, TARGET_HEIGHT));

        allTrackables = new ArrayList<>();
        allTrackables.addAll(FTCFieldObjects);

        for (VuforiaTrackable tracker : FTCFieldObjects) {
            ((VuforiaTrackableDefaultListener) tracker.getListener()).setPhoneInformation(phoneLocationMatrix, parameters.cameraDirection);
        }

        FTCFieldObjects.activate();
    }

    @Override
    public void loop() {

        for (VuforiaTrackable trackable: allTrackables) {
            VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener) trackable.getListener();
            if (listener.isVisible()) {
                if (listener.getRobotLocation() != null) {
                    OpenGLMatrix robotLocation = listener.getRobotLocation();
                    lastRobotLocation = robotLocation;
                    telemetry.addData("robot", String.valueOf(robotLocation.formatAsTransform()));
                }
            }

        }


    }
}
