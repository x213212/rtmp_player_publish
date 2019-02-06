package com.pedro.rtpstreamer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.encoder.input.gl.SpriteGestureController;
import com.pedro.encoder.input.gl.render.filters.AndroidViewFilterRender;
import com.pedro.encoder.input.gl.render.filters.BasicDeformationFilterRender;
import com.pedro.encoder.input.gl.render.filters.BeautyFilterRender;
import com.pedro.encoder.input.gl.render.filters.BlurFilterRender;
import com.pedro.encoder.input.gl.render.filters.BrightnessFilterRender;
import com.pedro.encoder.input.gl.render.filters.CartoonFilterRender;
import com.pedro.encoder.input.gl.render.filters.CircleFilterRender;
import com.pedro.encoder.input.gl.render.filters.ColorFilterRender;
import com.pedro.encoder.input.gl.render.filters.ContrastFilterRender;
import com.pedro.encoder.input.gl.render.filters.DuotoneFilterRender;
import com.pedro.encoder.input.gl.render.filters.EarlyBirdFilterRender;
import com.pedro.encoder.input.gl.render.filters.EdgeDetectionFilterRender;
import com.pedro.encoder.input.gl.render.filters.ExposureFilterRender;
import com.pedro.encoder.input.gl.render.filters.FireFilterRender;
import com.pedro.encoder.input.gl.render.filters.GammaFilterRender;
import com.pedro.encoder.input.gl.render.filters.GlitchFilterRender;
import com.pedro.encoder.input.gl.render.filters.GreyScaleFilterRender;
import com.pedro.encoder.input.gl.render.filters.HalftoneLinesFilterRender;
import com.pedro.encoder.input.gl.render.filters.Image70sFilterRender;
import com.pedro.encoder.input.gl.render.filters.LamoishFilterRender;
import com.pedro.encoder.input.gl.render.filters.MoneyFilterRender;
import com.pedro.encoder.input.gl.render.filters.NegativeFilterRender;
import com.pedro.encoder.input.gl.render.filters.NoFilterRender;
import com.pedro.encoder.input.gl.render.filters.PixelatedFilterRender;
import com.pedro.encoder.input.gl.render.filters.PolygonizationFilterRender;
import com.pedro.encoder.input.gl.render.filters.RGBSaturationFilterRender;
import com.pedro.encoder.input.gl.render.filters.RainbowFilterRender;
import com.pedro.encoder.input.gl.render.filters.RippleFilterRender;
import com.pedro.encoder.input.gl.render.filters.RotationFilterRender;
import com.pedro.encoder.input.gl.render.filters.SaturationFilterRender;
import com.pedro.encoder.input.gl.render.filters.SepiaFilterRender;
import com.pedro.encoder.input.gl.render.filters.SharpnessFilterRender;
import com.pedro.encoder.input.gl.render.filters.SnowFilterRender;
import com.pedro.encoder.input.gl.render.filters.SwirlFilterRender;
import com.pedro.encoder.input.gl.render.filters.TemperatureFilterRender;
import com.pedro.encoder.input.gl.render.filters.ZebraFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.GifObjectFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.ImageObjectFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.SurfaceFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.TextObjectFilterRender;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.encoder.utils.gl.TranslateTo;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import com.pedro.rtplibrary.view.OpenGlView;
import com.pedro.rtpstreamer.customexample.RtmpActivity;
import com.pedro.rtpstreamer.customexample.RtspActivity;

import com.pedro.rtpstreamer.utils.ActivityLink;
import com.upyun.upplayer.widget.UpVideoView;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity  implements ConnectCheckerRtmp, View.OnClickListener ,SurfaceHolder.Callback ,View.OnTouchListener
{
    private RtmpCamera1 rtmpCamera1;
    private Button button;
    private Button bRecord;
    private EditText etUrl;

    private String currentDateAndTime = "";
    private File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/rtmp-rtsp-stream-client-java");
    private OpenGlView openGlView;
    private SpriteGestureController spriteGestureController = new SpriteGestureController();
    String path = "http://192.168.2.101:7001/live/movie.flv";
    private GridView list;
  private List<ActivityLink> activities;

  private final String[] PERMISSIONS = {
      Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
      Manifest.permission.WRITE_EXTERNAL_STORAGE
  };

  ///
  private static final String TAG = MainActivity.class.getSimpleName();
    RelativeLayout.LayoutParams mVideoParams;

    UpVideoView upVideoView;
    private EditText mPathEt;
    private TableLayout mHudView;
    private TextView mPlayInfo;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
      setContentView(R.layout.activity_open_gl);
      openGlView = findViewById(R.id.surfaceView);
      button = findViewById(R.id.b_start_stop);
      button.setOnClickListener(this);
      bRecord = findViewById(R.id.b_record);
      bRecord.setOnClickListener(this);
      Button switchCamera = findViewById(R.id.switch_camera);
      switchCamera.setOnClickListener(this);
      etUrl = findViewById(R.id.et_rtp_url);
      etUrl.setHint(R.string.hint_rtmp);
      etUrl.setText("rtmp://192.168.2.101:1936/live/test");
      rtmpCamera1 = new RtmpCamera1(openGlView, this);
      openGlView.getHolder().addCallback(this);
      openGlView.setOnTouchListener(this);
      rtmpCamera1.prepareVideo();
    overridePendingTransition(R.transition.slide_in, R.transition.slide_out);

//    list = findViewById(R.id.list);
//    createList();
//    setListAdapter(activities);

    if (!hasPermissions(this, PERMISSIONS)) {
      ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
    }


      getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

      mPathEt = (EditText) findViewById(R.id.editText);
      mPathEt.setText(path);
      mHudView = (TableLayout) findViewById(R.id.hud_view);
      mPlayInfo = (TextView) findViewById(R.id.tv_info);
      mPlayInfo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (mHudView.getVisibility() == View.VISIBLE) {
                  mHudView.setVisibility(View.GONE);
              } else {
                  mHudView.setVisibility(View.VISIBLE);
              }
          }
      });

      upVideoView = (UpVideoView) findViewById(R.id.uvv_vido);
      upVideoView.setHudView(mHudView);

      //设置背景图片
//        upVideoView.setImage(R.drawable.dog);

      //设置播放地址
     // upVideoView.setVideoPath(path);

      //开始播放
    //  upVideoView.start();
  }

  private void createList() {
    activities = new ArrayList<>();
    activities.add(
        new ActivityLink(new Intent(this, RtmpActivity.class), getString(R.string.rtmp_streamer),
            JELLY_BEAN));
    activities.add(
        new ActivityLink(new Intent(this, RtspActivity.class), getString(R.string.rtsp_streamer),
            JELLY_BEAN));
//    activities.add(new ActivityLink(new Intent(this, ExampleRtmpActivity.class),
//        getString(R.string.default_rtmp), JELLY_BEAN));
//    activities.add(new ActivityLink(new Intent(this, ExampleRtspActivity.class),
//        getString(R.string.default_rtsp), JELLY_BEAN));
//    activities.add(new ActivityLink(new Intent(this, RtmpFromFileActivity.class),
//        getString(R.string.from_file_rtmp), JELLY_BEAN_MR2));
//    activities.add(new ActivityLink(new Intent(this, RtspFromFileActivity.class),
//        getString(R.string.from_file_rtsp), JELLY_BEAN_MR2));
//    activities.add(new ActivityLink(new Intent(this, SurfaceModeRtmpActivity.class),
//        getString(R.string.surface_mode_rtmp), LOLLIPOP));
//    activities.add(new ActivityLink(new Intent(this, SurfaceModeRtspActivity.class),
//        getString(R.string.surface_mode_rtsp), LOLLIPOP));
//    activities.add(new ActivityLink(new Intent(this, TextureModeRtmpActivity.class),
//        getString(R.string.texture_mode_rtmp), LOLLIPOP));
//    activities.add(new ActivityLink(new Intent(this, TextureModeRtspActivity.class),
//        getString(R.string.texture_mode_rtsp), LOLLIPOP));
//    activities.add(new ActivityLink(new Intent(this, DisplayRtmpActivity.class),
//        getString(R.string.display_rtmp), LOLLIPOP));
//    activities.add(new ActivityLink(new Intent(this, DisplayRtspActivity.class),
//        getString(R.string.display_rtsp), LOLLIPOP));
    activities.add(new ActivityLink(new Intent(this, MainActivity.class),
        getString(R.string.opengl_rtmp), JELLY_BEAN_MR2));
//    activities.add(new ActivityLink(new Intent(this, OpenGlRtspActivity.class),
//        getString(R.string.opengl_rtsp), JELLY_BEAN_MR2));
  }




  private void showMinSdkError(int minSdk) {
    String named;
    switch (minSdk) {
      case JELLY_BEAN_MR2:
        named = "JELLY_BEAN_MR2";
        break;
      case LOLLIPOP:
        named = "LOLLIPOP";
        break;
      default:
        named = "JELLY_BEAN";
        break;
    }
    Toast.makeText(this, "You need min Android " + named + " (API " + minSdk + " )",
        Toast.LENGTH_SHORT).show();
  }

  private void showPermissionsErrorAndRequest() {
    Toast.makeText(this, "You need permissions before", Toast.LENGTH_SHORT).show();
    ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
  }

  private boolean hasPermissions(Context context, String... permissions) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
      for (String permission : permissions) {
        if (ActivityCompat.checkSelfPermission(context, permission)
            != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
      }
    }
    return true;
  }

    @Override
    protected void onResume() {
        super.onResume();

        // 重新开始播放器
        upVideoView.resume();
        upVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        upVideoView.pause();
    }

    public void toggle(View view) {

        if (upVideoView.isPlaying()) {

            //暂停播放
            upVideoView.pause();

        } else {

            //开始播放
            upVideoView.start();
        }
    }

    public void refresh(View view) {
        path = mPathEt.getText().toString();
        upVideoView.setVideoPath(path);
        upVideoView.start();
    }

    //全屏播放
    public void fullScreen(View view) {
        upVideoView.fullScreen(this);
     //   fullScreen();
    }

    private void fullScreen() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(metrics.widthPixels, metrics.heightPixels);
        mVideoParams = (RelativeLayout.LayoutParams) upVideoView.getLayoutParams();
        upVideoView.setLayoutParams(params);
       // upVideoView.getTrackInfo();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (upVideoView.isFullState()) {
            //退出全屏
            upVideoView.exitFullScreen(this);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        upVideoView.release(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gl_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Stop listener for image, text and gif stream objects.
        spriteGestureController.setBaseObjectFilterRender(null);
        switch (item.getItemId()) {
            case R.id.e_d_fxaa:
                rtmpCamera1.getGlInterface().enableAA(!rtmpCamera1.getGlInterface().isAAEnabled());
                Toast.makeText(this,
                        "FXAA " + (rtmpCamera1.getGlInterface().isAAEnabled() ? "enabled" : "disabled"),
                        Toast.LENGTH_SHORT).show();
                return true;
            //filters. NOTE: You can change filter values on fly without reset the filter.
            // Example:
            // ColorFilterRender color = new ColorFilterRender()
            // rtmpCamera1.setFilter(color);
            // color.setRGBColor(255, 0, 0); //red tint
            case R.id.no_filter:
                rtmpCamera1.getGlInterface().setFilter(new NoFilterRender());
                return true;
            case R.id.android_view:
                AndroidViewFilterRender androidViewFilterRender = new AndroidViewFilterRender();
                androidViewFilterRender.setView(findViewById(R.id.activity_example_rtmp));
                rtmpCamera1.getGlInterface().setFilter(androidViewFilterRender);
                return true;
            case R.id.basic_deformation:
                rtmpCamera1.getGlInterface().setFilter(new BasicDeformationFilterRender());
                return true;
            case R.id.beauty:
                rtmpCamera1.getGlInterface().setFilter(new BeautyFilterRender());
                return true;
            case R.id.blur:
                rtmpCamera1.getGlInterface().setFilter(new BlurFilterRender());
                return true;
            case R.id.brightness:
                rtmpCamera1.getGlInterface().setFilter(new BrightnessFilterRender());
                return true;
            case R.id.cartoon:
                rtmpCamera1.getGlInterface().setFilter(new CartoonFilterRender());
                return true;
            case R.id.circle:
                rtmpCamera1.getGlInterface().setFilter(new CircleFilterRender());
                return true;
            case R.id.color:
                rtmpCamera1.getGlInterface().setFilter(new ColorFilterRender());
                return true;
            case R.id.contrast:
                rtmpCamera1.getGlInterface().setFilter(new ContrastFilterRender());
                return true;
            case R.id.duotone:
                rtmpCamera1.getGlInterface().setFilter(new DuotoneFilterRender());
                return true;
            case R.id.early_bird:
                rtmpCamera1.getGlInterface().setFilter(new EarlyBirdFilterRender());
                return true;
            case R.id.edge_detection:
                rtmpCamera1.getGlInterface().setFilter(new EdgeDetectionFilterRender());
                return true;
            case R.id.exposure:
                rtmpCamera1.getGlInterface().setFilter(new ExposureFilterRender());
                return true;
            case R.id.fire:
                rtmpCamera1.getGlInterface().setFilter(new FireFilterRender());
                return true;
            case R.id.gamma:
                rtmpCamera1.getGlInterface().setFilter(new GammaFilterRender());
                return true;
            case R.id.glitch:
                rtmpCamera1.getGlInterface().setFilter(new GlitchFilterRender());
                return true;
            case R.id.gif:
                setGifToStream();
                return true;
            case R.id.grey_scale:
                rtmpCamera1.getGlInterface().setFilter(new GreyScaleFilterRender());
                return true;
            case R.id.halftone_lines:
                rtmpCamera1.getGlInterface().setFilter(new HalftoneLinesFilterRender());
                return true;
            case R.id.image:
                setImageToStream();
                return true;
            case R.id.image_70s:
                rtmpCamera1.getGlInterface().setFilter(new Image70sFilterRender());
                return true;
            case R.id.lamoish:
                rtmpCamera1.getGlInterface().setFilter(new LamoishFilterRender());
                return true;
            case R.id.money:
                rtmpCamera1.getGlInterface().setFilter(new MoneyFilterRender());
                return true;
            case R.id.negative:
                rtmpCamera1.getGlInterface().setFilter(new NegativeFilterRender());
                return true;
            case R.id.pixelated:
                rtmpCamera1.getGlInterface().setFilter(new PixelatedFilterRender());
                return true;
            case R.id.polygonization:
                rtmpCamera1.getGlInterface().setFilter(new PolygonizationFilterRender());
                return true;
            case R.id.rainbow:
                rtmpCamera1.getGlInterface().setFilter(new RainbowFilterRender());
                return true;
            case R.id.rgb_saturate:
                RGBSaturationFilterRender rgbSaturationFilterRender = new RGBSaturationFilterRender();
                rtmpCamera1.getGlInterface().setFilter(rgbSaturationFilterRender);
                //Reduce green and blue colors 20%. Red will predominate.
                rgbSaturationFilterRender.setRGBSaturation(1f, 0.8f, 0.8f);
                return true;
            case R.id.ripple:
                rtmpCamera1.getGlInterface().setFilter(new RippleFilterRender());
                return true;
            case R.id.rotation:
                RotationFilterRender rotationFilterRender = new RotationFilterRender();
                rtmpCamera1.getGlInterface().setFilter(rotationFilterRender);
                rotationFilterRender.setRotation(90);
                return true;
            case R.id.saturation:
                rtmpCamera1.getGlInterface().setFilter(new SaturationFilterRender());
                return true;
            case R.id.sepia:
                rtmpCamera1.getGlInterface().setFilter(new SepiaFilterRender());
                return true;
            case R.id.sharpness:
                rtmpCamera1.getGlInterface().setFilter(new SharpnessFilterRender());
                return true;
            case R.id.snow:
                rtmpCamera1.getGlInterface().setFilter(new SnowFilterRender());
                return true;
            case R.id.swirl:
                rtmpCamera1.getGlInterface().setFilter(new SwirlFilterRender());
                return true;
            case R.id.surface_filter:
                //You can render this filter with other api that draw in a surface. for example you can use VLC
                SurfaceFilterRender surfaceFilterRender = new SurfaceFilterRender();
                rtmpCamera1.getGlInterface().setFilter(surfaceFilterRender);
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.big_bunny_240p);
                mediaPlayer.setSurface(surfaceFilterRender.getSurface());
                mediaPlayer.start();
                //Video is 360x240 so select a percent to keep aspect ratio (50% x 33.3% screen)
                surfaceFilterRender.setScale(50f, 33.3f);
                spriteGestureController.setBaseObjectFilterRender(surfaceFilterRender); //Optional
                return true;
            case R.id.temperature:
                rtmpCamera1.getGlInterface().setFilter(new TemperatureFilterRender());
                return true;
            case R.id.text:
                setTextToStream();
                return true;
            case R.id.zebra:
                rtmpCamera1.getGlInterface().setFilter(new ZebraFilterRender());
                return true;
            default:
                return false;
        }
    }

    private void setTextToStream() {
        TextObjectFilterRender textObjectFilterRender = new TextObjectFilterRender();
        rtmpCamera1.getGlInterface().setFilter(textObjectFilterRender);
        textObjectFilterRender.setText("Hello world", 22, Color.RED);
        textObjectFilterRender.setDefaultScale(rtmpCamera1.getStreamWidth(),
                rtmpCamera1.getStreamHeight());
        textObjectFilterRender.setPosition(TranslateTo.CENTER);
        spriteGestureController.setBaseObjectFilterRender(textObjectFilterRender); //Optional
    }

    private void setImageToStream() {
        ImageObjectFilterRender imageObjectFilterRender = new ImageObjectFilterRender();
        rtmpCamera1.getGlInterface().setFilter(imageObjectFilterRender);
        imageObjectFilterRender.setImage(
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        imageObjectFilterRender.setDefaultScale(rtmpCamera1.getStreamWidth(),
                rtmpCamera1.getStreamHeight());
        imageObjectFilterRender.setPosition(TranslateTo.RIGHT);
        spriteGestureController.setBaseObjectFilterRender(imageObjectFilterRender); //Optional
        spriteGestureController.setPreventMoveOutside(false); //Optional
    }

    private void setGifToStream() {
        try {
            GifObjectFilterRender gifObjectFilterRender = new GifObjectFilterRender();
            gifObjectFilterRender.setGif(getResources().openRawResource(R.raw.banana));
            rtmpCamera1.getGlInterface().setFilter(gifObjectFilterRender);
            gifObjectFilterRender.setDefaultScale(rtmpCamera1.getStreamWidth(),
                    rtmpCamera1.getStreamHeight());
            gifObjectFilterRender.setPosition(TranslateTo.BOTTOM);
            spriteGestureController.setBaseObjectFilterRender(gifObjectFilterRender); //Optional
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Connection success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionFailedRtmp(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Connection failed. " + reason, Toast.LENGTH_SHORT)
                        .show();
                rtmpCamera1.stopStream();
                button.setText(R.string.start_button);
            }
        });
    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthErrorRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Auth error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Auth success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_start_stop:
                if (!rtmpCamera1.isStreaming()) {
                    if (rtmpCamera1.isRecording()
                            || rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                        button.setText(R.string.stop_button);
                        rtmpCamera1.startStream(etUrl.getText().toString());
                    } else {
                        Toast.makeText(this, "Error preparing stream, This device cant do it",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    button.setText(R.string.start_button);
                    rtmpCamera1.stopStream();
                }
                break;
            case R.id.switch_camera:
                try {
                    rtmpCamera1.switchCamera();
                } catch (CameraOpenException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.b_record:
                if (!rtmpCamera1.isRecording()) {
                    try {
                        if (!folder.exists()) {
                            folder.mkdir();
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                        currentDateAndTime = sdf.format(new Date());
                        if (!rtmpCamera1.isStreaming()) {
                            if (rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                                rtmpCamera1.startRecord(
                                        folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                                bRecord.setText(R.string.stop_record);
                                Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Error preparing stream, This device cant do it",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            rtmpCamera1.startRecord(folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                            bRecord.setText(R.string.stop_record);
                            Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        rtmpCamera1.stopRecord();
                        bRecord.setText(R.string.start_record);
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    rtmpCamera1.stopRecord();
                    bRecord.setText(R.string.start_record);
                    Toast.makeText(this,
                            "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    currentDateAndTime = "";
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        rtmpCamera1.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (rtmpCamera1.isRecording()) {
            rtmpCamera1.stopRecord();
            bRecord.setText(R.string.start_record);
            Toast.makeText(this,
                    "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();
            currentDateAndTime = "";
        }
        if (rtmpCamera1.isStreaming()) {
            rtmpCamera1.stopStream();
            button.setText(getResources().getString(R.string.start_button));
        }
        rtmpCamera1.stopPreview();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (spriteGestureController.spriteTouched(view, motionEvent)) {
            spriteGestureController.moveSprite(view, motionEvent);
            spriteGestureController.scaleSprite(motionEvent);
            return true;
        }
        return false;
    }
}