
package com.ikmr.banbara23.yaeyama_liner_register.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Liner implements Parcelable {

    public Port port;
    public Status status;
    public String text;

    public Liner() {
    }

    protected Liner(Parcel in) {
        port = (Port) in.readValue(Port.class.getClassLoader());
        status = (Status) in.readValue(Status.class.getClassLoader());
        text = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(port);
        dest.writeValue(status);
        dest.writeString(text);
    }

    @SuppressWarnings("unused")
    public static final Creator<Liner> CREATOR = new Creator<Liner>() {
        @Override
        public Liner createFromParcel(Parcel in) {
            return new Liner(in);
        }

        @Override
        public Liner[] newArray(int size) {
            return new Liner[size];
        }
    };

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Liner{" +
                "port=" + port +
                ", type=" + status +
                ", value='" + text + '\'' +
                '}';
    }
}
