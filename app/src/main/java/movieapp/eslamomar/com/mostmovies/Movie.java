package movieapp.eslamomar.com.mostmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eslam on 12/19/2015.
 */
public class Movie implements Parcelable {
    private String title;
    private String poster;
    private String overview;
    private String voteAverage;
    private String releaseDate;


    private String movieReviews;
    private String moviePreviews = "";
    private int movieId;


    public Movie(int movieId ,String title, String poster, String overview,
                 String voteAverage, String releaseDate){
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;

        this.movieId = movieId;


    }




    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
//for  rev and youtube
    public int getMovieId() {
        return movieId;
    }

    public String getReviews() {
        return movieReviews;
    }

    public void setReviews(String reviews) {
        movieReviews = reviews;
    }

    public String getMoviePreviews() {
        return moviePreviews;
    }

    public void setMoviePreviews(String previews) {
        moviePreviews = previews;
    }




    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(poster);
        out.writeString(overview);
        out.writeString(voteAverage);
        out.writeString(releaseDate);

        out.writeInt(movieId);
        out.writeString(movieReviews);
        out.writeString(moviePreviews);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Movie(Parcel in) {
        title = in.readString();
        poster = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();

        movieId = in.readInt();
        movieReviews = in.readString();
        moviePreviews = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}