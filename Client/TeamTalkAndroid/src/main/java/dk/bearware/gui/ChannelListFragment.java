package dk.bearware.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChannelListFragment extends Fragment {

    private ChannelListAdapter channelsAdapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Pakai layout yang sudah ada
        View view = inflater.inflate(R.layout.fragment_main_channels, container, false);
        
        // Sesuaikan ID dengan xml kamu: listChannels
        listView = view.findViewById(R.id.listChannels);
        
        // Ambil adapter dari MainActivity
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            channelsAdapter = mainActivity.getChannelsAdapter();
            listView.setAdapter(channelsAdapter);
        }
        
        return view;
    }

    public void refreshList() {
        if (channelsAdapter != null) {
            channelsAdapter.notifyDataSetChanged();
        }
    }
}
