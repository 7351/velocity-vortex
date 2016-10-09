package us.a7351.dynamicautonomousselector;

class UserOptions {
    /**
     * This is the input for the programmers to enter all options
     */
    static final Option[] options = new Option[]{
            /**
             * The way to have the drive team enter number values like wait time, prox values, etc...
             * is in the following format:
             * new NumberOption("Title of the option", "hashMap key of the option), <- dont forget the comma if it isnt thelast one
             * @see NumberOption
            */
            new NumberOption("Prox minimum", "proxkey"),
            new RadioOption(new String[]{"Kentucky", "Hawaii", "Mountain", "Center vortex"}, "Target", "target"),
            new RadioOption(new String[]{"Red", "Blue"}, "Alliance", "alliance"),
            new NumberOption("Distance", "distancekey")
    };
}
