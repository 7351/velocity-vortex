package us.a7351.dynamicautonomousselector;

import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by Leo on 10/5/2016.
 */

class NumberOption extends Option {

    int MaxNumber;

    // This is the UI edittext view binded to the Option instance for easier tracking and retrieval of values
    EditText editText;

    /**
     * This creates a new reference to a EditText It will take in any number value from the drive team
     * Example: You can have an option for the prox value to the wall as a decimal, typical value: "5.0"
     * @param MaxNumber The max amount of numbers that the drive team can enter. This helps prevent error
     * @param OptionTitle The title that includes the question above the option
     * @param sharedPrefKey The sharedpreference key that you can use in your code. (P.S. Its actually a hashmap...)
     */
    NumberOption(int MaxNumber, String OptionTitle, String sharedPrefKey) {
        // Set the max amount of digits inside the edittext to the max amount
        this.MaxNumber = MaxNumber;
        this.OptionTitle = OptionTitle;
        this.sharedPreferenceKey = sharedPrefKey;
    }

    // Same as above but with the absense of the max number of characters, it defaults to 4
    NumberOption(String OptionTitle, String sharedPrefKey) {
        // By default we will set it to 4
        MaxNumber = 4;
        this.OptionTitle = OptionTitle;
        this.sharedPreferenceKey = sharedPrefKey;
    }

    // Helps identify the type of option that the instance of the object is. Helps in polymorphism and casting
    public String getType() {
        return "Number";
    }
}
