package org.example.tmdb.dto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieInfoDto {

    private String title;
    private List<String> genre = new ArrayList<>();
    private URL poster_path;
    private int year;
    private List<CharacterDto> characters = new ArrayList<>();

    public MovieInfoDto() {
    }

    public MovieInfoDto(String title, List<String> genre, URL poster_path, int year, List<CharacterDto> characters) {
        this.title = title;
        this.genre = genre != null ? genre : new ArrayList<>();
        this.poster_path = poster_path;
        this.year = year;
        this.characters = characters != null ? characters : new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre != null ? genre : new ArrayList<>();
    }

    public List<String> getGenres() {
        return getGenre();
    }

    public void setGenres(List<String> genres) {
        setGenre(genres);
    }

    public URL getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(URL poster_path) {
        this.poster_path = poster_path;
    }

    public URL getPosterPath() {
        return getPoster_path();
    }

    public void setPosterPath(URL posterPath) {
        setPoster_path(posterPath);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<CharacterDto> getCharacters() {
        return characters;
    }

    public void setCharacters(List<CharacterDto> characters) {
        this.characters = characters != null ? characters : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieInfoDto that = (MovieInfoDto) o;
        return year == that.year &&
                Objects.equals(title, that.title) &&
                Objects.equals(genre, that.genre) &&
                Objects.equals(characters, that.characters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genre, year, characters);
    }

    @Override
    public String toString() {
        return "MovieInfoDto{" +
                "title='" + title + '\'' +
                ", genre=" + genre +
                ", poster_path=" + poster_path +
                ", year=" + year +
                ", characters=" + characters +
                '}';
    }
}
