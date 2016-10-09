package us.a7351.dynamicautonomousselector;

/**
 * Created by Leo on 10/5/2016.
 */

// Abstract class so we can't initialize a generic option. That wouldn't go well in the LinearLayout
abstract class Option{
    String OptionTitle;
    String sharedPreferenceKey;

    /**
     * This is a generic option that includes the pref key and the title of it
     */
    public String getType() {
        return "Generic";
    }
}
