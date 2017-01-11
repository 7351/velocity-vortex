package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;

/**
 * Created by Dynamic Signals on 10/23/2016.
 */

public class ColorUtils {

    public ColorSensor lineColorSensor;
    public ColorSensor beaconColorSensor;
    private HardwareMap hardwareMap;

    public ColorUtils(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        // Initialize bottom color sensor with i2c addr 0x5c
        lineColorSensor = hardwareMap.colorSensor.get("lineColorSensor");
        lineColorSensor.setI2cAddress(I2cAddr.create7bit(0x2e));
        lineColorSensor.enableLed(false);

        // Initialize bottom color sensor with i2c addr 0x4c
        beaconColorSensor = hardwareMap.colorSensor.get("beaconColorSensor");
        beaconColorSensor.setI2cAddress(I2cAddr.create7bit(0x26));
        beaconColorSensor.enableLed(false);
    }

    public boolean aboveWhiteLine() {
        boolean returnValue = false;
        if ((lineColorSensor.red() >= 2) && (lineColorSensor.green() >= 2) && (lineColorSensor.blue() >= 2)) {
            returnValue = true;
        }
        return returnValue;
    }

    public boolean aboveBlueLine() {
        boolean returnValue = false;
        if ((lineColorSensor.blue() > lineColorSensor.red()) && (lineColorSensor.blue() > lineColorSensor.green())) {
            returnValue = true;
        }
        return returnValue;
    }

    public boolean aboveRedLine() {
        boolean returnValue = false;
        if ((lineColorSensor.red() > lineColorSensor.green() + 2) && (lineColorSensor.red() > lineColorSensor.blue() + 2)) {
            returnValue = true;
        }
        return returnValue;
    }

    public Color beaconColor() {
        Color returnColor = Color.NONE;
        if ((beaconColorSensor.red() > beaconColorSensor.green() + 1) && (beaconColorSensor.red() > beaconColorSensor.blue() + 1)) {
            returnColor = Color.RED;
        }
        if ((beaconColorSensor.blue() > beaconColorSensor.red()) && (beaconColorSensor.blue() > beaconColorSensor.green())) {
            returnColor = Color.BLUE;
        }
        if ((beaconColorSensor.red() >= 2) && (beaconColorSensor.green() >= 2) && (beaconColorSensor.blue() >= 2)) {
            returnColor = Color.WHITE;
        }
        return returnColor;
    }

    public enum Color {
        BLUE,
        RED,
        WHITE,
        NONE
    }

    public String colorData(ColorSensor sensor) {
        return String.valueOf("R: " + sensor.red() + " G: " + sensor.green() + " B: " + sensor.blue());
    }


}
