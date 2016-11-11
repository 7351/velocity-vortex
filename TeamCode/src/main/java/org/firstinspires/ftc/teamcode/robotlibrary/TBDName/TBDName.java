package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TBDName {

    // We declare all of the objects that the opmode brings into the programs lifecycle
    public VuforiaSystem vuforiaSystem;
    public DriveTrain driveTrain;
    public GyroUtils gyroUtils;
    public ColorUtils colorUtils;
    public FlyWheel flyWheel;
    public Intake intake;
    public DynamicAutonomousSelector das;
    Telemetry telemetry;
    HardwareMap hardwareMap;

    public TBDName(HardwareMap hardwareMap, Telemetry telemetry, boolean VuforiaEnabled) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        if (VuforiaEnabled) {
            vuforiaSystem = new VuforiaSystem();
        }

        driveTrain = new DriveTrain(hardwareMap);
        intake = new Intake(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        gyroUtils.gyro.calibrate();
        flyWheel = new FlyWheel(hardwareMap);
        colorUtils = new ColorUtils(hardwareMap);
        das = new DynamicAutonomousSelector();
    }

    public TBDName(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        driveTrain = new DriveTrain(hardwareMap);
        intake = new Intake(hardwareMap);
        gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
        gyroUtils.gyro.calibrate();
        flyWheel = new FlyWheel(hardwareMap);
        colorUtils = new ColorUtils(hardwareMap);
        das = new DynamicAutonomousSelector();
    }

    public void start() {
        colorUtils.lineColorSensor.enableLed(true);
        gyroUtils.gyro.calibrate();
    }

}
