package fpoly.andoid.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    Context mContext;
    DAO dao;
    ArrayList<Object> list;
    Object object;
    public Adapter(Context context, ArrayList<Object> list) {
        this.mContext = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        if (list == null){
            return 0;
        }else {
            return list.size();
        }
    }

    @Override
    public java.lang.Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public class ViewHolder{
        public ImageView img;
        public TextView id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder view;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
            view = new ViewHolder();

            view.img = convertView.findViewById(R.id.imgAdapter);
            view.id = convertView.findViewById(R.id.tvId);
            convertView.setTag(view);
        }else {
            view = (ViewHolder) convertView.getTag();
        }
        object = (Object) list.get(position);
        byte[] img = object.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        view.img.setImageBitmap(bitmap);
        view.id.setText(object.getId() + "");
        return convertView;
    }
}
