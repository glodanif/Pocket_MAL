package com.g.pocketmal.data.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchParams implements Parcelable {

    public static final int SEARCH_TYPE_ANIME = 6;
    public static final int SEARCH_TYPE_MANGA = 8;
    public static final int SEARCH_EPISODES = 4;
    public static final int SEARCH_SCORE = 3;

    private String query;

    private int type;
    private int score;
    private int status;
    private int producers;
    private int rated;
    private String startDate;
    private String finishDate;

    private Set<Integer> genres = new HashSet<>();
    private boolean exclude;

    private int sorting;
    private boolean reverse;

    public SearchParams() {

    }

    public SearchParams(Parcel parcel) {
        query = parcel.readString();
        type = parcel.readInt();
        score = parcel.readInt();
        status = parcel.readInt();
        producers = parcel.readInt();
        rated = parcel.readInt();
        startDate = parcel.readString();
        finishDate = parcel.readString();
        genres = new HashSet<Integer>(parcel.readArrayList(null));
        exclude = parcel.readInt() == 1;
        sorting = parcel.readInt();
        reverse = parcel.readInt() == 1;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProducers() {
        return producers;
    }

    public void setProducers(int producers) {
        this.producers = producers;
    }

    public int getRated() {
        return rated;
    }

    public void setRated(int rated) {
        this.rated = rated;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartDateDay() {
        return startDate == null ? "0" : startDate.substring(8, 10);
    }

    public String getStartDateMonth() {
        return startDate == null ? "0" : startDate.substring(5, 7);
    }

    public String getStartDateYear() {
        return startDate == null ? "0" : startDate.substring(0, 4);
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public String getFinishDateDay() {
        return finishDate == null ? "0" : finishDate.substring(8, 10);
    }

    public String getFinishDateMonth() {
        return finishDate == null ? "0" : finishDate.substring(5, 7);
    }

    public String getFinishDateYear() {
        return finishDate == null ? "0" : finishDate.substring(0, 4);
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public List<Integer> getGenres() {
        return new ArrayList<>(genres);
    }

    public void addGenre(int genreId) {
        this.genres.add(genreId);
    }

    public void removeGenre(int genreId) {
        this.genres.remove(genreId);
    }

    public void setGenres(Collection<Integer> genres) {
        this.genres.clear();
        this.genres.addAll(genres);
    }

    public int exclude() {
        return exclude ? 1 : 0;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    public int reverse() {
        return reverse ? 2 : 1;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public String toString() {
        return "SearchParams{" +
                "query='" + query + '\'' +
                ", type=" + type +
                ", score=" + score +
                ", status=" + status +
                ", producers=" + producers +
                ", rated=" + rated +
                ", startDate='" + startDate + '\'' +
                ", finishDate='" + finishDate + '\'' +
                ", genres=" + genres +
                ", exclude=" + exclude +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(query);
        dest.writeInt(type);
        dest.writeInt(score);
        dest.writeInt(status);
        dest.writeInt(producers);
        dest.writeInt(rated);
        dest.writeString(startDate);
        dest.writeString(finishDate);
        dest.writeList(new ArrayList<>(genres));
        dest.writeInt(exclude ? 1 : 0);
        dest.writeInt(sorting);
        dest.writeInt(reverse ? 1 : 0);
    }

    public static Creator<SearchParams> CREATOR = new Creator<SearchParams>() {

        @Override
        public SearchParams createFromParcel(Parcel source) {
            return new SearchParams(source);
        }

        @Override
        public SearchParams[] newArray(int size) {
            return new SearchParams[size];
        }

    };
}
