package us.a7351.dynamicautonomousselector.autonomousgenerator;

/**
 * Created by Dynamic Signals on 1/21/2017.
 */

public abstract class EncoderRoutine extends Routine {

    int EncoderCounts;
    double power;

    public EncoderRoutine(int stage, int EncoderCounts, double power) {
        this.stage = stage;
        this.EncoderCounts = EncoderCounts;
        this.power = power;
    }

}
