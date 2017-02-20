package org.firstinspires.ftc.teamcode.robotlibrary.testing.navxtesting;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.PID;

import java.text.DecimalFormat;

/**
 * Created by Dynamic Signals on 2/19/2017.
 */

@Autonomous(name = "PIDTuner", group = "Sensor")
public class PIDTuner extends OpMode {

    AHRS navx;
    GyroTurn gyroTurn;
    DriveTrain driveTrain;
    int stage = 0;
    PID pid;
    double tweakerIncrement = 0.0001;
    DecimalFormat df = new DecimalFormat("#.##");
    DecimalFormat df2 = new DecimalFormat("#.######");
    ElapsedTime time = new ElapsedTime();

    boolean dpadupPressed = false;
    boolean dpaddownPressed = false;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);

        navx = AHRS.getInstance(hardwareMap);

        pid = new PID();

    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {

        if (stage == 0) {
            if (!navx.isCalibrating()) {
                stage++;
                navx.zeroYaw();
            }
        }

        if (stage == 1) {
            telemetry.addData("ACTION:", "Press D-Pad up or down to tweak variables now");

            if (gamepad1.dpad_up) {
                if (!dpadupPressed) {
                    pid.p += tweakerIncrement;
                    dpadupPressed = true;
                }
            } else {
                dpadupPressed = false;
            }

            if (gamepad1.dpad_down) {
                if (!dpaddownPressed) {
                    pid.p -= tweakerIncrement;
                    dpaddownPressed = true;
                }
            } else {
                dpaddownPressed = false;
            }

            if (gamepad1.dpad_right) {
                navx.zeroYaw();
                stage++;
            }

        }

        if (stage == 2) {
            if (gyroTurn == null) {
                gyroTurn = new GyroTurn(navx, driveTrain, 90, pid);
                time.reset();
            }
            gyroTurn.run();
            if (gyroTurn.isCompleted()) {
                stage = 1;
                gyroTurn = null;
            }
            if (time.time() > 5) {
                gyroTurn.yawPIDController.enable(false);
                driveTrain.stopRobot();
                stage = 1;
                gyroTurn = null;
            }
        }

        telemetry.addData("PID variables", "P: " + df2.format(pid.p) + ", I: " + pid.i + ", D: " + pid.d);
        telemetry.addData("Stage", String.valueOf(stage));
        telemetry.addData("L", driveTrain.LeftFrontMotor.getPower());
        telemetry.addData("R", driveTrain.RightFrontMotor.getPower());
        telemetry.addData("Heading", df.format(navx.getYaw()));

    }
}
