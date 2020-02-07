

package cordova.plugin.raqmiyat.micrcameraview;

import java.io.Serializable;

public final class LeadRect  implements Serializable {
    private static final long serialVersionUID = 1L;
    int _x;
    int _y;
    int _width;
    int _height;
    transient int _left;
    transient int _top;
    transient int _right;
    transient int _bottom;

    public static LeadRect getEmpty() {
        return new LeadRect(0, 0, 0, 0);
    }

    public boolean isEmpty() {
        return this._left == 0 && this._top == 0 && this._right == 0 && this._bottom == 0;
    }

    public LeadRect() {
    }

    public LeadRect clone() {
        return new LeadRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    public LeadRect(int left, int top, int width, int height) {
        this._left = left;
        this._top = top;
        this._right = left + width;
        this._bottom = top + height;
        this.update();
    }

    public static LeadRect fromLTRB(int left, int top, int right, int bottom) {
        return new LeadRect(left, top, right - left, bottom - top);
    }

    public static LeadRect normalize(LeadRect rect) {
        LeadRect result = rect.clone();
        result.normalize();
        return result;
    }

    public void normalize() {
        if (this._left > this._right || this._top > this._bottom) {
            int left = Math.min(this._left, this._right);
            int top = Math.min(this._top, this._bottom);
            int right = Math.max(this._left, this._right);
            int bottom = Math.max(this._top, this._bottom);
            this._left = left;
            this._top = top;
            this._right = right;
            this._bottom = bottom;
            this.update();
        }

    }

    public int getX() {
        return this._left;
    }

    public void setX(int value) {
        int width = this._right - this._left;
        this._left = value;
        this._right = this._left + width;
        this.update();
    }

    public int getY() {
        return this._top;
    }

    public void setY(int value) {
        int height = this._bottom - this._top;
        this._top = value;
        this._bottom = this._top + height;
        this.update();
    }

    public int getRight() {
        return this._right;
    }

    public void setRight(int value) {
        this._right = value;
        this.update();
    }

    public int getBottom() {
        return this._bottom;
    }

    public void setBottom(int value) {
        this._bottom = value;
        this.update();
    }

    public int getLeft() {
        return this.getX();
    }

    public void setLeft(int value) {
        this.setX(value);
        this.update();
    }

    public int getTop() {
        return this.getY();
    }

    public void setTop(int value) {
        this.setY(value);
        this.update();
    }

    public int getWidth() {
        return this._right - this._left;
    }

    public void setWidth(int value) {
        this._right = this._left + value;
        this.update();
    }

    public int getHeight() {
        return this._bottom - this._top;
    }

    public void setHeight(int value) {
        this._bottom = this._top + value;
        this.update();
    }

    public LeadRect(LeadPoint location, LeadSize size) {
        this._left = location._x;
        this._top = location._y;
        this._right = location._x + size._width;
        this._bottom = location._y + size._height;
        this.update();
    }

    public LeadPoint getLocation() {
        return new LeadPoint(this.getLeft(), this.getTop());
    }

    public void setLocation(LeadPoint value) {
        this.setLeft(value._x);
        this.setTop(value._y);
    }

    public LeadSize getSize() {
        return new LeadSize(this.getWidth(), this.getHeight());
    }

    public void setSize(LeadSize value) {
        this.setWidth(value._width);
        this.setHeight(value._height);
    }

    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == LeadRect.class) {
            LeadRect rc = (LeadRect)obj;
            return rc.getX() == this.getX() && rc.getY() == this.getY() && rc.getWidth() == this.getWidth() && rc.getHeight() == this.getHeight();
        } else {
            return false;
        }
    }

    public boolean contains(int x, int y) {
        return this.getX() <= x && x < this.getX() + this.getWidth() && this.getY() <= y && y < this.getY() + this.getHeight();
    }

    public boolean contains(LeadPoint pt) {
        return this.contains(pt._x, pt._y);
    }

    public boolean contains(LeadRect rect) {
        return this.getX() <= rect.getX() && rect.getX() + rect.getWidth() <= this.getX() + this.getWidth() && this.getY() <= rect.getY() && rect.getY() + rect.getHeight() <= this.getY() + this.getHeight();
    }

    public int hashCode() {
        return this.getX() ^ (this.getY() << 13 | this.getY() >> 19) ^ (this.getWidth() << 26 | this.getWidth() >> 6) ^ (this.getHeight() << 7 | this.getHeight() >> 25);
    }

    public void inflate(int width, int height) {
        this.setX(this.getX() - width);
        this.setY(this.getY() - height);
        this.setWidth(this.getWidth() + 2 * width);
        this.setHeight(this.getHeight() + 2 * height);
    }

    public void inflate(LeadSize size) {
        this.inflate(size._width, size._height);
    }

    public static LeadRect inflate(LeadRect rect, int x, int y) {
        LeadRect rectangle = rect.clone();
        rectangle.inflate(x, y);
        return rectangle;
    }

    public void intersect(LeadRect rect) {
        LeadRect temp = new LeadRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        LeadRect rectangle = intersect(rect, temp);
        this.setX(rectangle.getX());
        this.setY(rectangle.getY());
        this.setWidth(rectangle.getWidth());
        this.setHeight(rectangle.getHeight());
    }

    public static LeadRect intersect(LeadRect a, LeadRect b) {
        int x = Math.max(a.getX(), b.getX());
        int right = Math.min(a.getX() + a.getWidth(), b.getX() + b.getWidth());
        int y = Math.max(a.getY(), b.getY());
        int bottom = Math.min(a.getY() + a.getHeight(), b.getY() + b.getHeight());
        return right >= x && bottom >= y ? new LeadRect(x, y, right - x, bottom - y) : getEmpty();
    }

    public boolean intersectsWith(LeadRect rect) {
        return rect.getX() < this.getX() + this.getWidth() && this.getX() < rect.getX() + rect.getWidth() && rect.getY() < this.getY() + this.getHeight() && this.getY() < rect.getY() + rect.getHeight();
    }

    public static LeadRect union(LeadRect a, LeadRect b) {
        int x = Math.min(a.getX(), b.getX());
        int right = Math.max(a.getX() + a.getWidth(), b.getX() + b.getWidth());
        int y = Math.min(a.getY(), b.getY());
        int bottom = Math.max(a.getY() + a.getHeight(), b.getY() + b.getHeight());
        return new LeadRect(x, y, right - x, bottom - y);
    }

    public void offset(LeadPoint pos) {
        this.offset(pos._x, pos._y);
    }

    public void offset(int x, int y) {
        this.setX(this.getX() + x);
        this.setY(this.getY() + y);
    }

    public static LeadRect create(int x, int y, int width, int height) {
        return new LeadRect(x, y, width, height);
    }

    public String toString() {
        return String.format("%d,%d,%d,%d", this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }


    private void update() {
        this._x = this._left;
        this._y = this._top;
        this._width = this._right - this._left;
        this._height = this._bottom - this._top;
    }
}

