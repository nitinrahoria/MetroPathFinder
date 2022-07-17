package nitin.rahoria.metropathfinder.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import nitin.rahoria.metropathfinder.R;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    TextView mResultTV, mDistanceTV;
    Spinner mSrcSPN, mDestSPN;
    Button mCalculateBtn;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.initModel();
        mResultTV = view.findViewById(R.id.result);
        mDistanceTV = view.findViewById(R.id.distance_tv);
        mSrcSPN = view.findViewById(R.id.source_spinner);
        mDestSPN = view.findViewById(R.id.destination_spinner);
        mSrcSPN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mViewModel.src = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mDestSPN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mViewModel.dest = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mCalculateBtn = view.findViewById(R.id.calculate_button);
        mCalculateBtn.setOnClickListener( v -> handleCalculateClick());
        mViewModel.result.observe(getViewLifecycleOwner(), result -> updateResult(result));
        mViewModel.distance.observe(getViewLifecycleOwner(), dist -> updateDistance(dist));
        mViewModel.setStationArray(getResources().getStringArray(R.array.station_name_array));
    }

    private void updateDistance(Integer dist) {
        if (dist < 0) {
            mDistanceTV.setText(R.string.no_path_error);
        } else if (dist == 0) {
            mDistanceTV.setText(R.string.source_and_destination_same_error);
        } else {
            mDistanceTV.setText(getString(R.string.distance_text, dist));
        }
    }

    private void handleCalculateClick() {
        mResultTV.setVisibility(View.GONE);
        mViewModel.findShortestDistance();
    }

    private void updateResult(String result) {
        mResultTV.setVisibility(View.VISIBLE);
        mResultTV.setText(result);
    }

}