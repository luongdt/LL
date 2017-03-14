package com.devhd.remote.remote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.connectsdk.core.MediaInfo;
import com.connectsdk.core.SubtitleInfo;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.device.ConnectableDeviceListener;
import com.connectsdk.device.DevicePicker;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.service.DeviceService;
import com.connectsdk.service.capability.ExternalInputControl;
import com.connectsdk.service.capability.KeyControl;
import com.connectsdk.service.capability.Launcher;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MediaPlayer;
import com.connectsdk.service.capability.MouseControl;
import com.connectsdk.service.capability.PlaylistControl;
import com.connectsdk.service.capability.PowerControl;
import com.connectsdk.service.capability.TVControl;
import com.connectsdk.service.capability.TextInputControl;
import com.connectsdk.service.capability.ToastControl;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.capability.WebAppLauncher;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.sessions.LaunchSession;

import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
    ImageButton ibVolume, ibPower, ibRemoteUp, ibRemoteLeft, ibRemoteRight, ibOk, ibReplay, ibRemoteBottom, ibHome,  ibVolumeUp, ibVolumeDown, ibChanelUp, ibChanelDowen;//, ibPlay, ibFastN, ibFasrPre;
    LinearLayout llControl, llNumber;
    Button btnNumberControl,btnNumber,btnNumber1, btnNumber2, btnNumber3, btnNumber4, btnNumber5, btnNumber6, btnNumber7, btnNumber8, btnNumber9, btnSubt, btnMenu;
    AlertDialog dialog;
    AlertDialog pairingAlertDialog;
    AlertDialog pairingCodeDialog;
    DevicePicker dp;
    ConnectableDevice mTV;
    LinearLayout llMask;

    //   private ConnectableDevice mTv;
    private Launcher launcher;
    private MediaPlayer mediaPlayer;
    private MediaControl mediaControl;
    private TVControl tvControl;
    private VolumeControl volumeControl;
    private ToastControl toastControl;
    private MouseControl mouseControl;
    private TextInputControl textInputControl;
    private PowerControl powerControl;
    private ExternalInputControl externalInputControl;
    private KeyControl keyControl;
    private WebAppLauncher webAppLauncher;
    public Button[] buttons;
    Context mContext;
    public TestResponseObject testResponse;
    private ConnectableDeviceListener deviceListener = new ConnectableDeviceListener() {

        @Override
        public void onPairingRequired(ConnectableDevice device, DeviceService service, DeviceService.PairingType pairingType) {
            Log.e("2ndScreenAPP", "Connected to " + mTV.getIpAddress());

            switch (pairingType) {
                case FIRST_SCREEN:
                    Log.e("2ndScreenAPP", "First Screen");
                    pairingAlertDialog.show();
                    break;

                case PIN_CODE:
                case MIXED:
                    Log.e("2ndScreenAPP", "Pin Code");
                    pairingCodeDialog.show();
                    break;

                case NONE:
                default:
                    break;
            }
        }

        @Override
        public void onConnectionFailed(ConnectableDevice device, ServiceCommandError error) {
            Log.e("2ndScreenAPP", "onConnectFailed");
            connectFailed(mTV);
        }

        @Override
        public void onDeviceReady(ConnectableDevice device) {
            Log.e("2ndScreenAPP", "onPairingSuccess");
            if (pairingAlertDialog.isShowing()) {
                pairingAlertDialog.dismiss();
            }
            if (pairingCodeDialog.isShowing()) {
                pairingCodeDialog.dismiss();
            }
            registerSuccess(mTV);
        }

        @Override
        public void onDeviceDisconnected(ConnectableDevice device) {
            Log.e("2ndScreenAPP", "Device Disconnected");
            connectEnded(mTV);
            //   connectItem.setTitle("Connect");

            //   BaseFragment frag = mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem());
            //   if (frag != null) {
            //      Toast.makeText(getApplicationContext(), "Device Disconnected", Toast.LENGTH_SHORT).show();
            //      frag.disableButtons();
            //  }
        }

        @Override
        public void onCapabilityUpdated(ConnectableDevice device, List<String> added, List<String> removed) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//
//            @Override
//            public void run() {
//
//            }
//        });
        setupPicker();


        DiscoveryManager.getInstance().registerDefaultDeviceTypes();
        DiscoveryManager.getInstance().setPairingLevel(DiscoveryManager.PairingLevel.ON);
        DiscoveryManager.getInstance().start();

//          setupPicker();
//
//          DiscoveryManager.getInstance().registerDefaultDeviceTypes();
//          DiscoveryManager.getInstance().setPairingLevel(DiscoveryManager.PairingLevel.ON);
//          DiscoveryManager.getInstance().start();
//          DiscoveryManager.getInstance().addListener(this);
//         dialog.show();



        testResponse = new TestResponseObject();
    }
    void initView(){
        ibHome = (ImageButton) findViewById(R.id.ibHome);
        //     ibNext = (ImageButton) v.findViewById(R.id.ibNext);
        ibOk = (ImageButton) findViewById(R.id.ibRemoteOk);
        ibPower = (ImageButton) findViewById(R.id.imgPower);
        //    ibPause = (ImageButton) v.findViewById(R.id.ibPause);
        //   ibPrev = (ImageButton) v.findViewById(R.id.ibPrev);
        ibRemoteBottom = (ImageButton) findViewById(R.id.ibRemoteBottom);
        ibRemoteLeft = (ImageButton) findViewById(R.id.ibRemoteLeft);
        ibRemoteUp = (ImageButton) findViewById(R.id.imgControlTop);
        ibReplay = (ImageButton) findViewById(R.id.ibReplay);
        ibVolume = (ImageButton) findViewById(R.id.imgVolume);
        ibRemoteRight = (ImageButton) findViewById(R.id.ibRemoteRight);
        //    ibPlay = (ImageButton) v.findViewById(R.id.ibplay);
        ibVolumeDown = (ImageButton) findViewById(R.id.ibvolumedown);
        ibVolumeUp = (ImageButton) findViewById(R.id.ibvolumeup);
        ibChanelDowen = (ImageButton) findViewById(R.id.ibchaneldown);
        ibChanelUp = (ImageButton) findViewById(R.id.ibchanelup);
        //     ibFasrPre = (ImageButton) v.findViewById(R.id.ibPrevFast);
        //    ibFastN = (ImageButton) v.findViewById(R.id.ibPrevFast);
        llControl = (LinearLayout) findViewById(R.id.llControl);
        llNumber = (LinearLayout) findViewById(R.id.llNumber);
        btnNumber1 = (Button) findViewById(R.id.number1);
        btnNumber = (Button) findViewById(R.id.number0);
        btnNumber2 = (Button) findViewById(R.id.number2);
        btnNumber3 = (Button) findViewById(R.id.number3);
        btnNumber4 = (Button) findViewById(R.id.number4);
        btnNumber5 = (Button) findViewById(R.id.number5);
        btnNumber6 = (Button) findViewById(R.id.number6);
        btnNumber7 = (Button) findViewById(R.id.number7);
        btnNumber8 = (Button) findViewById(R.id.number8);
        btnNumber9 = (Button) findViewById(R.id.number9);
        //btnAudio = (Button) v.findViewById(R.id.audio);
        btnSubt = (Button) findViewById(R.id.subt);
        btnNumberControl = (Button) findViewById(R.id.btnNumber);
        llMask = (LinearLayout) findViewById(R.id.llmask);
        //  btnMenu = (Button) v.findViewById(R.id.btnMenu);


        ibHome.setOnClickListener(this);
        //   ibNext.setOnClickListener(this);
        ibOk.setOnClickListener(this);
        ibPower.setOnClickListener(this);
        //  ibPause.setOnClickListener(this);
        //   ibPrev.setOnClickListener(this);
        ibRemoteBottom.setOnClickListener(this);
        ibRemoteUp.setOnClickListener(this);
        ibRemoteLeft.setOnClickListener(this);
        ibRemoteRight.setOnClickListener(this);
        ibReplay.setOnClickListener(this);
        ibVolume.setOnClickListener(this);
        //    ibFasrPre.setOnClickListener(this);
        //   ibFastN.setOnClickListener(this);
        ibChanelUp.setOnClickListener(this);
        ibChanelDowen.setOnClickListener(this);
        ibVolumeUp.setOnClickListener(this);
        ibVolumeDown.setOnClickListener(this);
        //   ibPlay.setOnClickListener(this);
        btnNumber1.setOnClickListener(this);
        btnNumber.setOnClickListener(this);
        btnNumber2.setOnClickListener(this);
        btnNumber3.setOnClickListener(this);
        btnNumber4.setOnClickListener(this);
        btnNumber5.setOnClickListener(this);
        btnNumber6.setOnClickListener(this);
        btnNumber7.setOnClickListener(this);
        btnNumber8.setOnClickListener(this);
        btnNumber9.setOnClickListener(this);
        //btnAudio.setOnClickListener(this);
        btnSubt.setOnClickListener(this);
        btnNumberControl.setOnClickListener(this);
        //  btnMenu.setOnClickListener(this);
        //  ibFasrPre.setOnLongClickListener(this);
        //   ibFastN.setOnLongClickListener(this);



    }



    void connectFailed(ConnectableDevice device) {
        if (device != null)
            Log.e("2ndScreenAPP", "Failed to connect to " + device.getIpAddress());

        if (mTV != null) {
            mTV.removeListener(deviceListener);
            mTV.disconnect();
            mTV = null;
        }
    }

    void connectEnded(ConnectableDevice device) {
        if (pairingAlertDialog.isShowing()) {
            pairingAlertDialog.dismiss();
        }
        if (pairingCodeDialog.isShowing()) {
            pairingCodeDialog.dismiss();
        }
        mTV.removeListener(deviceListener);
        mTV = null;
    }
    void registerSuccess(ConnectableDevice device) {
        Log.e("2ndScreenAPP", "successful register");

        //  BaseFragment frag = mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem());
        //   if (frag != null)
        //       frag.setTv(mTV);
        setTv();
//        if (mTV.hasCapability(KeyControl.Up)) {
//            ibRemoteUp.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (keyControl != null) {
//                        keyControl.up(null);
//                        testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.UpClicked);
//                    }
//                }
//            });
//        }
    }
    private void setupPicker() {
        dp = new DevicePicker(this);

        ///////////////////
//        final DevicePickerListView view = new DevicePickerListView(this);
//        ConnectableDevice c = new ConnectableDevice();
//        for (int i = 0; i <  view.pickerAdapter.getCount(); i++) {
//            ConnectableDevice d = view.pickerAdapter.getItem(i);
//        }
        //////////////////////

        dialog = dp.getPickerDialog("Device List", new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                mTV = (ConnectableDevice)arg0.getItemAtPosition(arg2);
                mTV.addListener(deviceListener);
                mTV.setPairingType(null);
                mTV.connect();
                Log.e("","===" + mTV.getFriendlyName());
                //    connectItem.setTitle(mTV.getFriendlyName());

                dp.pickDevice(mTV);
            }
        });

        pairingAlertDialog = new AlertDialog.Builder(this)
                .setTitle("Pairing with TV")
                .setMessage("Please confirm the connection on your TV")
                .setPositiveButton("Okay", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dp.cancelPicker();

                        hConnectToggle();
                    }
                })
                .create();

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        //   final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        pairingCodeDialog = new AlertDialog.Builder(this)
                .setTitle("Enter Pairing Code on TV")
                .setView(input)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (mTV != null) {
                            String value = input.getText().toString().trim();
                            mTV.sendPairingKey(value);
                            //  imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dp.cancelPicker();

                        hConnectToggle();
                        //    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    }
                })
                .create();
        dialog.show();
    }
    public void hConnectToggle()
    {
        if (!this.isFinishing()) {
            if (mTV != null)
            {
                if (mTV.isConnected())
                    mTV.disconnect();

                //    connectItem.setTitle("Connect");
                mTV.removeListener(deviceListener);
                mTV = null;
                //  for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
                //      if (mSectionsPagerAdapter.getFragment(i) != null) {
                //          mSectionsPagerAdapter.getFragment(i).setTv(null);
                //      }
                //  }
            } else
            {
                dialog.show();
            }
        }
    }

    public void setTv()
    {
        //   = tv;

        if (mTV == null) {
            launcher = null;
            mediaPlayer = null;
            mediaControl = null;
            tvControl = null;
            volumeControl = null;
            toastControl = null;
            textInputControl = null;
            mouseControl = null;
            externalInputControl = null;
            powerControl = null;
            keyControl = null;
            webAppLauncher = null;

            //  disableButtons();
        }
        else {
            launcher = mTV.getCapability(Launcher.class);
            mediaPlayer = mTV.getCapability(MediaPlayer.class);
            mediaControl = mTV.getCapability(MediaControl.class);
            tvControl = mTV.getCapability(TVControl.class);
            volumeControl = mTV.getCapability(VolumeControl.class);
            toastControl = mTV.getCapability(ToastControl.class);
            textInputControl = mTV.getCapability(TextInputControl.class);
            mouseControl = mTV.getCapability(MouseControl.class);
            externalInputControl = mTV.getCapability(ExternalInputControl.class);
            powerControl = mTV.getCapability(PowerControl.class);
            keyControl = mTV.getCapability(KeyControl.class);
            webAppLauncher = mTV.getCapability(WebAppLauncher.class);

            // enableButtons();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ibHome:
                if (keyControl != null) {
                    keyControl.home(null);
                    testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.HomeClicked);
                }
                break;
            //   case R.id.ibNext:
            //    if (mPlaylistControl != null)
            //       mPlaylistControl.next(null);
            //      testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.Next);
            //    break;
            case R.id.ibRemoteOk:
                if (keyControl != null) {
                    keyControl.ok(null);
                    testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.Clicked);
                }
                break;
            case R.id.imgPower:
                if (powerControl != null) {
                    testResponse = new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.Power_OFF);
                    powerControl.powerOff(null);
                }
                break;
            //       case R.id.ibPause:
            //           mediaControl.pause(null);
            //           break;
            //       case R.id.ibPrev:
            //          break;
            case R.id.ibRemoteBottom:
                if (keyControl != null) {
                    keyControl.down(null);
                    testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.DownClicked);
                }
                break;
            case R.id.ibRemoteLeft:
                if (keyControl != null) {
                    keyControl.left(null);
                    testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.LeftClicked);
                }
                break;
            case R.id.ibRemoteRight:
                if (keyControl != null) {
                    keyControl.right(null);
                    testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.RightClicked);
                }
                break;
            case R.id.imgControlTop:
                //    if (mTV.hasCapability(KeyControl.Up)) {
                if (keyControl != null) {
                    keyControl.up(null);
                    testResponse = new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.UpClicked);
                }
                //     }
                break;
            case R.id.imgVolume:
                view.setSelected(!view.isSelected());
                volumeControl.setMute(view.isSelected(), null);
                if (view.isSelected()) {
                    //Handle selected state change
                    testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.Muted_Media);
                } else {
                    //Handle de-select state change
                    testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.UnMuted_Media);
                }
                break;
            //    case R.id.ibReplay:
            //        if (keyControl != null) {
            //           keyControl.back(null);
            //       }
            //       break;
            case R.id.ibchaneldown :
                if(tvControl != null) {
                    tvControl.channelDown(null);
                }
                break;
            case R.id.ibchanelup:
                if (tvControl != null)
                    tvControl.channelUp(null);
                break;
            case R.id.ibvolumedown:
                if (volumeControl != null){
                    volumeControl.volumeDown(null);
                    testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.VolumeDown);}
                break;
            case R.id.ibvolumeup:
                if (volumeControl != null){
                    volumeControl.volumeUp(null);
                    testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.VolumeUp);}
                break;
            //     case R.id.ibplay :
            //       if (mediaControl != null)
            //           mediaControl.play(null);
            //        testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.Played_Media);
            //    playVideo();
            //         break;
            //     case R.id.ibNextFast:
            //         if (mPlaylistControl != null)
            //              mPlaylistControl.next(null);
            //        testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.Next);
            //         break;
            //    case R.id.ibPrevFast:
            //        if (mPlaylistControl != null)
            //            mPlaylistControl.previous(null);
            //        testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.Previous);
            //          break;
            case R.id.number0:
                if (keyControl != null)
                    keyControl.sendKeyCode(KeyControl.KeyCode.NUM_0, null);
                break;
            case R.id.number1:
                if (keyControl != null)
                    keyControl.sendKeyCode(KeyControl.KeyCode.NUM_1, null);
                break;
            case R.id.number2:
                if (keyControl != null)
                    keyControl.sendKeyCode(KeyControl.KeyCode.NUM_2, null);
                break;
            case R.id.number3:
                if (keyControl != null)
                    keyControl.sendKeyCode(KeyControl.KeyCode.NUM_3, null);
                break;
            case R.id.number4:
                if (keyControl != null)
                    keyControl.sendKeyCode(KeyControl.KeyCode.NUM_4, null);
                break;
            case R.id.number5:
                if (keyControl != null)
                    keyControl.sendKeyCode(KeyControl.KeyCode.NUM_5, null);
                break;
            case R.id.number6:
                if (keyControl != null)
                    keyControl.sendKeyCode(KeyControl.KeyCode.NUM_6, null);
                break;
            case R.id.number7:
                if (keyControl != null)
                    keyControl.sendKeyCode(KeyControl.KeyCode.NUM_7, null);
                break;
            case R.id.number8:
                if (keyControl != null)
                    keyControl.sendKeyCode(KeyControl.KeyCode.NUM_8, null);
                break;
            case R.id.number9:
                if (keyControl != null)
                    keyControl.sendKeyCode(KeyControl.KeyCode.NUM_9, null);
                break;
            //     case R.id.audio:
            //       break;
            case R.id.subt:
                if (keyControl != null) {
                    keyControl.ok(null);
                    testResponse =  new TestResponseObject(true, TestResponseObject.SuccessCode, TestResponseObject.Clicked);
                }
                break;
            case  R.id.btnNumber:
                if(llNumber.getVisibility() == View.VISIBLE){
                    llControl.setVisibility(View.VISIBLE);
                    llNumber.setVisibility(View.GONE);
                    Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.slideinl);
                    llControl.startAnimation(hyperspaceJumpAnimation);
                }else{
                    llControl.setVisibility(View.GONE);
                    llNumber.setVisibility(View.VISIBLE);
                    Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.slideinr);
                    llNumber.startAnimation(hyperspaceJumpAnimation);
                }
                break;
            //     case  R.id.btnMenu:
            //       if (keyControl != null)
            //           keyControl.menu(null);
            //      break;




        }

    }
    public LaunchSession launchSession;
    //private MediaControl mMediaControl = null;
    private PlaylistControl mPlaylistControl = null;
    public static final String URL_IMAGE_ICON =
            "http://ec2-54-201-108-205.us-west-2.compute.amazonaws.com/samples/media/videoIcon.jpg";
    public static final String URL_VIDEO_MP4 = "http://103.233.48.21/hls/vod/Hoang/ryan/index.m3u8";
    private void playVideo() {
        //   Log.e("","click playyyy");
        //   boolean shouldLoop = loopingButton.isChecked();

        SubtitleInfo.Builder subtitleBuilder = null;
//        if (subtitlesButton.isChecked()) {
//            subtitleBuilder = new SubtitleInfo.Builder(
//                    getTv().hasCapability(MediaPlayer.Subtitle_WebVTT) ? URL_SUBTITLES_WEBVTT :
//                            URL_SUBTITLE_SRT);
//            subtitleBuilder.setLabel("English").setLanguage("en");
//        }

        MediaInfo mediaInfo = new MediaInfo.Builder(URL_VIDEO_MP4, "video/mp4")
                .setTitle("luongdt")
                .setDescription("ABCSDDSSD")
                .setIcon(URL_IMAGE_ICON)
                .setSubtitleInfo(subtitleBuilder == null ? null : subtitleBuilder.build())
                .build();

//        mediaPlayer.playMedia(URL_VIDEO_MP4, "", "title", "sdas", "", true, new MediaPlayer.LaunchListener() {
//            @Override
//            public void onSuccess(MediaPlayer.MediaLaunchObject object) {
//
//            }
//
//            @Override
//            public void onError(ServiceCommandError error) {
//
//            }
//        });

        mediaPlayer.playMedia(mediaInfo, true, new MediaPlayer.LaunchListener() {

            @Override
            public void onError(ServiceCommandError error) {
                Log.e("Error", "Error playing video", error);
                //      stopMediaSession();
            }

            public void onSuccess(MediaPlayer.MediaLaunchObject object) {
                Log.e(""," sucesss");
                launchSession = object.launchSession;
                testResponse = new TestResponseObject(true, TestResponseObject.SuccessCode,
                        TestResponseObject.Play_Video);
                mediaControl = object.mediaControl;
                mPlaylistControl = object.playlistControl;
                stopUpdating();
                //   enableMedia();
                //   isPlaying = true;
            }
        });
    }
    private Timer refreshTimer;
    private void stopUpdating() {
        if (refreshTimer == null)
            return;

        refreshTimer.cancel();
        refreshTimer = null;
    }



//    @Override
//    public void onDeviceAdded(DiscoveryManager manager,final ConnectableDevice device) {
//        Log.e("","3===33"+ device.getModelName());
//        String name = device.getFriendlyName().substring(0,5);
//        Log.e("==","==string==" +name);
//        if(name.equals("NAGRA") || name.equals("OpenTV")){
//            Log.e("","====");
//            mTV = device;
//            mTV.addListener(deviceListener);
//            mTV.setPairingType(null);
//            mTV.connect();
//            Log.e("","===" + mTV.getFriendlyName());
//            //    connectItem.setTitle(mTV.getFriendlyName());
//
//            dp.pickDevice(mTV);
//            llMask.setVisibility(View.GONE);
//        }
//        Util.runOnUI(new Runnable () {
//            @Override
//            public void run() {
//                int index = -1;
//                for (int i = 0; i < 3; i++) {
//              //      ConnectableDevice d = pickerAdapter.getItem(i);
//                    //          Log.e("","dv===" + d.getFriendlyName());
//
//                    String newDeviceName = device.getFriendlyName();
//                   // String dName = d.getFriendlyName();
//
//                    if (newDeviceName == null) {
//                        newDeviceName = device.getModelName();
//                    }
//
//                  // // if (dName == null) {
//                 //       dName = d.getModelName();
//                 //   }
//
//
//
//
//                }
//
//
//            }
//        });
    //   }
//
//    @Override
//    public void onDeviceUpdated(DiscoveryManager manager, ConnectableDevice device) {
//
//    }
//
//    @Override
//    public void onDeviceRemoved(DiscoveryManager manager, ConnectableDevice device) {
//
//    }
//
//    @Override
//    public void onDiscoveryFailed(DiscoveryManager manager, ServiceCommandError error) {
//
//    }

    public void removeTVListener(){
        if (mTV != null) {
            mTV.removeListener(deviceListener);
            mTV.disconnect();
            mTV = null;
        }
    }

}
