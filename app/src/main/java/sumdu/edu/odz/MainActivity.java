package sumdu.edu.odz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import sumdu.edu.odz.model.Cat;

public class MainActivity extends AppCompatActivity implements CatListFragment.OnCatSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceFragment(new CatListFragment());
    }

    @Override
    public void onCatSelected(Cat cat) {
        CatDetailsFragment detailsFragment = CatDetailsFragment.newInstance(cat.getBreedName(),cat.getDescription());

        replaceFragment(detailsFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
