package com.example.awaaz.Class;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.example.awaaz.Activity.MainActivity;


public class SLR implements Parcelable {
    private int id;
    private LiveVideo liveVideo;
    private UploadImage uploadImage;
    private Context context;
    private ImageView temp;

    public SLR(int id, Context context) {
        this.id = id;
        this.context = context;
    }

    protected SLR(Parcel in) {
        id = in.readInt();
    }

    public static final Creator<SLR> CREATOR = new Creator<SLR>() {
        @Override
        public SLR createFromParcel(Parcel in) {
            return new SLR(in);
        }

        @Override
        public SLR[] newArray(int size) {
            return new SLR[size];
        }
    };

    public LiveVideo getLiveVideo() {
        return liveVideo;
    }

    public void setLiveVideo(LiveVideo liveVideo) {
        this.liveVideo = liveVideo;
    }

    public void startLiveVideo() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("SLR", this);
        context.startActivity(intent);
    }

    public void setClassifier(ImageClassifier imageClassifier) {
//
    }

    public void uploadImage() {
        uploadImage = new UploadImage(context, temp);
        uploadImage.openGallery();
    }

    public void setInterval(int interval) {
        liveVideo.setInterval(interval);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
    }
}
