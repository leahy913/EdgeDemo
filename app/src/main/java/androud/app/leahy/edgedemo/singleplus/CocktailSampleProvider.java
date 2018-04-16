package androud.app.leahy.edgedemo.singleplus;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.samsung.android.sdk.look.cocktailbar.SlookCocktailManager;
import com.samsung.android.sdk.look.cocktailbar.SlookCocktailProvider;

import androud.app.leahy.edgedemo.EdgeLog;
import androud.app.leahy.edgedemo.R;

/**
 * Created by Toommi Leahy on 2018/4/3.
 * <p>
 * 曲面应用，设置界面
 */

public class CocktailSampleProvider extends SlookCocktailProvider {


    private static final String ACTION_REMOTE_LONG_CLICK = "com.example.cocktailslooksample.action.ACTION_REMOTE_LONGCLICK";

    private static final String ACTION_REMOTE_CLICK = "com.example.cocktailslooksample.action.ACTION_REMOTE_CLICK";

    private static final String ACTION_PULL_TO_REFRESH = "com.example.cocktailslooksample.action.ACTION_PULL_TO_REFRESH";


    private static RemoteViews mRemoteListView = null;
    private static RemoteViews mLongClickStateView = null;

    /**
     * 这种方法是在用户添加边缘单模式、边缘单加模式或边缘馈送模式时调用的。如果有必要，它应该执行必要的设置，例如为视图定义事件处理程序并启动临时服务。
     */
    @Override
    public void onUpdate(Context context, SlookCocktailManager cocktailManager, int[] cocktailIds) {
        super.onUpdate(context, cocktailManager, cocktailIds);

        EdgeLog.wtf("onUpdate");

        if (mLongClickStateView == null) {
            mLongClickStateView = createStateView(context);
        }
        if (mRemoteListView == null) {
            mRemoteListView = createRemoteListView(context);
        }
        cocktailManager.updateCocktail(cocktailIds[0], mRemoteListView, mLongClickStateView);

        // 刷新
//        Intent refreshintent = new Intent(ACTION_PULL_TO_REFRESH);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0xff, refreshintent, PendingIntent.FLAG_UPDATE_CURRENT);
//        SlookCocktailManager.getInstance(context).setOnPullPendingIntent(cocktailIds[0], R.id.remote_list, pendingIntent);


    }


    /**
     * 这种方法是在首次创建边缘单模式、边单加模式或边缘馈送模式时调用的。如果您需要打开一个新的数据库或执行安装，那么这是一个很好的地方。
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        EdgeLog.wtf("onEnabled");
    }


    /**
     * 当您的边缘单模式、边单加模式或边缘馈送模式的实例从已启用列表中删除时，将调用此方法。这是你应该做的任何工作，清理onenabled（上下文），如删除临时数据库。
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        EdgeLog.wtf("onDisabled");
    }


    /**
     * 此方法用于每次广播和上述回调方法之前调用。
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        EdgeLog.wtf("onReceive");
        String action = intent.getAction();
        switch (action) {
            case ACTION_REMOTE_LONG_CLICK:
                performRemoteLongClick(context, intent);
                break;
            case ACTION_REMOTE_CLICK:
                performRemoteLongClick(context, intent);
                break;
            case ACTION_PULL_TO_REFRESH:
                //TODO 刷新 set pull to refresh
                Toast.makeText(context, "refresh", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    /**
     * 这种方法被称为当能见度边缘的单一模式，边缘单加模式或边缘订阅模式改变。
     */
    @Override
    public void onVisibilityChanged(Context context, int cocktailId, int visibility) {
        super.onVisibilityChanged(context, cocktailId, visibility);
        EdgeLog.wtf("onVisibilityChanged");
    }

    /**
     * 设置
     */
    private RemoteViews createStateView(Context context) {
        RemoteViews stateView = new RemoteViews(context.getPackageName(), R.layout.single_plus_long_click_state_layout);
        SlookCocktailManager.getInstance(context).setOnLongClickPendingIntent(stateView, R.id.state_btn1, getLongClickIntent(context, R.id.state_btn1, 0));
        stateView.setOnClickPendingIntent(R.id.state_btn1, getClickIntent(context, R.id.state_btn1, 0));
        stateView.setOnClickPendingIntent(R.id.state_btn2, getClickIntent(context, R.id.state_btn2, 0));
        return stateView;
    }

    private PendingIntent getClickIntent(Context context, int id, int key) {
        Intent clickIntent = new Intent(ACTION_REMOTE_CLICK);
        clickIntent.putExtra("id", id);
        clickIntent.putExtra("key", key);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, id, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pIntent;
    }

    private PendingIntent getLongClickIntent(Context context, int id, int key) {
        Intent longClickIntent = new Intent(ACTION_REMOTE_LONG_CLICK);
        longClickIntent.putExtra("id", id);
        longClickIntent.putExtra("key", key);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, id, longClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pIntent;
    }


    /**
     * 曲面列表视图
     */


    private RemoteViews createRemoteListView(Context context) {
        RemoteViews remoteListView = new RemoteViews(context.getPackageName(), R.layout.single_plus_remote_list_view);
        Intent remoteIntent = new Intent(context, LongClickRemoteViewService.class);
        remoteListView.setRemoteAdapter(R.id.remote_list, remoteIntent);
        SlookCocktailManager.getInstance(context).setOnLongClickPendingIntentTemplate(remoteListView, R.id.remote_list, getLongClickIntent(context, R.id.remote_list, 0));
        remoteListView.setPendingIntentTemplate(R.id.remote_list, getClickIntent(context, R.id.remote_list, 0));

        return remoteListView;
    }

    /**
     * 事件
     */

    private void performRemoteLongClick(Context context, Intent intent) {
        StringBuffer debugString = new StringBuffer("ACTION_REMOTE_LONGCLICK");
        int id = intent.getIntExtra("id", -1);
        debugString.append("id=").append(intent.getIntExtra("id", -1));

        switch (id) {
            case R.id.state_btn1:
                Toast.makeText(context, "state_btn1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.state_btn2:
                Toast.makeText(context, "state_btn2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remote_list:
                int itemId = intent.getIntExtra("item_id", -1);
                switch (itemId) {
                    case 0:
                        //电话
//                        Toast.makeText(context, "电话", Toast.LENGTH_LONG).show();
                        Intent call = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:");
                        intent.setData(data);
                        context.startActivity(call);
                        break;
                    case 1:
                        //相机
//                        Toast.makeText(context, "相机", Toast.LENGTH_LONG).show();
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        context.startActivity(cameraIntent);
                        break;
                    case 2:
                        //地图
//                        Toast.makeText(context, "地图", Toast.LENGTH_LONG).show();
                        Intent map = new Intent("android.intent.action.VIEW",android.net.Uri.parse("androidamap://showTraffic?sourceApplication=softname&amp;poiid=BGVIS1&amp;lat=36.2&amp;lon=116.1&amp;level=10&amp;dev=0"));
                        intent.setPackage("com.autonavi.minimap");
                        context.startActivity(map);
                        break;
                    case 3:
                        //字体
                        Toast.makeText(context, "字体", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        //还是地图
                        Toast.makeText(context, "还是地图", Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        //分享
//                        Toast.makeText(context, "分享", Toast.LENGTH_LONG).show();
                        Intent textIntent = new Intent(Intent.ACTION_SEND);
                        textIntent.setType("text/plain");
                        textIntent.putExtra(Intent.EXTRA_TEXT, "这是我分享的一条侧屏信息");
                        context.startActivity(Intent.createChooser(textIntent, "分享"));
                        break;
                }
                break;
            default:
                break;

        }
    }


}
