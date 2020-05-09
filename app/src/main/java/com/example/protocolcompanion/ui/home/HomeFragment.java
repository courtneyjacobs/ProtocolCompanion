package com.example.protocolcompanion.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protocolcompanion.R;
import com.example.protocolcompanion.Study;
import com.example.protocolcompanion.StudyViewModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

// This defines each element of the study list and determines what to do if the user selects an item
// in the list (go to its detail page), or long presses an item (display the context menu).
public class HomeFragment extends Fragment {

    private File JSONFile;
    private StudyViewModel studyViewModel;
    private RecyclerView recyclerView;

    public HomeFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            refreshAdapter();

            // Register for context menu
            registerForContextMenu(recyclerView);
        }

        studyViewModel = new ViewModelProvider(requireActivity()).get(StudyViewModel.class);
        JSONFile = new File(Objects.requireNonNull(getContext()).getFilesDir(), "protocols.json");

        return view;
    }

    // From context menu docs at https://developer.android.com/guide/topics/ui/menus#FloatingContextMenu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String itemId = String.valueOf(item.getGroupId());
        String position = Study.getItemPosition(itemId);

        // Toast
        Context context = getContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;
        Toast toast;

        switch (item.getItemId()) {
            case R.id.context_edit:
                HomeFragmentDirections.DetailAction action = HomeFragmentDirections.detailAction(position);
                action.setCurrentId(itemId);
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(action);
                return true;
            case R.id.context_delete:
                updateStudyAndFile(itemId, true);

                // Refresh adapter
                refreshAdapter();

                // Create Toast notification
                text = "Study successfully deleted!";
                toast = Toast.makeText(context, text, duration);
                toast.show();
                return true;
            case R.id.context_share:
                updateStudyAndFile(itemId, false);

                // Get info to send
                // TODO: right now it gets the full json string (all studies), if you want it to share only the current study have it just get the currentStudy variable and export it to json
                String studyName = Study.getStudy(itemId).getName();
                String studyContents = studyViewModel.getFullJSONString();

                // Create chooser from tutorialspoint at https://www.tutorialspoint.com/android/android_sending_email.htm
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setData(Uri.parse("mailto:"));
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "[ProtocolCompanion] " + studyName);
                shareIntent.putExtra(Intent.EXTRA_TEXT, studyContents);
                try {
                    startActivity(Intent.createChooser(shareIntent, "Share Study"));
                    Objects.requireNonNull(getActivity()).finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.context_send:
                updateStudyAndFile(itemId, false);

                // Create Toast notification
                text = "Study successfully sent to watch!";
                toast = Toast.makeText(context, text, duration);
                toast.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // Opens file and overwrites changes to the current study
    private void updateStudyAndFile(String id, boolean delete) {
        studyViewModel.setCurrentStudy(Study.getStudy(id));
        studyViewModel.updateCurrentStudy(false);

        if(delete) {
            studyViewModel.deleteCurrentStudy();
        }

        try {
            FileWriter fileWriter = new FileWriter(JSONFile, false);
            fileWriter.write(studyViewModel.getFullJSONString());
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshAdapter() {
        MyStudyRecyclerViewAdapter myStudyRecyclerViewAdapter = new MyStudyRecyclerViewAdapter(Study.ITEMS);
        recyclerView.setAdapter(myStudyRecyclerViewAdapter);
    }

}