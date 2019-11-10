package edu.temple.bookcase;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Book implements Parcelable {
    private int id;
    private String title, author, coverURL, published;

    public Book(JSONObject jsonBook) throws JSONException {
        this.title = jsonBook.getString("Title"); this.author = jsonBook.getString("Author");
        this.coverURL = jsonBook.getString("cover_url");
        this.id = jsonBook.getInt("book_id"); this.published = jsonBook.getString("Published");
    }

    protected Book(Parcel in)
    {
        id = in.readInt();
        title = in.readString();
        author = in.readString();
        coverURL = in.readString();
        published = in.readString();

    }



    public void setId(int id) {
        this.id = id;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public int getId() {
        return id;
    }

    public String getPublished() {
        return published;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCoverURL() {
        return coverURL;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(coverURL);
        dest.writeString(published);
    }
}
