package UserViewHolder.example.hp.shoppersstop.database;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.hp.shoppersstop.database.ProductContract.ProductEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.hp.shoppersstop.database.ProductContract.ProductEntry.TABLE_NAME;

/**
 * Created by hp on 10-03-2018.
 */

public class ProductDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "ProductDbHelper";
    private static final int DATABASE_VERSION = 1;

    private final Context mContext;
    private boolean mNeedUpdate = false;

    private SQLiteDatabase mDataBase;
    private static String DB_PATH = "";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE " + ProductContract.ProductEntry.TABLE_NAME;

   public ProductDbHelper(Context context) {

    super(context , ProductContract.ProductEntry.Database_Name,null, DATABASE_VERSION);

           DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        Log.i(TAG, "path:" + context.getApplicationInfo().dataDir);
       this.mContext = context;
        try {
            copyDataBase();
        } catch(Exception e) {

            Log.i(TAG, "1Error:" + e);
        }
       this.getReadableDatabase();
   }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + ProductContract.ProductEntry.Database_Name);
            if (dbFile.exists())
                dbFile.delete();

            try {
                copyDataBase();
            } catch(Exception e) {

                Log.i(TAG, "2Error:" + e);
            }
            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + ProductContract.ProductEntry.Database_Name);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    public void openDatabase() {

       String path = mContext.getDatabasePath(ProductContract.ProductEntry.Database_Name).getPath();

       if(mDataBase != null && mDataBase.isOpen()) {
           return;
       }

       mDataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

    }

    public List getList() {
       List<String> list = new ArrayList<>();
        openDatabase();

        Cursor cursor = mDataBase.rawQuery("SELECT * FROM product", null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {

            list.add(cursor.getString(1));

            cursor.moveToNext();
        }
            cursor.close();
        closeDatabase();

        return list;
    }

    public void closeDatabase() {
       if(mDataBase != null) {
           mDataBase.close();
       }
    }

    public Cursor getProduct(String id){


        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(ProductContract.ProductEntry.TABLE_NAME);

        Cursor c = queryBuilder.query(this.getReadableDatabase(),
                new String[] { "_id", ProductContract.ProductEntry.PRODUCT_NAME } ,
                ProductContract.ProductEntry.PRODUCT_NAME + " = ?", new String[] { id } , null, null, null ,null
        );

        return c;
    }

    public Cursor getProducts(String[] selectionArgs){

        String selection = ProductContract.ProductEntry.PRODUCT_NAME + " like ? ";

        if(selectionArgs!=null){
            selectionArgs[0] = "%"+selectionArgs[0] + "%";
        }

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
       // queryBuilder.setProjectionMap(mAliasMap);

        queryBuilder.setTables(ProductContract.ProductEntry.TABLE_NAME);

        Cursor c = queryBuilder.query(this.getReadableDatabase(),
                new String[] { "_id", ProductContract.ProductEntry.PRODUCT_NAME} ,
                selection,
                selectionArgs,
                null,
                null,
                 null,"10"
        );
        return c;
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(ProductContract.ProductEntry.Database_Name);
        OutputStream mOutput = new FileOutputStream(DB_PATH + ProductContract.ProductEntry.Database_Name);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + ProductContract.ProductEntry.Database_Name, null, SQLiteDatabase.OPEN_READONLY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }
}

