package com.kronos.sample;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.kronos.diffutil.IDifference;
import com.kronos.diffutil.IEqualsAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TestEntity implements Parcelable, IDifference, IEqualsAdapter {

    private int id;
    private long displayTime;
    private String text;

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public long getDisplayTime() {
        return displayTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void update() {
        displayTime = System.currentTimeMillis();
        text = "更新数据";
    }


    public TestEntity() {
        displayTime = System.currentTimeMillis();
        text = String.valueOf(new Random().nextInt(10000));
    }

    @NotNull
    @Override
    public String getUniqueId() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof TestEntity) {
            return displayTime == (((TestEntity) obj).displayTime);
        }
        return super.equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.displayTime);
    }

    protected TestEntity(Parcel in) {
        this.id = in.readInt();
        this.displayTime = in.readLong();
    }

    public static final Creator<TestEntity> CREATOR = new Creator<TestEntity>() {
        @Override
        public TestEntity createFromParcel(Parcel source) {
            return new TestEntity(source);
        }

        @Override
        public TestEntity[] newArray(int size) {
            return new TestEntity[size];
        }
    };
}
