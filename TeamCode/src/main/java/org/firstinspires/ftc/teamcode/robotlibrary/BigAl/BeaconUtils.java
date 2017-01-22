package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Dynamic Signals on 1/17/2017.
 */

public class BeaconUtils {

    public Servo BeaconServo;
    private ColorUtils colorUtils;
    private String alliance = "NS";
    private double leftPosition = 0.14, center = 0.37, rightPosition = 0.65;

    public BeaconUtils(HardwareMap hardwareMap, ColorUtils colorUtils, @NonNull String alliance) {
        BeaconServo = hardwareMap.servo.get("BeaconServo");
        this.colorUtils = colorUtils;
        this.alliance = alliance;
        rotateServo(ServoPosition.CENTER);
    }

    public BeaconUtils(HardwareMap hardwareMap, ColorUtils colorUtils) {
        BeaconServo = hardwareMap.servo.get("BeaconServo");
        this.colorUtils = colorUtils;
        alliance = "NS";
        rotateServo(ServoPosition.CENTER);
    }

    public void setAlliance(String alliance) {
        this.alliance = alliance;
    }

    public String getAlliance() {
        return alliance;
    }

    public enum ServoPosition {
        TRIGGER_LEFT,
        CENTER,
        TRIGGER_RIGHT
    }

    public void rotateServo(ServoPosition position) {
        switch (position) {
            case TRIGGER_LEFT:
                BeaconServo.setPosition(leftPosition);
                break;
            case CENTER:
                BeaconServo.setPosition(center);
                break;
            case TRIGGER_RIGHT:
                BeaconServo.setPosition(rightPosition);
                break;
        }
    }

    public void actOnBeaconWithColorSensor() {
        ColorUtils.Color beaconColor = colorUtils.beaconColor();
        if (alliance.equals("Red")) {
            switch (beaconColor) {
                case RED:
                    rotateServo(ServoPosition.TRIGGER_LEFT);
                    break;
                case BLUE:
                    rotateServo(ServoPosition.TRIGGER_RIGHT);
                    break;
                default:
                    // Case for reading nothing or white (the beacon)
                    break;
            }
        }
        if (alliance.equals("Blue")) {
            switch (beaconColor) {
                case RED:
                    rotateServo(ServoPosition.TRIGGER_RIGHT);
                    break;
                case BLUE:
                    rotateServo(ServoPosition.TRIGGER_LEFT);
                    break;
                default:
                    // Case for reading nothing or white (the beacon)
                    break;
            }
        }
    }

}
