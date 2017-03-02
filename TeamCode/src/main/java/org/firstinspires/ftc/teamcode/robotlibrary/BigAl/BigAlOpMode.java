package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Dynamic Signals on 2/26/2017.
 */

public abstract class BigAlOpMode extends OpMode {

    public BigAl bigAl;
    public VuforiaSystem vuforiaSystem;
    public DriveTrain driveTrain;
    public BeaconUtils beaconUtils;
    public ColorUtils colorUtils;
    public FlyWheel flyWheel;
    public Intake intake;
    public RangeUtils rangeUtils;
    public DynamicAutonomousSelector das;
    public Lift lift;
    public AHRS navx;

    public EncoderTurn turn;
    public EncoderDrive drive;
    public GyroTurn gyroTurn;

    public int stage = 0;
    public ElapsedTime time = new ElapsedTime();

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
        navx = AHRS.getInstance(hardwareMap);
    }

    @Override
    public void start() {
        super.start();
        bigAl.start();
        time.reset();
    }

    @Override
    public void stop() {
        navx.close();
        super.stop();
    }
}
