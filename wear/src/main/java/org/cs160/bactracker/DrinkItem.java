package org.cs160.bactracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
    private byte[] imageBytes;
    private String ingredients;
    private int calories;
    private double abv;
    private String category;






    public void recycleImages() {
        this.image.recycle();
        this.resizeImage.recycle();
        this.image = null;
        this.resizeImage = null;
    }

    public DrinkItem() {
        this.image = null;
        this.imageBytes = null;
        this.resizeWidth = 0;
        this.resizeHeight = 0;
        this.resizeImage = null;
        this.name = null;
        this.alcoholContent = 0;
        this.size = 0;
        this.units = null;
        this.calories = 0;
        this.ingredients = "";
        this.count = 0;
        this.abv = 0;
        this.category = category;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (this.imageBytes == null) {
            dest.writeInt(-1);
        } else {
            dest.writeInt(this.imageBytes.length);
            dest.writeByteArray(this.imageBytes);
        }
        dest.writeInt(this.resizeWidth);
        dest.writeInt(this.resizeHeight);
        dest.writeString(this.name);
        dest.writeFloat(this.alcoholContent);
        dest.writeInt(this.size);
        dest.writeString(this.units);
        dest.writeInt(this.calories);
        dest.writeString(this.ingredients);
        dest.writeInt(this.count);
        dest.writeDouble(this.abv);
        dest.writeString(this.category);
        Log.d(TAG, "Wrote Parcel");
    }

    public DrinkItem(Parcel source) {
        Log.d(TAG, "Started Reading Parcel");

        int length = source.readInt();
        if (length < 0) {
            this.imageBytes = null;
            this.image = null;
        } else {
            this.imageBytes = new byte[length];
            source.readByteArray(imageBytes);
            this.image = BitmapFactory.decodeByteArray(this.imageBytes, 0, this.imageBytes.length);
        }
        this.resizeWidth = source.readInt();
        this.resizeHeight = source.readInt();
        if(length < 0) {
            this.resizeImage = null;
        } else {
            this.resizeImage = Bitmap.createScaledBitmap(this.image, resizeWidth, resizeHeight, true);
        }
        this.name = source.readString();
        this.alcoholContent = source.readFloat();
        this.size = source.readInt();
        this.units = source.readString();
        this.calories = source.readInt();
        this.ingredients = source.readString();
        this.count = source.readInt();
        this.abv = source.readDouble();
        this.category = source.readString();
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


    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public byte[] getImageBytes() { return imageBytes; }
    public void setImageBytes(byte[] imageBytes) { this.imageBytes = imageBytes; }
    public int getResizeWidth() { return resizeWidth; }
    public void setResizeWidth(int resizeWidth) { this.resizeWidth = resizeWidth; }
    public int getResizeHeight() {
        return resizeHeight;
    }
    public void setResizeHeight(int resizeHeight) { this.resizeHeight = resizeHeight; }
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
    public void setAlcoholContent(float alcoholContent) { this.alcoholContent = alcoholContent; }
    public double getAbv() {
        return abv;
    }
    public void setAbv(double abv) {
        this.abv = abv;
    }
    public int getCalories() {
        return calories;
    }
    public void setCalories(int calories) { this.calories = calories; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
}