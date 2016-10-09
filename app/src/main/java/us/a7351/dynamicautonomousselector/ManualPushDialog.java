package us.a7351.dynamicautonomousselector;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Leo on 10/8/2016.
 */

public class ManualPushDialog {

    Context context;

    public ManualPushDialog(Context context) {
        this.context = context;
    }

    public AlertDialog createDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle("Where is the app running on?");
        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setView(R.layout.choose_push_mode);
        // create alert dialog
        return alertDialogBuilder.create();
    }
}
