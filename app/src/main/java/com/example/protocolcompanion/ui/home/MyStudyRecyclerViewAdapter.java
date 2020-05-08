package com.example.protocolcompanion.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.protocolcompanion.R;
import com.example.protocolcompanion.Study;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

// RecyclerViewAdapter generates the list of studies from the Study model ITEMS map
// and allows the studies to be displayed in a list form on the home page.
public class MyStudyRecyclerViewAdapter extends RecyclerView.Adapter<MyStudyRecyclerViewAdapter.ViewHolder>{

    private final List<Study> mValues;

    @RequiresApi(api = Build.VERSION_CODES.N)
    MyStudyRecyclerViewAdapter(HashMap<String, Study> items) {
        mValues = new ArrayList<>(Study.ITEMS.values());
        Collections.sort(mValues, new Comparator<Study>() {
            @Override
            public int compare(Study s1, Study s2) {
                if (s1.getId() == null || s2.getId() == null) {
                    return 0;
                }
                return s1.getId().compareTo(s2.getId());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home, parent, false);
        return new ViewHolder(view);
    }

    public void refreshAdapter(){
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(holder.mItem.getId());
        holder.mContentView.setText(mValues.get(position).getName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.DetailAction action = HomeFragmentDirections.detailAction(holder.mItem.getId());
                action.setCurrentId(holder.mItem.getId());
                Navigation.findNavController(v).navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener  {
        final View mView;
        //final TextView mIdView;
        final TextView mContentView;
        Study mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
            view.setOnCreateContextMenuListener(this);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        // from SO answer at https://stackoverflow.com/a/27752974/13083182
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Study Actions");
            contextMenu.add(Integer.parseInt(mItem.getId()), R.id.context_edit, 0, "Edit Study");//groupId, itemId, order, title
            contextMenu.add(Integer.parseInt(mItem.getId()), R.id.context_delete, 0, "Delete Study");
            contextMenu.add(Integer.parseInt(mItem.getId()), R.id.context_share, 0, "Share Study");
            contextMenu.add(Integer.parseInt(mItem.getId()), R.id.context_send, 0, "Send Study to Watch");
        }
    }
}
