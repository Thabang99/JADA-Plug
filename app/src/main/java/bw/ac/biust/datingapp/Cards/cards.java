package bw.ac.biust.datingapp.Cards;

public class cards {

    private String name;
    private String userId;
    private String profileImageUrl;
    private String bio;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

 public cards( String userId, String name,String bio,String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.bio=bio;
        this.profileImageUrl=profileImageUrl;

    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
