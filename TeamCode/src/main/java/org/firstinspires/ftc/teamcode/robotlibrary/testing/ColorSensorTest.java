package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import android.graphics.Color;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;

/**
 * Created by Dynamic Signals on 10/25/2016.
 */
@Autonomous(name = "ColorTest", group = "Testing")
public class ColorSensorTest extends OpMode {

    ColorUtils colorUtils;
    ModernRoboticsI2cColorSensor i2cColorSensor;

    @Override
    public void init() {

        colorUtils = new ColorUtils(hardwareMap);
        i2cColorSensor = (ModernRoboticsI2cColorSensor) colorUtils.beaconColorSensor;

    }

    @Override
    public void loop() {

        telemetry.addData("Line", "{" + colorUtils.colorData(colorUtils.lineColorSensor) + "} " + "W: " + colorUtils.aboveWhiteLine()
                + ", R: " + colorUtils.aboveRedLine() + ", B: " + colorUtils.aboveBlueLine());
        telemetry.addData("Beacon", "{" + colorUtils.colorData(colorUtils.beaconColorSensor) + "} " + colorUtils.beaconColor());

        NormalizedRGBA colors = i2cColorSensor.getNormalizedColors();

        float[] hsv = new float[3];
        Color.RGBToHSV(Math.round(colors.red * 100), Math.round(colors.green * 100), Math.round(colors.blue * 100), hsv);

        float hue = hsv[0];
        float sat = hsv[1];
        float val = hsv[2];

        telemetry.addData("Beacon HSV", "H: " + hue + ", S: " + sat + "m V: " + val);

    }

}
