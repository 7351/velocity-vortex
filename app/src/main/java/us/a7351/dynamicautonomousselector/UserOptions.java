package us.a7351.dynamicautonomousselector;

class UserOptions {
    /**
     * This is the input for the programmers to enter all options
     */
    static final Option[] options = new Option[]{
            /**
             * NumberOption:
             * The way to have the drive team enter number values like wait time, prox values, etc...
             * is in the following format:
             * new NumberOption("Title of the option", "hashMap key of the option),
             * You need the comma at the end unless it is the last option in the list
             * Example: A option where the drive team can enter in a value for the angle heading with a max value of 3 characters
             * the hashmap key would be "gyroValue" so you can call selectorHashmap.get("gyroValue");
             * You would write: new NumberOption(3, "Gyro value?", "gyroValue"), unless
             * @see NumberOption
            */

            new NumberOption("Prox minimum", "proxkey"),
            new RadioOption(new String[]{"Kentucky", "Hawaii", "Mountain", "Center vortex"}, "Target", "target"),
            new RadioOption(new String[]{"Red", "Blue"}, "Alliance", "alliance"),
            new NumberOption("Distance", "distancekey")
    };
}
