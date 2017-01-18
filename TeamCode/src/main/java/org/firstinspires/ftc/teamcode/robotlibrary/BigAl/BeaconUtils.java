package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Dynamic Signals on 1/17/2017.
 */

public class BeaconUtils {

    public Servo BeaconServo;
    private ColorUtils colorUtils;

    public BeaconUtils(HardwareMap hardwareMap, ColorUtils colorUtils, String alliance) {
        BeaconServo = hardwareMap.servo.get("BeaconServo");
        this.colorUtils = colorUtils;
    }

    public void actOnBeaconWithColorSensor() {
        //TODO: STUB!
    }

}
