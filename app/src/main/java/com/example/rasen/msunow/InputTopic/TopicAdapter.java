package com.example.rasen.msunow.InputTopic;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.rasen.msunow.ForumPost;
import com.example.rasen.msunow.R;

import java.util.List;


/**
 * Created by willl on 4/16/2017.
 */

public class TopicAdapter extends ArrayAdapter<Topic> {


    public TopicAdapter(Context context, int resource, List<Topic> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.topic_list, parent, false);

        TextView title = (TextView) convertView.findViewById(R.id.topic_title);
        TextView author = (TextView) convertView.findViewById(R.id.topic_author);
        TextView time = (TextView) convertView.findViewById(R.id.topic_time);
        //ImageView userIc = (ImageView) convertView.findViewById(R.id.fp_user_ic);

        Topic topic = getItem(position);
        title.setText(topic.getTitle());
        author.setText(topic.getAuthor());
        time.setText(topic.getTime());

        //userIc.setImageBitmap(/*image*/);


        return convertView;
    }

}
