package us.a7351.dynamicautonomousselector;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import us.a7351.dynamicautonomousselector.autonomousgenerator.Program;
import us.a7351.dynamicautonomousselector.autonomousgenerator.Routine;

import static us.a7351.dynamicautonomousselector.MainActivity.hoster;

public class AutonomousGeneratorFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = AutonomousGeneratorFragment.class.getName();

    CoordinatorLayout coordinatorLayout;
    Context context;
    RecyclerView recyclerView;
    Spinner programSpinner;
    File programDir;
    HashMap<File, Program> programMap;
    File selectedFile;

    public AutonomousGeneratorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_autonomous_generator, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();

        if (v != null) {

            try {
                v.findViewById(R.id.new_program_button).setOnClickListener(this);
                programSpinner = (Spinner) v.findViewById(R.id.programSpinner);
                programSpinner.setOnItemSelectedListener(this);
                coordinatorLayout = (CoordinatorLayout) v.getParent().getParent().getParent();
                recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_auto_generator);
                Snackbar.make(coordinatorLayout, "This is experimental!", Snackbar.LENGTH_SHORT).show();
                programDir = new File(Environment.getExternalStorageDirectory() + File.separator + "FIRST" + File.separator + "Programs" + File.separator);
                programDir.mkdirs();

                repopulateMapAndSpinner();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void repopulateMapAndSpinner() throws Exception {
        programMap = new HashMap<>();

        List<String> programs = new ArrayList<>();
        programs.add(0, "---");
        for (File file: programDir.listFiles()) {
            String fileContent = Files.toString(file, Charsets.UTF_8);
            try {
                new JSONObject(fileContent);
                Program program = new Gson().fromJson(fileContent, Program.class);
                if (program.type.equals("Program")) {
                    Log.d(TAG, "Program discovered: " + file.getName());
                    programs.add(file.getName());
                    programMap.put(file, program);
                } else {
                    Log.d(TAG, "Found an object, but it doesn't match the 'Program' type");
                }
            } catch (JSONException e) {
                Log.d(TAG, "Discovered " + file.getName() + ", but it isn't a program!");
            }
        }
        ArrayAdapter<String> programAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, programs);
        programSpinner.setAdapter(programAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Get all layout objects and create translater to inject the options to the LinearLayout
        this.context = context;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (view != null) {
            String fileName = ((TextView) view).getText().toString();
            selectedFile = new File(programDir.getPath() + File.separator + fileName);
            Log.d(TAG, selectedFile.getName() + " selected!");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_program_button:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.new_file_dialog_layout, new LinearLayout(context));
                alertDialog.setView(view);
                alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText fileNameEditText = (EditText) view.findViewById(R.id.file_name_edittext);
                        Log.d(TAG, fileNameEditText.getText().toString());
                    }
                });
                alertDialog.create().show();
                break;
        }
    }
}
