package androud.app.leahy.edgedemo.singleplus;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androud.app.leahy.edgedemo.R;


public class LongClickRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent arg0) {
        return new SampleRemoveViewFactory();
    }

    private class SampleRemoveViewFactory implements RemoteViewsService.RemoteViewsFactory {

        private static final int MAX_CHILD = 6;
        private static final int MAX_LEVEL = 10;
        private final String TAG = SampleRemoveViewFactory.class.getSimpleName();
        private float mIdOffset = -1;

        @Override
        public int getCount() {
            return MAX_CHILD;
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public RemoteViews getViewAt(int id) {
            // create list item
            RemoteViews itemView = new RemoteViews(getPackageName(), R.layout.single_plus_remote_list_item);
            int itemId = (int) (id + (mIdOffset * MAX_CHILD));

            switch (itemId) {
                case 0:
                    itemView.setImageViewResource(R.id.item_icon, R.drawable.icon01);
                    itemView.setTextViewText(R.id.item_text1, "电话");
                    break;
                case 1:
                    itemView.setImageViewResource(R.id.item_icon, R.drawable.icon02);
                    itemView.setTextViewText(R.id.item_text1, "相机");
                    break;
                case 2:
                    itemView.setImageViewResource(R.id.item_icon, R.drawable.icon03);
                    itemView.setTextViewText(R.id.item_text1, "地图");
                    break;
                case 3:
                    itemView.setImageViewResource(R.id.item_icon, R.drawable.icon04);
                    itemView.setTextViewText(R.id.item_text1, "字体");
                    break;
                case 4:
                    itemView.setImageViewResource(R.id.item_icon, R.drawable.icon05);
                    itemView.setTextViewText(R.id.item_text1, "还是地图");
                    break;
                case 5:
                    itemView.setImageViewResource(R.id.item_icon, R.drawable.icon06);
                    itemView.setTextViewText(R.id.item_text1, "分享");
                    break;

            }

//            float h = 360 * (mIdOffset / MAX_LEVEL);
//            float s = id / (float) MAX_CHILD;
//            float v = 1f;
//            int bgColor = Color.HSVToColor(new float[]{h, s, v});
//            itemView.setInt(R.id.item_text1, "setBackgroundColor", bgColor);

            // set fill in intent
            Intent intent = new Intent();
            intent.putExtra("item_id", itemId);
//            intent.putExtra("bg_color", bgColor);
            // should be set fillInIntent to root of item layout
            itemView.setOnClickFillInIntent(R.id.item_root, intent);


            return itemView;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            mIdOffset = (mIdOffset + 1) % MAX_LEVEL;
            Log.d(TAG, "onDataSetChanged: " + mIdOffset);
        }

        @Override
        public void onDestroy() {
        }

    }
}
