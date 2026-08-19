package br.com.erudio.integrationtests.dto;
import br.com.erudio.model.Book;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.Column;
import jakarta.xml.bind.annotation.XmlRootElement;

import javax.swing.text.StyledEditorKit;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@XmlRootElement
public class PersonDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Date birthDay;

    private String address;

    private String password;

    private String gender;

    private Boolean enabled;

    private String profileUrl;

    private String photoUrl;

    private List<Book> books;

    public PersonDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
                "id=" + this.id + "\n" +
                ", firstName='" + firstName + "\n" +
                ", lastName='" + lastName + "\n" +
                ", phoneNumber='" + phoneNumber + "\n" +
                ", birthDay=" + birthDay + "\n" +
                ", address='" + address + "\n" +
                ", password='" + password + "\n" +
                ", gender='" + gender + "\n" +
                ", enabled=" + enabled + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PersonDTO personDTO)) return false;
        return Objects.equals(id, personDTO.id) && Objects.equals(firstName, personDTO.firstName) && Objects.equals(lastName, personDTO.lastName) && Objects.equals(phoneNumber, personDTO.phoneNumber) && Objects.equals(birthDay, personDTO.birthDay) && Objects.equals(address, personDTO.address) && Objects.equals(password, personDTO.password) && Objects.equals(gender, personDTO.gender) && Objects.equals(enabled, personDTO.enabled) && Objects.equals(profileUrl, personDTO.profileUrl) && Objects.equals(photoUrl, personDTO.photoUrl) && Objects.equals(books, personDTO.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phoneNumber, birthDay, address, password, gender, enabled, profileUrl, photoUrl, books);
    }
}
