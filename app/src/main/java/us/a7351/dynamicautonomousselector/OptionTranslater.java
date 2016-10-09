package us.a7351.dynamicautonomousselector;

import android.content.Context;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * The purpose of OptionTranslater is to translate Options from UserOptions into a full layout and inflate it for the user to input selections
 */
class OptionTranslater {

    private LinearLayout layout;

    private Context context;
    private LayoutInflater inflater;
    private final static String LOG_TAG = "OptionTranslater";

    OptionTranslater(LinearLayout layout, Context context) {
        this.layout = layout;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     *
     * @param allOptions takes the Options in a List format, to format an array to list use Arrays.as
     */
    void show(List<Option> allOptions) {
        for (int i = 0; i < allOptions.size(); i++) {
            // We iterate though the list with an unspecified Option type
            Option genericOption = allOptions.get(i);

            // Create a new Textview fromt he template R.layout.title_text_view and fill it with data of
            TextView titleTextView = (TextView) inflater.inflate(R.layout.title_text_view, layout, false);
            titleTextView.setText(genericOption.OptionTitle);

            layout.addView(titleTextView);

            if (genericOption.getType().equals("Number")) {
                NumberOption currentNumberOption = (NumberOption) genericOption;

                EditText numberEditText = (EditText) inflater.inflate(R.layout.number_option, layout, false);

                numberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(currentNumberOption.MaxNumber)});

                currentNumberOption.editText = numberEditText;

                Log.d(LOG_TAG, String.valueOf("Number object created at ID " + i));

                layout.addView(numberEditText);


            }
            if (genericOption.getType().equals("Radio")) {
                RadioOption currentRadioOption = (RadioOption) genericOption;

                currentRadioOption.radioButtonList = new ArrayList<>();

                RadioGroup radioGroup = (RadioGroup) inflater.inflate(R.layout.radio_group_option, layout, false);
                for (int c = 0; c < currentRadioOption.availableOptions.length; c++) {
                    String currentSelection = currentRadioOption.availableOptions[c];
                    RadioButton currentRadioButton = (RadioButton) inflater.inflate(R.layout.radio_button_option, radioGroup, false);
                    currentRadioButton.setText(currentSelection);
                    radioGroup.addView(currentRadioButton);
                    currentRadioOption.radioButtonList.add(currentRadioButton);
                }

                currentRadioOption.radioGroup = radioGroup;

                Log.d(LOG_TAG, String.valueOf("Radio object created at ID " + i));

                layout.addView(radioGroup);


            }

            if (i != allOptions.size() - 1) {
                View dividerView = new View(context);
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1);
                dividerView.setLayoutParams(dividerParams);
                dividerView.setBackgroundColor(context.getResources().getColor(R.color.dividerColor));

                layout.addView(dividerView);
            }


        }
    }
}
