package sumdu.edu.odz;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import sumdu.edu.odz.database.DatabaseHelper;
import sumdu.edu.odz.model.Cat;

public class CatListFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private ListView listViewCats;
    private List<Cat> catsList;
    private CatListAdapter adapter;
    private OnCatSelectedListener listener;
    private EditText editTextSearch;
    private List<Cat> allCatsList;

    public interface OnCatSelectedListener {
        void onCatSelected(Cat cat);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCatSelectedListener) {
            listener = (OnCatSelectedListener) context;
        } else {
            throw new ClassCastException(context + " must implement OnCatSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cat_list, container, false);

        listViewCats = rootView.findViewById(R.id.listViewCats);
        editTextSearch = rootView.findViewById(R.id.editTextSearch);

        databaseHelper = new DatabaseHelper(requireContext());
        allCatsList = new ArrayList<>();
        catsList = new ArrayList<>();
        adapter = new CatListAdapter(catsList);

        listViewCats.setAdapter(adapter);

        listViewCats.setOnItemClickListener((parent, view, position, id) -> {
            Cat selectedCat = catsList.get(position);
            listener.onCatSelected(selectedCat);
        });

        listViewCats.setOnItemLongClickListener((parent, view, position, id) -> {
            Cat selectedCat = catsList.get(position);
            showOptionsDialog(selectedCat);

            return true;
        });

        Button addCatButton = rootView.findViewById(R.id.addCatButton);
        addCatButton.setOnClickListener(v -> showAddDialog());

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().trim();
                filterCats(searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loadCats();

        return rootView;
    }

    private void filterCats(String searchText) {
        catsList.clear();

        for (Cat cat : allCatsList) {
            if (cat.getBreedName().toLowerCase().contains(searchText.toLowerCase())) {
                catsList.add(cat);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void loadCats() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CATS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String breedName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BREED_NAME));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));

                Cat cat = new Cat(id, breedName, description);

                catsList.add(cat);
                allCatsList.add(cat);
            } while (cursor.moveToNext());
        }

        cursor.close();

        adapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_cat, null);

        builder.setView(dialogView);
        builder.setTitle("Add Breed");

        EditText editTextBreedName = dialogView.findViewById(R.id.editTextBreedName);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String breedName = editTextBreedName.getText().toString();
            String description = editTextDescription.getText().toString();

            if (!breedName.isEmpty()) {
                addCat(breedName, description);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addCat(String breedName, String description) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_BREED_NAME, breedName);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);

        long id = db.insert(DatabaseHelper.TABLE_CATS, null, values);

        if (id != -1) {
            Cat cat = new Cat((int) id, breedName, description);

            catsList.add(cat);
            allCatsList.add(cat);

            adapter.notifyDataSetChanged();

            Toast.makeText(requireContext(), "Breed added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Failed to add breed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOptionsDialog(final Cat cat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Options");

        String[] options = {"Edit", "Delete"};

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    showEditDialog(cat);
                    break;
                case 1:
                    showDeleteDialog(cat);
                    break;
            }
        });

        builder.show();
    }

    private void updateCat(Cat cat) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_BREED_NAME, cat.getBreedName());
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, cat.getDescription());

        int rowsAffected = db.update(DatabaseHelper.TABLE_CATS, values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(cat.getId())});

        if (rowsAffected > 0) {
            adapter.notifyDataSetChanged();

            Toast.makeText(requireContext(), "Cat updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Failed to update cat", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCat(Cat cat) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int rowsAffected = db.delete(DatabaseHelper.TABLE_CATS,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(cat.getId())});

        if (rowsAffected > 0) {
            catsList.remove(cat);
            allCatsList.remove(cat);

            adapter.notifyDataSetChanged();

            Toast.makeText(requireContext(), "Cat deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Failed to delete cat", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditDialog(final Cat cat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_cat, null);

        builder.setView(dialogView);
        builder.setTitle("Edit Cat");

        EditText editTextBreedName = dialogView.findViewById(R.id.editTextBreedName);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);

        editTextBreedName.setText(cat.getBreedName());
        editTextDescription.setText(cat.getDescription());

        builder.setPositiveButton("Save", (dialog, which) -> {
            String breedName = editTextBreedName.getText().toString();
            String description = editTextDescription.getText().toString();

            if (!breedName.isEmpty()) {
                cat.setBreedName(breedName);
                cat.setDescription(description);

                updateCat(cat);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteDialog(final Cat cat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle("Delete Cat");
        builder.setMessage("Are you sure you want to delete this cat?");

        builder.setPositiveButton("Delete", (dialog, which) -> deleteCat(cat));

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class CatListAdapter extends ArrayAdapter<Cat> {
        public CatListAdapter(List<Cat> cats) {
            super(requireContext(), android.R.layout.simple_list_item_1, cats);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(requireContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            TextView textViewBreedName = convertView.findViewById(android.R.id.text1);
            Cat cat = getItem(position);
            textViewBreedName.setText(cat.getBreedName());

            return convertView;
        }
    }
}
