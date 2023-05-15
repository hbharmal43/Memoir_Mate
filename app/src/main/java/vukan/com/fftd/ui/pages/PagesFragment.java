package vukan.com.fftd.ui.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import vukan.com.fftd.R;
import vukan.com.fftd.EventsActivity;
import vukan.com.fftd.FamilyAncestryActivity;
import vukan.com.fftd.FriendHistoryActivity;
import vukan.com.fftd.TravelActivity;

public class PagesFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pages, container, false);

        Button familyAncestryButton = view.findViewById(R.id.familyButton);
        familyAncestryButton.setOnClickListener(this);

        Button friendHistoryButton = view.findViewById(R.id.friendsButton);
        friendHistoryButton.setOnClickListener(this);

        Button eventsButton = view.findViewById(R.id.eventsButton);
        eventsButton.setOnClickListener(this);

        Button travelButton = view.findViewById(R.id.travelButton);
        travelButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.familyButton:
                Intent intent = new Intent(getActivity(), FamilyAncestryActivity.class);
                startActivity(intent);
                break;
            case R.id.friendsButton:
                Intent intent1 = new Intent(getActivity(), FriendHistoryActivity.class);
                startActivity(intent1);
                break;
            case R.id.eventsButton:
                Intent intent2 = new Intent(getActivity(), EventsActivity.class);
                startActivity(intent2);
                break;
            case R.id.travelButton:
                Intent intent3 = new Intent(getActivity(), TravelActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
