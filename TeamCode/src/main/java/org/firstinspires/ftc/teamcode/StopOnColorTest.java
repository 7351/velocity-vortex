package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.TBDName.DriveTrain;

/**
 * Created by Dynamic Signals on 11/6/2016.
 */
@Autonomous(name = "StopOnColorTest", group = "Testing")
public class StopOnColorTest extends OpMode {

    ColorUtils colorUtils;
    DriveTrain driveTrain;
    int stage = 0;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        colorUtils = new ColorUtils(hardwareMap);

    }

    @Override
    public void start() {

        colorUtils.lineColorSensor.enableLed(true);

    }

    @Override
    public void loop() {


        if (stage == 0) {
            if (!colorUtils.aboveRedLine()) {
                driveTrain.driveStraight(0.5);
            } else {
                driveTrain.stopRobot();
                stage++;
            }
        }


        telemetry.addData("Red", String.valueOf(colorUtils.aboveRedLine()));
        telemetry.addData("Blue", String.valueOf(colorUtils.aboveBlueLine()));
        telemetry.addData("White", String.valueOf(colorUtils.aboveWhiteLine()));


    }
}
