package com.facilityone.wireless.patrol;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facilityone.wireless.a.arch.base.FMFragment;
import com.facilityone.wireless.a.arch.ec.utils.SPKey;
import com.facilityone.wireless.a.arch.mvp.BaseFragmentActivity;
import com.facilityone.wireless.a.arch.offline.util.PatrolQrcodeUtils;
import com.facilityone.wireless.patrol.fragment.NfcFragment;
import com.facilityone.wireless.patrol.fragment.PatrolScanFragment;

import java.util.List;

import static com.blankj.utilcode.util.ToastUtils.showShort;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2018/11/16 6:07 PM
 */
public class NfcRedTagActivity extends BaseFragmentActivity {

    private NfcFragment mNfcFragment;

    @Override
    public Object createPresenter() {
        return null;
    }

    @Override
    protected int getContextViewId() {
        return R.id.patrol_nfc_id;
    }

    @Override
    protected FMFragment setRootFragment() {
        mNfcFragment = new NfcFragment();
        return mNfcFragment;
    }

    private static final String TAG = "stickynotes";
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private IntentFilter[] mNdefExchangeFilters;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            showShort(R.string.patrol_not_support_nfc);
            return;
        }
        mNfcAdapter.setNdefPushMessage(null, this);
        // Handle all of our received NFC intents in this activity.
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(
                NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
        }
        mNdefExchangeFilters = new IntentFilter[]{ ndefDetected };
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mResumed = true;
        // Sticky notes received from Android
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            if (messages != null && messages.length > 0) {
                NdefRecord[] ndefRecords = messages[0].getRecords();
                if (ndefRecords != null && ndefRecords.length > 0) {
                    byte[] payload = ndefRecords[0].getPayload();
                    setNoteBody(new String(payload));
                    setIntent(new Intent()); // Consume this intent.
                }
            }
        }
        if (mNfcAdapter != null) {
            enableNdefExchangeMode();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
    }

    private void setNoteBody(String body) {
        // 用户是否登录
        boolean logon = SPUtils.getInstance(SPKey.SP_MODEL).getBoolean(SPKey.HAVE_LOGON, false);
        if (!logon) {
            showShort(R.string.patrol_not_login_tip);
            this.finish();
            return;
        }

        // 应用是否打开
        List<Activity> activityList = ActivityUtils.getActivityList();
        if (activityList == null || activityList.size() < 2) {
            showShort(R.string.patrol_not_open_app_tip);
            this.finish();
            return;
        }
        
        String code = PatrolQrcodeUtils.parseSpotCode(body);
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort(R.string.patrol_qrcode_no_match);
        } else {
            mNfcFragment.startWithPop(PatrolScanFragment.getInstance(code));
        }
//        this.finish();
    }

    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{
                        record
                });
                msgs = new NdefMessage[]{
                        msg
                };
            }
        } else {
            Log.d(TAG, "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private void enableNdefExchangeMode() {
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }
}
