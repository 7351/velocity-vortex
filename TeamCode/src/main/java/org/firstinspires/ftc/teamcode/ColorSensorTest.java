package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;

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
    public void start() {

        colorUtils.lineColorSensor.enableLed(true);

    }

    @Override
    public void loop() {

        telemetry.addData("Bottom", colorData(colorUtils.lineColorSensor));
        telemetry.addData("Beacon", colorData(colorUtils.beaconColorSensor));
        telemetry.addData("Beacon", colorUtils.beaconColor());

    }

    private String colorData(ColorSensor sensor) {
        return String.valueOf("R: " + sensor.red() + " G: " + sensor.green() + " B: " + sensor.blue());
    }
}
