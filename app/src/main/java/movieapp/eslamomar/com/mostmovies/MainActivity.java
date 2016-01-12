package movieapp.eslamomar.com.mostmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment) != null) {

            mTwoPane = true;
            if (savedInstanceState == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.fragment, new Movie_detailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        }else
        {

            mTwoPane = false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void loadItem(Movie movie) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable("movies_details", movie);

            Movie_detailFragment fragment = new Movie_detailFragment();
            fragment.setArguments(args);

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, Movie_detail.class)
                    .putExtra("movies_details", movie);
            startActivity(intent);
        }
    }
}
