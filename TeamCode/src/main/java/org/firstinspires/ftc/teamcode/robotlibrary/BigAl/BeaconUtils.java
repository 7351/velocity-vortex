package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Dynamic Signals on 1/17/2017.
 */

public class BeaconUtils {

    public Servo BeaconServo;
    private ColorUtils colorUtils;
    private String alliance;

    public BeaconUtils(HardwareMap hardwareMap, ColorUtils colorUtils, String alliance) {
        BeaconServo = hardwareMap.servo.get("BeaconServo");
        this.colorUtils = colorUtils;
        this.alliance = alliance;
    }

    public void actOnBeaconWithColorSensor() {
        ColorUtils.Color beaconColor = colorUtils.beaconColor();
        if (alliance.equals("Red")) {
            switch (beaconColor) {
                case RED:

                    break;
                case BLUE:

                    break;
                default:
                    // Case for reading nothing or white (the beacon)
                    break;
            }
        }
        if (alliance.equals("Blue")) {
            switch (beaconColor) {
                case RED:

                    break;
                case BLUE:

                    break;
                default:
                    // Case for reading nothing or white (the beacon)
                    break;
            }
        }
    }

}
