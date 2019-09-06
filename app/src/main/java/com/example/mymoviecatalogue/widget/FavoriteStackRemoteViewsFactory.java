package com.example.mymoviecatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.mymoviecatalogue.BuildConfig;
import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.database.Contract;
import com.example.mymoviecatalogue.model.Favorite;

import java.util.concurrent.ExecutionException;

import static android.widget.AdapterView.INVALID_POSITION;
import static com.example.mymoviecatalogue.widget.FavoriteWidget.EXTRA_ITEM;

public class FavoriteStackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;

    private Cursor cursor;

    FavoriteStackRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        final long token = Binder.clearCallingIdentity();

        cursor = context.getContentResolver().query(Contract.Columns.Companion.getCONTENT_URI(), null, null, null, null);

        Binder.restoreCallingIdentity(token);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        int count;
        if (cursor == null) {
            count = 0;
        } else {
            count = cursor.getCount();
        }
        return count;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (i == INVALID_POSITION || cursor == null || !cursor.moveToPosition(i)) {
            return null;
        }

        Favorite favorite = getItem(i);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context).asBitmap()
                    .load(BuildConfig.POSTER_URL + favorite.getPoster())
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_broken_image)
                    .submit()
                    .get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        remoteViews.setImageViewBitmap(R.id.img_poster, bitmap);

        Bundle extras = new Bundle();
        extras.putString(EXTRA_ITEM, favorite.getTitle());

        Intent intent = new Intent();
        intent.putExtras(extras);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private Favorite getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Invalid position!");
        }

        return new Favorite(cursor);
    }
}
