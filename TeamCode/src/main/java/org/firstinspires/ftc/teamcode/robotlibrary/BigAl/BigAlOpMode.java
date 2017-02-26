package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Dynamic Signals on 2/26/2017.
 */

public abstract class BigAlOpMode extends OpMode {

    BigAl bigAl;
    VuforiaSystem vuforiaSystem;
    DriveTrain driveTrain;
    BeaconUtils beaconUtils;
    ColorUtils colorUtils;
    FlyWheel flyWheel;
    Intake intake;
    RangeUtils rangeUtils;
    DynamicAutonomousSelector das;
    Lift lift;

    EncoderTurn turn;
    EncoderDrive drive;
    GyroTurn gyroTurn;

    int stage = 0;
    ElapsedTime time = new ElapsedTime();

    @Override
    public void init() {
        bigAl = new BigAl(hardwareMap);
        vuforiaSystem = bigAl.vuforiaSystem;
        driveTrain = bigAl.driveTrain;
        beaconUtils = bigAl.beaconUtils;
        colorUtils = bigAl.colorUtils;
        flyWheel = bigAl.flyWheel;
        intake = bigAl.intake;
        rangeUtils = bigAl.rangeUtils;
        das = bigAl.das;
        lift = bigAl.lift;
    }

    @Override
    public void start() {
        super.start();
        bigAl.start();
        time.reset();
    }

    @Override
    public void loop() { // We start off at stage 0

    }

    @Override
    public void stop() {
        super.stop();
    }
}
