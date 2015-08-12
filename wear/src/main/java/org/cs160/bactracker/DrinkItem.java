package org.cs160.bactracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by avifrankl on 8/7/15.
 */
public class DrinkItem implements Parcelable {
    private static final long serialVersionUID = 1;
    private Bitmap image, resizeImage;
    private String name, units;
    private float alcoholContent;
    private int size;
    private int resizeWidth;
    private int resizeHeight;
    private final String TAG = "DrinkItem";
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    private byte[] imageBytes;

    public int getResizeWidth() {
        return resizeWidth;
    }

    public void setResizeWidth(int resizeWidth) {
        this.resizeWidth = resizeWidth;
    }

    public int getResizeHeight() {
        return resizeHeight;
    }

    public void setResizeHeight(int resizeHeight) {
        this.resizeHeight = resizeHeight;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getResizeImage() {
        return resizeImage;
    }

    public void setResizeImage(Bitmap resizeImage) {
        this.resizeImage = resizeImage;
    }
    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent(float alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    public void recycleImages() {
        this.image.recycle();
        this.resizeImage.recycle();
        this.image = null;
        this.resizeImage = null;
    }
    public DrinkItem(Bitmap image, int resizeWidth, int resizeHeight, String name, float alcoholContent, int size, String units) {
        this.image = image;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.image.compress(Bitmap.CompressFormat.PNG, 100, out);
        this.imageBytes = out.toByteArray();
        this.resizeWidth=resizeWidth;
        this.resizeHeight=resizeHeight;
        this.resizeImage = Bitmap.createScaledBitmap(this.image, resizeWidth, resizeHeight, true);
        this.name = name;
        this.alcoholContent = alcoholContent;
        this.size = size;
        this.units = units;
    }

    public DrinkItem(Parcel source) {
        Log.d(TAG, "Started Reading Parcel");
        this.imageBytes = new byte[source.readInt()];
        source.readByteArray(this.imageBytes);
        this.image = BitmapFactory.decodeByteArray(this.imageBytes, 0, this.imageBytes.length);
        this.resizeWidth = source.readInt();
        this.resizeHeight = source.readInt();
        this.resizeImage = Bitmap.createScaledBitmap(this.image, resizeWidth, resizeHeight, true);
        this.name = source.readString();
        this.alcoholContent = source.readFloat();
        this.size = source.readInt();
        this.units = source.readString();
        Log.d(TAG, "Finished Reading Parcel");
    }
    public static final Parcelable.Creator<DrinkItem> CREATOR = new Parcelable.Creator<DrinkItem>() {
        @Override
        public DrinkItem createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new DrinkItem(source);
        }
        @Override
        public DrinkItem[] newArray(int size) {
            // TODO Auto-generated method stub
            return new DrinkItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.imageBytes.length);
        dest.writeByteArray(this.imageBytes);
        dest.writeInt(this.resizeWidth);
        dest.writeInt(this.resizeHeight);
        dest.writeString(this.name);
        dest.writeFloat(this.alcoholContent);
        dest.writeInt(this.size);
        dest.writeString(this.units);
        Log.d(TAG, "Wrote Parcel");
    }
}