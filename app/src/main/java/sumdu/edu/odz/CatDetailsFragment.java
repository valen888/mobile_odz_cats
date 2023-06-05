package sumdu.edu.odz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CatDetailsFragment extends Fragment {
    private static final String ARG_BREED_NAME = "breedName";
    private static final String ARG_DESCRIPTION = "description";
    private String breedName;
    private String description;

    public static CatDetailsFragment newInstance(String breedName, String description) {
        CatDetailsFragment fragment = new CatDetailsFragment();
        Bundle args = new Bundle();

        args.putString(ARG_BREED_NAME, breedName);
        args.putString(ARG_DESCRIPTION, description);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            breedName = getArguments().getString(ARG_BREED_NAME);
            description = getArguments().getString(ARG_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cat_details, container, false);

        Button backButton = view.findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        TextView breedNameTextView = view.findViewById(R.id.breedNameTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);

        breedNameTextView.setText(breedName);
        descriptionTextView.setText(description);

        return view;
    }
}
