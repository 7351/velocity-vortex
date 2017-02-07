package org.firstinspires.ftc.teamcode.robotlibrary.BigAl;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BigAl {

    // We declare all of the objects that the opmode brings into the programs lifecycle
    public VuforiaSystem vuforiaSystem;
    public DriveTrain driveTrain;
    public GyroUtils gyroUtils;
    public BeaconUtils beaconUtils;
    public ColorUtils colorUtils;
    public FlyWheel flyWheel;
    public Intake intake;
    public RangeUtils rangeUtils;
    public DynamicAutonomousSelector das;
    public Lift lift;

    public BigAl(HardwareMap hardwareMap, Telemetry telemetry, boolean VuforiaEnabled) {
        if (VuforiaEnabled) {
            vuforiaSystem = new VuforiaSystem();
        }
        rangeUtils = new RangeUtils(hardwareMap);
        driveTrain = new DriveTrain(hardwareMap);
        intake = new Intake(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        gyroUtils.gyro.calibrate();
        flyWheel = new FlyWheel(hardwareMap);
        colorUtils = new ColorUtils(hardwareMap);
        das = new DynamicAutonomousSelector();
        lift = new Lift(hardwareMap);
        beaconUtils = new BeaconUtils(hardwareMap, colorUtils);
    }

    public BigAl(HardwareMap hardwareMap, Telemetry telemetry) {
        this(hardwareMap, telemetry, false);
    }

    public void start() {
        colorUtils.lineColorSensor.enableLed(true);
        //gyroUtils.gyro.calibrate();
    }

}
