package com.example.hp.shoppersstop;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.shoppersstop.database.ProductDbHelper;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.example.hp.shoppersstop.database.ProductContract.ProductEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends  AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ListItem>>,
        RecyclerView.OnItemTouchListener, View.OnClickListener, ActionMode.Callback{

    public static final String UID = "USER_ID";

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "MainActivity";
    private static final String KEY = "KEY";

    private String[] mListTitles;
    ArrayList<DrawerList> arrayList;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private Toolbar toolbar;

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    public FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    public FirebaseUser firebaseUser;

    private TextView userName, userID;
    private ImageView userProfile;

    private SQLiteDatabase sqLiteDatabase;
    private ProductDbHelper productDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Shimmer effect...
//Making database object...

        productDbHelper = new ProductDbHelper(this);

        Toast.makeText(this, "Welcomes U!", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "hello Im working!");
        //Firebase OAuth starts here...
        mAuth = FirebaseAuth.getInstance();

        final List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), new AuthUI.IdpConfig.FacebookBuilder().build(), new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

        //Firstly Iam creating an Instance of database this is the User's Id.

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //SearchView coding...
        ImageButton searchView = toolbar.findViewById(R.id.search_button);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        //mListTitles = getResources().getStringArray(R.array.navigation_list);
        arrayList = getArrayList();

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.list_view);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList.setAdapter(new DrawerAdapter(this, arrayList));

        //Action Bar Drawer Toggle

        mDrawerToggle = getActionBarDrawerToggle();

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        try {

            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        } catch (Exception e) {
            Log.i(TAG, "3Exception:" + e);
        }
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                switch (position) {
                    case 0:
                        getActionBarDrawerToggle();
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, ShopsCategoryActivity.class));
                        break;
                    case 2:

                        break;

                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:

                        break;
                    case 7:
                        startActivity(new Intent(MainActivity.this, PendingOrderActivity.class));
                        break;
                    case 8:
                        startActivity(new Intent(MainActivity.this, Customer_Profile_Main_Page.class));
                        break;
                    case 9:
                        startActivity(new Intent(MainActivity.this, ImproveUsActivity.class));
                        break;
                    case 10:
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        break;
                    case 11:
                        startActivity(new Intent(MainActivity.this, TermsAndConditions.class));

                        break;
                    case 12:

                        if(mAuth.getUid() != null) {
                            AuthUI.getInstance()
                                    .signOut(MainActivity.this)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // ...
                                            Log.i(TAG, "Log out:" + position);
                                            Toast.makeText(MainActivity.this, "Position:" + position, Toast.LENGTH_SHORT).show();
                                            signIn();

                                        }
                                    });
                        }
                        break;
                }
            }
        });


        //Navigation Drawer Button
        ImageButton navButton = toolbar.findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });

        if (mAuth.getCurrentUser() != null) {
            updateUI(mAuth.getCurrentUser());
        }

        //Query data from database which is readable...
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
              //  Log.i(TAG, "OnCreateResult:" + firebaseUser.getUid());
                // ...
            } else {

                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                // Sign in failed, check response for error code
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //Check if user is signed in and update UI accordingly.
        firebaseUser = mAuth.getCurrentUser();
       if(mAuth.getCurrentUser() != null)
        Log.w(TAG, "oncreate4:" + firebaseUser.getUid());
          //  mFirebaseDatabase = FirebaseDatabase.getInstance();
            //mDatabaseReference = mFirebaseDatabase.getReference("" + firebaseUser.getUid());

        updateUI(firebaseUser);
    }

    //Below method updates UI by the valid user login
    public void updateUI(FirebaseUser currentUser){

        if(currentUser != null) {
            userName = findViewById(R.id.name_id);
            userID = findViewById(R.id.email_id);
            userProfile = findViewById(R.id.user_profile);

            userName.setText(currentUser.getDisplayName());
            userID.setText(currentUser.getEmail());
            Log.w(TAG, "UpdateUI:" + currentUser.getUid());
            //userProfile.setImageDrawable(Drawable.createFromPath(currentUser.getPhotoUrl().toString()));
        }
        else {
            Log.w(TAG, "Cannot update UI");
        }

    }

    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //Sign in successful, update UI with the signed-in user's info
                    Log.i(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void signIn (String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                     firebaseUser = mAuth.getCurrentUser();
                    updateUI(firebaseUser);
                }
                else {
                    Log.w (TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void getCurrentUser() {
        firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null) {
            String name = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();
            Uri photoUri = firebaseUser.getPhotoUrl();

            boolean emailVerified = firebaseUser.isEmailVerified();
            String uid = firebaseUser.getUid();
        }
    }

    public void getInputList(View view) {
        firebaseUser = mAuth.getCurrentUser();
        Intent intent = new Intent(MainActivity.this, EnterList.class);
        intent.putExtra(UID, firebaseUser.getUid());
        startActivity(intent);
    }

    private ActionBarDrawerToggle getActionBarDrawerToggle(){
        return new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                try {
                    getActionBar().setTitle(mTitle);
                } catch (NullPointerException e) {
                    Log.i (TAG, " 1Exception :" + e);
                }
                invalidateOptionsMenu();
            }

            public void onDrawerOpen(View view) {
                super.onDrawerClosed(view);
                try{
                    getActionBar().setTitle(mDrawerTitle);
                } catch (Exception e) {
                    Log.i( TAG, "2Exception:" + e);
                }
                invalidateOptionsMenu();
            }
        };
    }

    private ArrayList<DrawerList> getArrayList(){

        ArrayList arrayList = new ArrayList<DrawerList>();
        arrayList.add(new DrawerList("Home",getDrawable(R.drawable.ic_home)));
        arrayList.add(new DrawerList("Shop by Category",null));
        arrayList.add(new DrawerList("Notifications",getDrawable(R.drawable.ic_bell)));
        arrayList.add(new DrawerList("Offer Zone",null));
        arrayList.add(new DrawerList("My Rewards",getDrawable(R.drawable.ic_trophy)));
        arrayList.add(new DrawerList("My Cart",getDrawable(R.drawable.ic_cart)));
        arrayList.add(new DrawerList("My Wish List",getDrawable(R.drawable.ic_heart)));
        arrayList.add(new DrawerList("My Orders", null));
        arrayList.add(new DrawerList("My Account", getDrawable(R.drawable.ic_account_circle_grey_36dp)));
        arrayList.add(new DrawerList("Send Feedback",getDrawable(R.drawable.ic_comment)));
        arrayList.add(new DrawerList("About Us", getDrawable(R.drawable.ic_help)));
        arrayList.add(new DrawerList("Legal", null));
        arrayList.add(new DrawerList("Log Out",null));

        return arrayList;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<ListItem>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<ListItem>> loader, List<ListItem> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<ListItem>> loader) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void onClick(View v) {

    }

}



