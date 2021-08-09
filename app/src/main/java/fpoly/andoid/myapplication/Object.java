package fpoly.andoid.myapplication;

public class Object {
    int id;
    byte[] img;

    //Tạo các thuộc tính truy vấn đến từng cột trong database
    public static final String TB_NAME = "object";
    public static final String COL_ID = "id";
    public static final String COL_IMG = "img";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
