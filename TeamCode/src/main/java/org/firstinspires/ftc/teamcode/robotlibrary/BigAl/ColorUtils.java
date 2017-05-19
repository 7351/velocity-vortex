package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import android.graphics.Color;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.Autonomous;
import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;

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
        lineColorSensor.setI2cAddress(I2cAddr.create7bit(0x5c/2));
        lineColorSensor.enableLed(false);
        lineColorSensor.enableLed(true);

        // Initialize bottom color sensor with i2c addr 0x4c
        beaconColorSensor = hardwareMap.colorSensor.get("beaconColorSensor");
        beaconColorSensor.setI2cAddress(I2cAddr.create7bit(0x4c/2));
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
        //if ((beaconColorSensor.red() > beaconColorSensor.green() + 1) && (beaconColorSensor.red() > beaconColorSensor.blue() + 1)) {
        if ((beaconColorSensor.red() > beaconColorSensor.green() + 0) && (beaconColorSensor.red() > beaconColorSensor.blue() + 0)){
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

    public HSV getSensorHSV(ColorSensor sensor) {
        HSV returnHSV = new HSV();

        ModernRoboticsI2cColorSensor i2cColorSensor = (ModernRoboticsI2cColorSensor) sensor;

        NormalizedRGBA colors = i2cColorSensor.getNormalizedColors();

        float[] hsv = new float[3];
        android.graphics.Color.RGBToHSV(Math.round(colors.red * 255), Math.round(colors.green * 255), Math.round(colors.blue * 255), hsv);

        returnHSV.hsv = hsv;
        returnHSV.hue = Double.valueOf(AutonomousUtils.df.format(hsv[0]));
        returnHSV.saturation = Double.valueOf(AutonomousUtils.df.format(hsv[1]));
        returnHSV.value = Double.valueOf(AutonomousUtils.df.format(hsv[2]));

        double sumRGB = (colors.red * 255) + (colors.blue * 255) + (colors.green * 255);
        returnHSV.intensity = Double.valueOf(AutonomousUtils.df.format(sumRGB/3));

        return returnHSV;
    }

    /**
     *
     * @param sensor
     * @return int color
     * @see ://developer.android.com/reference/android/graphics/Color.html#BLACK
     */
    public int getSensorColorFromHSV(ColorSensor sensor) {
        return android.graphics.Color.HSVToColor(getSensorHSV(sensor).hsv);
    }

    public class HSV {
        float[] hsv;
        double hue;
        double saturation;
        double value;
        double intensity;

        @Override
        public String toString() {
            return "H: " + hue + ", S: " + saturation + ", I: " + intensity;
        }
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
