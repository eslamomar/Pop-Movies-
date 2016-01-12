package movieapp.eslamomar.com.mostmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private final String STORED_MOVIES = "stored_movies";
    private SharedPreferences prefs;
    private String sortOrder;
    private AdapterGridMovies mMoviePosterAdapter;
    private ArrayList<Movie> movies = new ArrayList<Movie>();

    public interface Callback {
        public void loadItem(Movie movie);
    }


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }
    //______________________________________
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
       sortOrder = prefs.getString(getString(R.string.display_preferences_sort_order_key),
                getString(R.string.display_preferences_sort_default_value));

        if(savedInstanceState != null){
            ArrayList<Movie> storedMovies = new ArrayList<Movie>();
            storedMovies = savedInstanceState.<Movie>getParcelableArrayList(STORED_MOVIES);
            movies.clear();
            movies.addAll(storedMovies);
        }
    }


    //__________________________________________________________________________-
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        mMoviePosterAdapter = new AdapterGridMovies(
                getActivity(),
                R.layout.film_image,
                R.id.list_item_poster_imageview,
                new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.main_movie_grid);
        gridView.setAdapter(mMoviePosterAdapter);

       /* gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie details = movies.get(position);
                Intent intent = new Intent(getActivity(), Movie_detail.class)
                        .putExtra("movies_details", details);
                startActivity(intent);
            }

        });*/
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Movie details = movies.get(position);

                ((Callback) getActivity())
                        .loadItem(details);

            }

        });

        return rootView;
    }


    /*
    * call getMovies to kick of async task. Asynk task now handles if it should call
    * the API or not
    * */
    @Override
    public void onStart() {
        super.onStart();

        // get preferences to check sort order
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String checkSortOrder = prefs.getString(getString(R.string.display_preferences_sort_order_key),
                getString(R.string.display_preferences_sort_default_value));


        boolean sortOrderChange = !checkSortOrder.equals(sortOrder);
        sortOrder = checkSortOrder;


        if(movies.size() == 0 || sortOrderChange || sortOrder.equals("favorites")){
            getMovies();
        }else{
            // else load what's already in memory
            mMoviePosterAdapter.clear();
            for(Movie movie : movies) {
                mMoviePosterAdapter.add(movie.getPoster());
            }
        }

    }


    /*
    * On save instance state. Creates a parcable array of all current received from the API
    * */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STORED_MOVIES, movies);
    }


    /*
    * kicks off async task to get movies for the main movie list UI
     */
    private void getMovies() {

        // fetch the movies from the API, or it will get favorites from the DB
        AskForMovies fetchMoviesTask = new AskForMovies(getActivity(),
                movies, mMoviePosterAdapter, sortOrder);

        fetchMoviesTask.execute();

    }

}
