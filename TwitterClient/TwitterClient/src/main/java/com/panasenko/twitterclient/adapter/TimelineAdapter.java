/**
 * File: TimelineAdapter.java
 * Created: 2/4/14
 * Author: Viacheslav Panasenko
 */
package com.panasenko.twitterclient.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.panasenko.twitterclient.R;
import com.panasenko.twitterclient.model.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

/**
 * TimelineAdapter
 * Adapter for twitter timeline representation in ListView.
 */
public class TimelineAdapter extends BaseAdapter {

    private Context context;
    private List<Tweet> tweets;
    private LayoutInflater inflater;

    public TimelineAdapter(Context parent, List<Tweet> timeline) {
        super();
        context = parent;
        tweets = timeline;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (tweets == null) {
            throw new IllegalStateException("List not initialized with proper data");
        }

        return tweets.size();
    }

    @Override
    public Object getItem(int i) {
        if (tweets == null || tweets.size() < i) {
            throw new IndexOutOfBoundsException("List doesn't have requested index at: " + i);
        }

        return tweets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.tweet_layout, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Tweet twt = (Tweet) getItem(i);
        holder.userHandle.setText("@" + twt.getUserHandle());
        holder.username.setText(twt.getUsername());
        holder.text.setText(twt.getBody());
        Linkify.addLinks(holder.text, Linkify.WEB_URLS);
        Picasso.with(context).load(Uri.parse(twt.getAvatar())).into(holder.avatar);

        try {
            long timestamp = twt.getTimestampMillis();
            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timestamp,
                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
            holder.timestamp.setText(relativeTime);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.timestamp.setText("(N/A)");
        }

        return view;
    }



    /**
     * ViewHolder
     * Helper class to
     */
    private class ViewHolder {
        TextView username;
        TextView userHandle;
        TextView timestamp;
        TextView text;
        ImageView avatar;

        public ViewHolder(View parent) {
            userHandle = (TextView) parent.findViewById(R.id.tv_user_handle);
            username = (TextView) parent.findViewById(R.id.tv_user_name);
            text = (TextView) parent.findViewById(R.id.tv_tweet);
            timestamp = (TextView) parent.findViewById(R.id.tv_timestamp);
            avatar = (ImageView) parent.findViewById(R.id.iv_avatar);
        }
    }

}
