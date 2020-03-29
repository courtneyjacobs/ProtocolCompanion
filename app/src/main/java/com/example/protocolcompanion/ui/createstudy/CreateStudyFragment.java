package com.example.protocolcompanion.ui.createstudy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.protocolcompanion.R;
import com.example.protocolcompanion.StudyViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

public class CreateStudyFragment extends Fragment {

    private StudyViewModel studyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        studyViewModel = new ViewModelProvider(requireActivity()).get(StudyViewModel.class);

        View root = inflater.inflate(R.layout.fragment_createstudy, container, false);

        // Get elements
        final Switch GPSSwitch = root.findViewById(R.id.GPSSwitch);
        final Switch accelerationSwitch = root.findViewById(R.id.accelerationSwitch);
        final Switch HRSwitch = root.findViewById(R.id.HRSwitch);
        final EditText regionText = root.findViewById(R.id.regionText);
        final EditText bucketText = root.findViewById(R.id.bucketText);
        final EditText folderText = root.findViewById(R.id.folderText);
        Button saveButton = root.findViewById(R.id.saveNewStudyButton);

        // region
        studyViewModel.getText("region").observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                regionText.setText(s);
            }
        });
        // bucket
        studyViewModel.getText("bucket").observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                bucketText.setText(s);
            }
        });
        // folder
        studyViewModel.getText("folder").observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                folderText.setText(s);
            }
        });
        // gps
        studyViewModel.getSwitch("gps").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    GPSSwitch.setChecked(b);
                }
            }
        });
        // acceleration
        studyViewModel.getSwitch("acceleration").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    accelerationSwitch.setChecked(b);
                }
            }
        });
        // hr
        studyViewModel.getSwitch("hr").observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if(null != b) {
                    HRSwitch.setChecked(b);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // TODO: this overwrites all objects. find a way to edit the json.
                File exportFile = new File(Objects.requireNonNull(getContext()).getFilesDir(), "protocols.json");
                String nextID = "0";

                // Get next available id by reading JSON and finding max id // TODO: is this the best way?
                try {
                    JSONObject fileJSON = new JSONObject(String.valueOf(exportFile));
                    int maxId = 0;
                    Iterator keys = fileJSON.keys();
                    while(keys.hasNext()) {
                        String currentKey = (String) keys.next();
                        System.out.println("key: " + currentKey);
                        int currentID = Integer.parseInt(currentKey);
                        if (currentID > maxId) {
                            maxId = currentID;
                        }
                    }
                    nextID = String.valueOf(++maxId);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                // Set appropriate values in VM
                studyViewModel.setText("id", nextID);
                studyViewModel.setText("region", regionText.getText().toString());
                studyViewModel.setText("bucket", bucketText.getText().toString());
                studyViewModel.setText("folder", folderText.getText().toString());
                studyViewModel.setSwitch("gps", GPSSwitch.isChecked());
                studyViewModel.setSwitch("acceleration", accelerationSwitch.isChecked());
                studyViewModel.setSwitch("hr", HRSwitch.isChecked());

                // Open file and overwrite changes
                try {
                    JSONObject newStudyJSONObject = studyViewModel.exportJSON();
                    studyViewModel.fullJSONObject.put(nextID, newStudyJSONObject);
                    FileWriter fileWriter = new FileWriter(exportFile, false);
                    System.out.println(studyViewModel.fullJSONObject.toString()); // TODO: remove
                    fileWriter.write(studyViewModel.fullJSONObject.toString());
                    fileWriter.close();
                    Context context = getContext();
                    CharSequence text = "Study successfully created!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
                catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }
}