package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.ColorUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderDrive;

/**
 * Created by Dynamic Signals on 1/22/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "WhiteLineTest", group = "Testing")
public class WhiteLineTest extends OpMode {

    DriveTrain driveTrain;
    ColorUtils colorUtils;
    int stage = 0;
    EncoderDrive drive;

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
            if (drive == null) {
                drive = new EncoderDrive(driveTrain, 1000, 0.2);
                drive.run();
            }
            if (drive.isCompleted()) {
                stage = 100;
                driveTrain.stopRobot();
            }
            if (colorUtils.aboveWhiteLine()) {
                stage = 200;
                driveTrain.stopRobot();
            }
            if (time > 10) {
                stage = 300;
                driveTrain.stopRobot();
            }
        }

        if (stage == 100) {
            telemetry.addData("Reason Stopped", "Encoder");
        }
        if (stage == 200) {
            telemetry.addData("Reason Stopped", "Color Sensor");
        }
        if (stage == 300) {
            telemetry.addData("Reason Stopped", "Time");
        }




    }
}
