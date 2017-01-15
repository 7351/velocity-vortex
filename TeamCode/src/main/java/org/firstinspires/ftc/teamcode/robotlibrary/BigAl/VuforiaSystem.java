package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;

import java.util.ArrayList;
import java.util.List;

public class VuforiaSystem {

    private final float MM_PER_INCH = 25.4f;
    private final float ROBOT_WIDTH = 18 * MM_PER_INCH;               // in mm
    private final float FTC_FIELD_WIDTH = (12 * 12 - 2) * MM_PER_INCH;  // in mm
    private final float TARGET_HEIGHT = 160.0f;                     // in mm

    public VuforiaLocalizer vuforia;
    public List<VuforiaTrackable> allTrackables = new ArrayList<>();

    /* Vuforia Preferences */
    private static final String VUFORIA_LICENSE_KEY = "AbXnxf//////AAAAGRuNC5J8ZEyftEBQmHGLn/JRAsckJezlsbt+FqzEIevPs5nHoqNr8RxWAOXkyTKIYfEkL17legkgm4sV7qv3qcJXlVQE1Xlo/UKbwVQBgzEfGZi9M3d3tgaJNLEeDe1VLXCVrGyrGSThbd364UF/+nsZMhnFGcnLavxaH8N0QWS5QiAgdbV71V4SLS2vWzML4leBiAxl8qqitSqHEmlez4xF5BoyADuT3lLanURW+g+guX7jFo8ONDzI+xjBsi5BCnI41USBfJdhRnh272sUgdpJFetdTQKIlvRifwHOzGz9oX1WpFSOid+NE76fLon5sHVRx4ztQrqBtSQN3J9CgaJo0DjkDyTMbJBTTE56n2Yi";
    private static final VuforiaLocalizer.CameraDirection DIRECTION = VuforiaLocalizer.CameraDirection.BACK;
    private static final String FIELD_DATABASE_NAME = "FTC_2016-17";
    OpenGLMatrix phoneLocationMatrix = locationMatrix(90.0f, 0.0f, 0.0f, 0.0f, ROBOT_WIDTH / 2.0f, 0.0f);
    /* END OF PREFERENCES */

    /**
     * Constructor with the initialization of the Vuforia variable.
     */

    public VuforiaSystem() {
        initializeVuforia();
    }

    /**
     * This method creates a new VuforiaLocalizer.
     * It uses the Vuforia preferences above and fills sets the vuforia variable to the newly created vuforia instance.
     */

    public void initializeVuforia() {
        if (vuforia == null) {
            VuforiaLocalizer.Parameters vuforiaParameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
            vuforiaParameters.cameraDirection = DIRECTION;
            vuforiaParameters.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
            vuforia = ClassFactory.createVuforiaLocalizer(vuforiaParameters);
        } else {
            RobotLog.a("Vuforia already initialized!");
        }

    }

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

    /**
     * This command loads every trackable and adds it to the allTrackables list.
     * It gives the location, name, and the index of the file.
     * It enables the trackable as well.
     * This is a one command to get going and faster development on navigating the field.
     */

    public void loadFieldDatabase() {
        VuforiaTrackables fieldTrackables = vuforia.loadTrackablesFromAsset(FIELD_DATABASE_NAME);
        addToTrackableList(specifyTrackable(fieldTrackables, 0, "wheels", locationMatrix(90.0f, 0.0f, 0.0f, 12.0f * MM_PER_INCH, FTC_FIELD_WIDTH / 2.0f, TARGET_HEIGHT)));
        addToTrackableList(specifyTrackable(fieldTrackables, 1, "tools", locationMatrix(90.0f, 0.0f, 90.0f, -FTC_FIELD_WIDTH / 2.0f, 30.0f * MM_PER_INCH, TARGET_HEIGHT)));
        addToTrackableList(specifyTrackable(fieldTrackables, 2, "legos", locationMatrix(90.0f, 0.0f, 0.0f, -30.0f * MM_PER_INCH, FTC_FIELD_WIDTH / 2.0f, TARGET_HEIGHT)));
        addToTrackableList(specifyTrackable(fieldTrackables, 3, "gears", locationMatrix(90.0f, 0.0f, 90.0f, -FTC_FIELD_WIDTH / 2.0f, -12.0f * MM_PER_INCH, TARGET_HEIGHT)));
        enableAllTrackables();
    }

    /**
     * This function adds a trackable to the allTrackables list
     *
     * @param trackable The VuforiaTrackable object that you want to add
     */
    public void addToTrackableList(VuforiaTrackable trackable) {
        allTrackables.add(trackable);
    }

    /**
     * This iterates through the allTrackables list and activates them.
     * This is to be called after you load all trackables.
     */
    public void enableAllTrackables() {
        for (VuforiaTrackable trackable : allTrackables) {
            trackable.getTrackables().activate();
        }
    }

    /**
     * This command adds all trackables in a VuforiaTrackables object to the allTrackables list.
     *
     * @param trackables a VuforiaTrackables object that you want to add to the list.
     */
    public void addToTrackableList(VuforiaTrackables trackables) {
        allTrackables.addAll(trackables);
    }

    /**
     * This function sets information on a trackable from a vuforiatrackables object.
     *
     * @param TrackableDatabase the object that the trackable exists in.
     * @param XMLIndex          the number of the vuforiatrackable. Zerod indexed.
     * @param Name              the name you want to set the trackable to.
     * @param Location          an OpenGLMatrix to set the trackables position to.
     * @return a vuforiatrackable object.
     */
    public VuforiaTrackable specifyTrackable(VuforiaTrackables TrackableDatabase, int XMLIndex, String Name, OpenGLMatrix Location) {
        VuforiaTrackable currentTrackable = TrackableDatabase.get(XMLIndex);
        currentTrackable.setName(Name);
        if (Location != null) {
            currentTrackable.setLocation(Location);
        }
        return currentTrackable;
    }

    // Same master method without OpenGLMatrix. This is used for non-static positional objects, like balls.
    public VuforiaTrackable specifyTrackable(VuforiaTrackables TrackableDatabase, int XMLIndex, String Name) {
        return specifyTrackable(TrackableDatabase, XMLIndex, Name, null);
    }

    public enum TargetDirection {
        HORIZONTAL,
        VERTICAL
    }

    /*
    public double angleBetweenTargets(OpenGLMatrix PhoneLocationMatrix, OpenGLMatrix TargetLocationMatrix, TargetDirection Direction) {
        VectorF phoneVector = PhoneLocationMatrix.getTranslation();
        VectorF targetVector = TargetLocationMatrix.getTranslation();
        float[] orderedPairs = {phoneVector.get(0), phoneVector.get(2),
                targetVector.get(0), phoneVector.get(2)};


    }
    */

    public double distanceBetweenTargets(OpenGLMatrix PhoneLocationMatrix, OpenGLMatrix TargetLocationMatrix) {
        VectorF phoneVector = PhoneLocationMatrix.getTranslation();
        VectorF targetVector = TargetLocationMatrix.getTranslation();
        double[] orderedPairPhone = {phoneVector.get(0), phoneVector.get(2)};
        double[] orderedPairTarget = {targetVector.get(0), phoneVector.get(2)};
        double differenceX = orderedPairPhone[0] - orderedPairTarget[0];
        double differenceZ = orderedPairPhone[1] - orderedPairTarget[1];
        return Math.abs(Math.sqrt(Math.pow(differenceX, 2) + Math.pow(differenceZ, 2)));
    }


}
