package com.example.rasen.msunow;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rixoro on 4/5/2017.
 */

public class ForumPostAdapter extends ArrayAdapter<ForumPost> {
    public ForumPostAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ForumPost> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.forum_post, parent, false);

        TextView body = (TextView) convertView.findViewById(R.id.fp_body);
        TextView author = (TextView) convertView.findViewById(R.id.fp_author);
        TextView time = (TextView) convertView.findViewById(R.id.fp_time);
        final TextView karma = (TextView) convertView.findViewById(R.id.fp_karma);
        //ImageView userIc = (ImageView) convertView.findViewById(R.id.fp_user_ic);

        final ForumPost fp = getItem(position);
        body.setText(fp.getBody());
        author.setText(fp.getAuthor());
        time.setText(fp.getTime());
        karma.setText(String.valueOf(fp.getKarma()));
        //userIc.setImageBitmap(/*image*/);

//        Log.i("POSFPADPTR", String.valueOf(position));
        Button upVote = (Button) convertView.findViewById(R.id.fp_upvote);
        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fp.setKarma(fp.getKarma()+1);
                karma.setText(String.valueOf(fp.getKarma()));
                ForumPage.setVote(position);
            }
        });

        Button downVote = (Button) convertView.findViewById(R.id.fp_downvote);
        downVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fp.setKarma(fp.getKarma()-1);
                    karma.setText(String.valueOf(fp.getKarma()));
                    ForumPage.setVote(position);
                }
            });

        return convertView;
    }
}
