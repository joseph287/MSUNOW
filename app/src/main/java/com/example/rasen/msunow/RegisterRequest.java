package com.example.rasen.msunow;

import android.os.Parcel;
import android.os.Parcelable;

public class RegisterRequest implements Parcelable {

    //parameters required for user registration
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String dob;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter of parameters

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RegisterRequest() {
    }

    protected RegisterRequest(Parcel in) {
        this.userId = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.dob = in.readString();
    }


    public static final Creator<RegisterRequest> CREATOR = new Creator<RegisterRequest>() {
        @Override
        public RegisterRequest createFromParcel(Parcel in) {
            return new RegisterRequest(in);
        }

        @Override
        public RegisterRequest[] newArray(int size) {
            return new RegisterRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.dob);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RegisterRequest registerRequest = (RegisterRequest) obj;

        if (userId != null ? !userId.equals(registerRequest.userId) : registerRequest.userId != null)
            return false;
        if (firstName != null ? !firstName.equals(registerRequest.firstName) : registerRequest.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(registerRequest.lastName) : registerRequest.lastName != null)
            return false;
        if (email != null ? !email.equals(registerRequest.email) : registerRequest.email != null)
            return false;
        if (password != null ? !password.equals(registerRequest.password) : registerRequest.password != null)
            return false;
        return dob != null ? !dob.equals(registerRequest.dob) : registerRequest.dob != null;
    }
}
