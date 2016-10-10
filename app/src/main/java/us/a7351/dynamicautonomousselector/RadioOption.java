package us.a7351.dynamicautonomousselector;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

/**
 * Created by Leo on 10/5/2016.
 */
// This Option type allows you to have the user choose out of a radio group
class RadioOption extends Option {

    String[] availableOptions;

    // UI Binding of the radioGroup to the instance of the option for easier retrieval of values
    RadioGroup radioGroup;

    List<RadioButton> radioButtonList;

    /**
     * Creates a new Option that can have radio options
     * @param availableOptions the string array of available option for the radiogroup
     * @param OptionTitle the title of the option
     * @param sharedPrefKey the sharedpreference key of the option. (P.S. its actually a hashmap)
     */
    public RadioOption(String[] availableOptions, String OptionTitle, String sharedPrefKey) {
        // We will set the available option to the list passed in the constructor
        this.availableOptions = availableOptions;
        this.OptionTitle = OptionTitle;
        this.sharedPreferenceKey = sharedPrefKey;
    }

    /**
     * Helps polymorphism and casting the object
     * @return the type of option: "Radio"
     */
    public String getType() {
        return "Radio";
    }

}
