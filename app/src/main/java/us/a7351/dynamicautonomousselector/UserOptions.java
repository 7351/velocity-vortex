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
             * the hashmap key would be "gyroValue" so you can call selectorHashmap.get("gyroValue"); in the op-mode code
             * You would write: new NumberOption(3, "Gyro value?", "gyroValue"), unless
             * @see NumberOption
            */

            /**
             * RadioOption:
             * The way to have the drive team pick from a pre-given list of items is in the following format:
             * new RadioOption(String[]{"Option1", "Option2", "Option3"}, "Title of option", "hashMap key of the option),
             * You need the comma at the end unless it is the last in the list
             * Example: An option where the drive team can choose what alliance they're on for the current match
             * the hashmap key would be "alliance" so you can call selectorHashmap.get("alliance"); in the op-mode code
             * You would write: new RadioOption(new String[]{"Red", "Blue"}, "Alliance?", "alliance");
             * @see RadioOption
            */
            new RadioOption(new String[]{"Red", "Blue"}, "Alliance?", "alliance"),
            new NumberOption(2, "Delay before match?", "delay"),
            new RadioOption(new String[]{"0", "1", "2"}, "How many debris to shoot?", "shoot"),
            new RadioOption(new String[]{"Beacon", "Cap ball"}, "Target?", "target"),
            //new NumberOption("Smaller power?", "SmallPower")

    };
}
