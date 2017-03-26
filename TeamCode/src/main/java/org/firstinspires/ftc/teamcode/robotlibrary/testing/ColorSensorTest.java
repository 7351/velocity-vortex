package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;

/**
 * Created by Dynamic Signals on 10/25/2016.
 */
@Autonomous(name = "ColorTest", group = "Testing")
public class ColorSensorTest extends OpMode {

    ColorUtils colorUtils;

    @Override
    public void init() {

        colorUtils = new ColorUtils(hardwareMap);

    }

    @Override
    public void loop() {

        telemetry.addData("Line", "{" + colorUtils.colorData(colorUtils.lineColorSensor) + "} " + "W: " + colorUtils.aboveWhiteLine()
                + ", R: " + colorUtils.aboveRedLine() + ", B: " + colorUtils.aboveBlueLine());
        telemetry.addData("Beacon", "{" + colorUtils.colorData(colorUtils.beaconColorSensor) + "} " + colorUtils.beaconColor());

    }

}
