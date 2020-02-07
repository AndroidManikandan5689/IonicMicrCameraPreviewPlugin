package cordova.plugin.raqmiyat.micrcameraview;

import java.io.Serializable;

public final class LeadSize implements Serializable {
    private static final long serialVersionUID = 1L;
    int _width;
    int _height;

    public static LeadSize getEmpty() {
        return new LeadSize(0, 0);
    }

    public boolean isEmpty() {
        return this._width == 0 && this._height == 0;
    }

    public static LeadSize create(int width, int height) {
        return new LeadSize(width, height);
    }

    public LeadSize() {
    }

    public LeadSize(int width, int height) {
        this._width = width;
        this._height = height;
    }

    public int getWidth() {
        return this._width;
    }

    public void setWidth(int value) {
        this._width = value;
    }

    public int getHeight() {
        return this._height;
    }

    public void setHeight(int value) {
        this._height = value;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == LeadSize.class) {
            LeadSize size = (LeadSize)obj;
            return size._width == this._width && size._height == this._height;
        } else {
            return false;
        }
    }

    public String toString() {
        return String.format("%d,%d", this._width, this._height);
    }

    public int hashCode() {
        return this._width ^ this._height;
    }

    public LeadSize clone() {
        return new LeadSize(this._width, this._height);
    }

}