package movieapp.eslamomar.com.mostmovies;

/**
 * Created by Eslam on 12/26/2015.
 */
public class MovieReview {

    private String author;
    private String content;

    public MovieReview(String author, String content){
        this.author = author;
        this.content = content;;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

}