package com.example.protocolcompanion.ui.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

public class HomeFragment extends Fragment {

    private File JSONFile;
    private StudyViewModel studyViewModel;
    private RecyclerView recyclerView;
    private MyStudyRecyclerViewAdapter myStudyRecyclerViewAdapter;

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
                studyViewModel.setCurrentStudy(Study.getStudy(itemId));
                studyViewModel.deleteCurrentStudy();
                updateFile();
                // Refresh adapter
                refreshAdapter();
                // Create Toast notification
                text = "Study successfully deleted!";
                toast = Toast.makeText(context, text, duration);
                toast.show();
                return true;
            case R.id.context_email:
                // TODO: create email
                updateFile();
                // Create Toast notification
                text = "Study successfully emailed!";
                toast = Toast.makeText(context, text, duration);
                toast.show();
                return true;
            case R.id.context_send:
                updateFile();
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
    private void updateFile() {
        try {
            FileWriter fileWriter = new FileWriter(JSONFile, false);
            fileWriter.write(studyViewModel.getFullJSONString());
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshAdapter() {
        myStudyRecyclerViewAdapter = new MyStudyRecyclerViewAdapter(Study.ITEMS);
        recyclerView.setAdapter(myStudyRecyclerViewAdapter);
    }

}