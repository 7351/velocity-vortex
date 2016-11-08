package org.firstinspires.ftc.teamcode.robotlibrary.TBDName;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TBDName {

    Telemetry telemetry;
    HardwareMap hardwareMap;
    // We declare all of the objects that the opmode brings into the programs lifecycle
    public VuforiaSystem vuforiaSystem;
    public DriveTrain driveTrain = new DriveTrain(hardwareMap);
    public GyroUtils gyroUtils = new GyroUtils(hardwareMap, driveTrain, telemetry);
    public ColorUtils colorUtils = new ColorUtils(hardwareMap);
    public FlyWheel flyWheel = new FlyWheel(hardwareMap);
    public Intake intake = new Intake(hardwareMap);
    public DynamicAutonomousSelector das = new DynamicAutonomousSelector();
    public AutonomousUtils autonomousUtils = new AutonomousUtils();

    public TBDName(HardwareMap hardwareMap, Telemetry telemetry, boolean VuforiaEnabled) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        if (VuforiaEnabled) {
            vuforiaSystem = new VuforiaSystem();
        }
    }

    public TBDName(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

}
