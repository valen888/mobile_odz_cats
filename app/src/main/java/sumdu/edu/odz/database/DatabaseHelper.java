package sumdu.edu.odz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cat_directory.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_CATS = "cats";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BREED_NAME = "breed_name";
    public static final String COLUMN_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_CATS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_BREED_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(createTableQuery);

        insertInitialCats(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATS);
        onCreate(db);
    }

    private void insertInitialCats(SQLiteDatabase db) {
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_CATS;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int rowCount = cursor.getInt(0);
        cursor.close();

        if (rowCount == 0) {
            ContentValues catValues0 = new ContentValues();
            catValues0.put(COLUMN_BREED_NAME, "British Shorthair");
            catValues0.put(COLUMN_DESCRIPTION, "The British Shorthair is a medium to large-sized breed of cat, with a chunky build and dense coat.");

            ContentValues catValues1 = new ContentValues();
            catValues1.put(COLUMN_BREED_NAME, "Siamese");
            catValues1.put(COLUMN_DESCRIPTION, "The Siamese is a breed of domestic cat that originated in Thailand (formerly known as Siam).");

            ContentValues catValues2 = new ContentValues();
            catValues2.put(COLUMN_BREED_NAME, "Maine Coon");
            catValues2.put(COLUMN_DESCRIPTION, "The Maine Coon is one of the largest domesticated cat breeds and has a distinctive physical appearance.");

            ContentValues catValues3 = new ContentValues();
            catValues3.put(COLUMN_BREED_NAME, "Persian");
            catValues3.put(COLUMN_DESCRIPTION, "The Persian cat is a long-haired breed of cat characterized by its round face and shortened muzzle.");

            ContentValues catValues4 = new ContentValues();
            catValues4.put(COLUMN_BREED_NAME, "Bengal");
            catValues4.put(COLUMN_DESCRIPTION, "The Bengal cat is a domesticated breed created from hybrids of domestic cats and the Asian leopard cat.");

            ContentValues catValues5 = new ContentValues();
            catValues5.put(COLUMN_BREED_NAME, "Ragdoll");
            catValues5.put(COLUMN_DESCRIPTION, "The Ragdoll is a cat breed with blue eyes and a distinct colorpoint coat.");

            ContentValues catValues6 = new ContentValues();
            catValues6.put(COLUMN_BREED_NAME, "Sphynx");
            catValues6.put(COLUMN_DESCRIPTION, "The Sphynx is a breed of cat known for its lack of coat (fur), though it is not truly hairless.");

            ContentValues catValues7 = new ContentValues();
            catValues7.put(COLUMN_BREED_NAME, "Scottish Fold");
            catValues7.put(COLUMN_DESCRIPTION, "The Scottish Fold is a breed of domestic cat with a natural dominant-gene mutation.");

            ContentValues catValues8 = new ContentValues();
            catValues8.put(COLUMN_BREED_NAME, "Russian Blue");
            catValues8.put(COLUMN_DESCRIPTION, "The Russian Blue is a cat breed that has a silver-blue coat. They are known for their friendliness.");

            ContentValues catValues9 = new ContentValues();
            catValues9.put(COLUMN_BREED_NAME, "Norwegian Forest");
            catValues9.put(COLUMN_DESCRIPTION, "The Norwegian Forest cat is a breed of domestic cat originating in Northern Europe.");

            db.insert(TABLE_CATS, null, catValues0);
            db.insert(TABLE_CATS, null, catValues1);
            db.insert(TABLE_CATS, null, catValues2);
            db.insert(TABLE_CATS, null, catValues3);
            db.insert(TABLE_CATS, null, catValues4);
            db.insert(TABLE_CATS, null, catValues5);
            db.insert(TABLE_CATS, null, catValues6);
            db.insert(TABLE_CATS, null, catValues7);
            db.insert(TABLE_CATS, null, catValues8);
            db.insert(TABLE_CATS, null, catValues9);
        }
    }
}
