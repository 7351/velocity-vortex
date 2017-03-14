package org.firstinspires.ftc.teamcode.robotlibrary.testing;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autonomous;
import org.firstinspires.ftc.teamcode.robotlibrary.AutonomousUtils;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.DriveTrain;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.EncoderTurn;
import org.firstinspires.ftc.teamcode.robotlibrary.BigAl.GyroUtils;

/**
 * Created by Dynamic Signals on 12/6/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "EncoderTurnTest", group = "Testing")
public class EncoderTurnTest extends OpMode {

    DriveTrain driveTrain;
    AHRS navx;

    EncoderTurn turn;

    ElapsedTime time = new ElapsedTime();
    double completedTime = 0;
    int completedCounts = 0;
    double completedYaw = 0;


    int timesRan = 0;
    int stage = 0;

    @Override
    public void init() {

        driveTrain = new DriveTrain(hardwareMap);
        navx = AHRS.getInstance(hardwareMap);

        driveTrain.RightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /* Encoder drive sequence
     * 1. Reset
     * 2. Set target
     * 3. Set mode to RUN_TO_POSITION
     * 4. Set motor power
     * 5. Wait until motor isn't busy
     */

    @Override
    public void start() {

    }

    @Override
    public void loop() {

        if (stage != 2) {
            telemetry.addData("F", driveTrain.LeftFrontMotor.getCurrentPosition() + ":" + driveTrain.RightFrontMotor.getCurrentPosition());
            telemetry.addData("B", driveTrain.LeftBackMotor.getCurrentPosition() + ":" + driveTrain.RightBackMotor.getCurrentPosition());
            telemetry.addData("Yaw", AutonomousUtils.df.format(navx.getYaw()));
            telemetry.addData("Stage", stage);
        }


        if (stage == 0) {
            if (!navx.isCalibrating()) {
                navx.zeroYaw();
                stage++;
                time.reset();
                turn = null;
            }
        }

        if (stage == 1) {
            if (turn == null) {
                double counts = 20; // Change the counts here for testing
                turn = new EncoderTurn(driveTrain, counts, GyroUtils.Direction.CLOCKWISE, true);
                turn.run();
            }
            if (turn.isCompleted()) {
                turn.completed();
                stage++;
            }
        }

        if (stage == 2) {
            if (completedTime == 0) {
                completedTime = time.time();
                time.reset();
            }
            if (completedCounts == 0) completedCounts = driveTrain.LeftFrontMotor.getCurrentPosition();
            if (completedYaw == 0) completedYaw = navx.getYaw();
            telemetry.addData("Counts", completedCounts);
            telemetry.addData("Yaw", AutonomousUtils.df.format(completedYaw));
            telemetry.addData("Time", AutonomousUtils.df.format(completedTime));
            telemetry.addData("Times ran", timesRan);

            if (time.time() > 12) {
                if (timesRan < 20) {
                    stage = 0;
                    timesRan++;
                    completedYaw = 0;
                    completedCounts = 0;
                    completedTime = 0;
                }
            }

        }
    }
}
