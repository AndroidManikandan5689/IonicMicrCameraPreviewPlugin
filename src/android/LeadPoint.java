
package cordova.plugin.raqmiyat.micrcameraview;

import java.io.Serializable;

public final class LeadPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    int _x;
    int _y;

    public static LeadPoint getEmpty() {
        return new LeadPoint();
    }

    public boolean isEmpty() {
        return this._x == 0 && this._y == 0;
    }

    public LeadPoint() {
    }

    public static LeadPoint create(int x, int y) {
        return new LeadPoint(x, y);
    }

    public LeadPoint(int x, int y) {
        this._x = x;
        this._y = y;
    }

    public LeadPoint clone() {
        return this.isEmpty() ? new LeadPoint() : new LeadPoint(this._x, this._y);
    }

    public int getX() {
        return this._x;
    }

    public void setX(int value) {
        this._x = value;
    }

    public int getY() {
        return this._y;
    }

    public void setY(int value) {
        this._y = value;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == LeadPoint.class) {
            LeadPoint point = (LeadPoint)obj;
            return point._x == this._x && point._y == this._y;
        } else {
            return false;
        }
    }

    public String toString() {
        return String.format("%d,%d", this._x, this._y);
    }

    public int hashCode() {
        return this._x ^ this._y;
    }

}