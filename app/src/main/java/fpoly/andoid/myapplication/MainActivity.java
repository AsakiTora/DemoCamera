package fpoly.andoid.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    Button btn, btnLoad, btnPhoto;
    DAO dao;
    Adapter adapter;
    Object object;
    ArrayList<Object> list;
    ListView listView;

    //key này trả về kết quả để xác định đã chụp hoặc up 1 ảnh nào đó từ điện thoại hay chưa.
    final int RES_CODE_CAMERA = 1;
    final int RES_CODE_FOLDER = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);
        btn = findViewById(R.id.btnPost);
        btnLoad = findViewById(R.id.btnLoad);
        btnPhoto = findViewById(R.id.btnPhoto);
        listView = findViewById(R.id.lv);

        object = new Object();
        dao = new DAO(getApplication());
        dao.open();
        list = dao.GetAll();
        adapter = new Adapter(MainActivity.this, list);
        listView.setAdapter(adapter);

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //truy cập vào camera
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, RES_CODE_CAMERA);
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //truy cập vào folder
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RES_CODE_FOLDER);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] hinhAnh = byteArrayOutputStream.toByteArray();
                        object.setImg(hinhAnh);
                    } catch (Exception e) {
                        Toast.makeText(getApplication(), "Chọn ảnh", Toast.LENGTH_SHORT).show();
                    }

                    long res = dao.Add(object);
                    if (res > 0) {
                        Toast.makeText(getApplication(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        list.clear();
                        list.addAll(dao.GetAll());
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplication(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RES_CODE_CAMERA:
                //nếu được cấp quyền cho phép sử dụng sẽ chuyển đến máy ảnh
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, RES_CODE_CAMERA);
                }
                //nếu không sẽ in ra thông báo.
                else {
                    Toast.makeText(getParent(), "Bạn không cho phép mở CAMERA!", Toast.LENGTH_SHORT).show();
                }
                break;
            //nếu được cấp quyền cho phép sử dụng sẽ chuyển đến thư mục ảnh
            case RES_CODE_FOLDER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, RES_CODE_FOLDER);
                } else {
                    Toast.makeText(getParent(), "Bạn không cho phép truy cập thư mục Ảnh!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //nếu ảnh đã được chọn thành công sẽ bắt đầu chuyển đổi thành kiểu bitmap
        if (requestCode == RES_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
        }
        if (requestCode == RES_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                //đọc ảnh liệu được gửi lên
                InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}