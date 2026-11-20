package org.example.tmdb.dto;

import java.net.URL;
import java.util.Objects;

public class CharacterDto {

    private String characterName;
    private URL imgUrl;
    private String actorName;
    private String birthday;
    private String deathday = null;
    private String placeOfBirth;

    public CharacterDto() {
    }

    public CharacterDto(String characterName, URL imgUrl, String actorName,
                        String birthday, String deathday, String placeOfBirth) {
        this.characterName = characterName;
        this.imgUrl = imgUrl;
        this.actorName = actorName;
        this.birthday = birthday;
        this.deathday = deathday;
        this.placeOfBirth = placeOfBirth;
    }

    public CharacterDto(String characterName, URL imgUrl, String actorName,
                        String birthday, String placeOfBirth) {
        this(characterName, imgUrl, actorName, birthday, null, placeOfBirth);
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public URL getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(URL imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterDto that = (CharacterDto) o;
        return Objects.equals(characterName, that.characterName) &&
                Objects.equals(actorName, that.actorName) &&
                Objects.equals(birthday, that.birthday) &&
                Objects.equals(deathday, that.deathday) &&
                Objects.equals(placeOfBirth, that.placeOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterName, actorName, birthday, deathday, placeOfBirth);
    }

    @Override
    public String toString() {
        return "CharacterDto{" +
                "characterName='" + characterName + '\'' +
                ", imgUrl=" + imgUrl +
                ", actorName='" + actorName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", deathday='" + deathday + '\'' +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                '}';
    }
}
