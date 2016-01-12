package movieapp.eslamomar.com.mostmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import movieapp.eslamomar.com.mostmovies.data.PopularMovieContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class Movie_detailFragment extends Fragment {
    private final String LOG_TAG = Movie_detailFragment.class.getSimpleName();
    Movie movie;
    private View rootView;
    private LayoutInflater mLayoutInflater;
    public Movie_detailFragment() {

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayoutInflater = inflater;

        Bundle arguments = getArguments();
        Intent intent = getActivity().getIntent();


        if(arguments != null || intent != null && intent.hasExtra("movies_details")){

            rootView = mLayoutInflater.inflate(R.layout.fragment_movie_detail, container, false);
            if (arguments != null) {
                movie = (Movie)getArguments().getParcelable("movies_details");
            }else{
                movie = (Movie)intent.getParcelableExtra("movies_details");
            }
            //
            boolean inFavorites = checkFavorites();
            ImageButton addToFav = (ImageButton) rootView.findViewById(R.id.add_to_fav_view);
            if (inFavorites) {
                addToFav.setImageResource(R.drawable.favorite_added);
            }
            // display the main movie info
            ItemsView(rootView);
            parseTrailers();
            parseReviews();

        }



            return rootView;

        }


    private void ItemsView(View v){
            TextView title = (TextView) v.findViewById(R.id.movie_title_view);
            ImageView poster = (ImageView) v.findViewById(R.id.poster_image_view);
            TextView releaseDate = (TextView) v.findViewById(R.id.release_date);
            TextView ratings = (TextView) v.findViewById(R.id.ratings_view);
            TextView overview = (TextView) v.findViewById(R.id.synopsis_view);
        //favourite
        ImageButton addToFav = (ImageButton) rootView.findViewById(R.id.add_to_fav_view);

            title.setText(movie.getTitle());
            Picasso.with(getActivity()).load(movie.getPoster()).into(poster);
            releaseDate.setText(movie.getReleaseDate());
            ratings.setText(movie.getVoteAverage() + "/10");
            overview.setText(movie.getOverview());
        //favourite lisetener

        addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean inFavorites = checkFavorites();
                if(inFavorites){
                    deleteFromFavorites();
                }else{
                    addToFavorites();
                }
                toggleFavorites();
            }
        });
        //___________________________
        }

    private void addToFavorites() {

        Uri uri = PopularMovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        values.clear();

        values.put(PopularMovieContract.MovieEntry.MOVIE_ID, movie.getMovieId());
        values.put(PopularMovieContract.MovieEntry.MOVIE_BACKDROP_URI, movie.getTitle());
        values.put(PopularMovieContract.MovieEntry.MOVIE_TITLE, movie.getTitle());
        values.put(PopularMovieContract.MovieEntry.MOVIE_POSTER, movie.getPoster());
        values.put(PopularMovieContract.MovieEntry.MOVIE_OVERVIEW, movie.getOverview());
        values.put(PopularMovieContract.MovieEntry.MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(PopularMovieContract.MovieEntry.MOVIE_RELEASE_DATE, movie.getReleaseDate());
        values.put(PopularMovieContract.MovieEntry.MOVIE_REVIEWS, movie.getReviews());
        values.put(PopularMovieContract.MovieEntry.MOVIE_TRAILERS, movie.getMoviePreviews());

        Uri check = resolver.insert(uri, values);
    }

    private void parseReviews(){

        if(movie.getReviews() == null)
            return;

        final String ARRAY_OF_REVIEW = "results";
        final String AUTHOR = "author";
        final String REVIEW_CONTENT = "content";

        try {
            JSONObject reviewsJson = new JSONObject(movie.getReviews());
            JSONArray reviewsArray = reviewsJson.getJSONArray(ARRAY_OF_REVIEW);
            int reviewsLength = reviewsArray.length();

            if (reviewsLength > 0){

                // append the review folder
                LinearLayout innerScrollLayout = (LinearLayout)
                        rootView.findViewById(R.id.inner_scroll_layout);

                View reviewsListView = mLayoutInflater.inflate(R.layout.review_list,
                        innerScrollLayout, false);

                innerScrollLayout.addView(reviewsListView);

                LinearLayout reviewList = (LinearLayout)
                        reviewsListView.findViewById(R.id.review_list);

                for (int i = 0; i < reviewsLength; ++i) {

                    View reviewItem = mLayoutInflater.inflate(R.layout.review_item,
                            reviewList, false);

                    JSONObject review = reviewsArray.getJSONObject(i);
                    String reviewAuthor = review.getString(AUTHOR);
                    String reviewContent = review.getString(REVIEW_CONTENT);

                    TextView author = (TextView) reviewItem.findViewById(R.id.review_author);
                    TextView content = (TextView) reviewItem.findViewById(R.id.review_content);

                    author.setText(reviewAuthor);
                    content.setText(reviewContent);

                    reviewList.addView(reviewItem);
                }
            }

        }catch (JSONException e){
            Log.e(LOG_TAG, "ERROR PARSING JSON STRING");
        }
    }

    private void parseTrailers(){

        final String ARRAY_OF_TRAILERS = "results";
        final String TRAILER_ID = "key";
        final String TRAILER_TITLE = "name";

        try{

            JSONObject trailersJson = new JSONObject(movie.getMoviePreviews());
            JSONArray trailersArray = trailersJson.getJSONArray(ARRAY_OF_TRAILERS);
            int trailersLength =  trailersArray.length();

            if(trailersLength > 0) {

                LinearLayout innerScrollLayout = (LinearLayout)
                        rootView.findViewById(R.id.inner_scroll_layout);

                View trailersListView = mLayoutInflater.inflate(R.layout.trailers_list,
                        innerScrollLayout, false);

                innerScrollLayout.addView(trailersListView);

                LinearLayout trailerList = (LinearLayout)
                        trailersListView.findViewById(R.id.trailers_list);

                for (int i = 0; i < trailersLength; ++i) {

                    View trailerItem = mLayoutInflater.inflate(R.layout.trailer_item,
                            trailerList, false);

                    JSONObject trailer = trailersArray.getJSONObject(i);
                    final String trailerId = trailer.getString(TRAILER_ID);
                    String trailerTitle = trailer.getString(TRAILER_TITLE);
                    TextView videoTitle = (TextView) trailerItem.findViewById(R.id.video_title);

                    //shareVideo = trailerId;
                    videoTitle.setText(trailerTitle);
                    trailerList.addView(trailerItem);

                    trailerItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ytIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("vnd.youtube:" + trailerId));
                            ytIntent.putExtra("VIDEO_ID", trailerId);
                            try {
                                startActivity(ytIntent);
                            } catch (ActivityNotFoundException ex) {
                                Log.i(LOG_TAG, "youtube app not installed");
                            }
                        }
                    });
                }
            }

        }catch (JSONException e){
            Log.e(LOG_TAG, "ERROR PARSING JSON STRING");
        }
    }

    private boolean checkFavorites() {

        Uri uri = PopularMovieContract.MovieEntry.buildMovieUri(movie.getMovieId());
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = null;

        try {

            cursor = resolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst())
                return true;

        } finally {

            if(cursor != null)
                cursor.close();

        }

        return false;
    }

    private void deleteFromFavorites() {

        Uri uri = PopularMovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = getActivity().getContentResolver();

        long noDeleted = resolver.delete(uri,
                PopularMovieContract.MovieEntry.MOVIE_ID + " = ? ",
                new String[]{ movie.getMovieId() + "" });

    }

    private void toggleFavorites(){
        boolean inFavorites = checkFavorites();
        ImageButton addToFav = (ImageButton) rootView.findViewById(R.id.add_to_fav_view);

        if(inFavorites){
            addToFav.setImageResource(R.drawable.favorite_added);
        }else{
            addToFav.setImageResource(R.drawable.favorite_removed);
        }
    }
}

