package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;

/**
 * Created by Dynamic Signals on 10/25/2016.
 */
@Autonomous(name = "ColorTest", group = "Testing")
@Disabled
public class ColorSensorTest extends OpMode {

    ColorUtils colorUtils;

    @Override
    public void init() {

        colorUtils = new ColorUtils(hardwareMap);

    }

    @Override
    public void start() {

        colorUtils.lineColorSensor.enableLed(true);

    }

    @Override
    public void loop() {

        telemetry.addData("Line", "{" + colorUtils.colorData(colorUtils.lineColorSensor) + "} " + "W: " + colorUtils.aboveWhiteLine()
                + ", R: " + colorUtils.aboveRedLine() + ", B: " + colorUtils.aboveBlueLine());
        telemetry.addData("Beacon", "{" + colorUtils.colorData(colorUtils.beaconColorSensor) + "} " + colorUtils.beaconColor());

    }

}
