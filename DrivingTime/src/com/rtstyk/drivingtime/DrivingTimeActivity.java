package com.rtstyk.drivingtime;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.format.Time;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DrivingTimeActivity extends Activity implements OnInitListener {
	
	
	private static final int MY_DATA_CHECK_CODE = 0;
	SeekBar currentSeekBar;
	SeekBar extraSpeedSeekBar;
	SeekBar remainingDistanceSeekBar;
	
	TextView currentSpeedText;
	TextView remainingDistance;
	TextView extraSpeedText;
	
	TextView savedTimeText;
	TextView savedTimeText2;
	
	int speed=0;
	int minSpeedOffset=40;
	int extraSpeed=0;
    int distance=0;
    
    SimpleDateFormat df = new SimpleDateFormat("HH:mm");

    private TextToSpeech mTts;
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                    TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
    
    public void saySomething(String text){
    	
    	
    	//String myText1 = "Did you sleep well?";
    	//String myText2 = "I hope so, because it's time to wake up.";
    	mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    	mTts.speak(text, TextToSpeech.QUEUE_ADD, null);
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Bitmap bitmap  = Bitmap.createBitmap(width, height, config)
        
        setContentView(R.layout.main);          
        
        currentSeekBar = (SeekBar)findViewById(R.id.currentSpeedBar);
        currentSeekBar.setProgress(5);
        
        extraSpeedSeekBar = (SeekBar)findViewById(R.id.extraSpeedBar);
        extraSpeedSeekBar.setProgress(15);
        
        remainingDistanceSeekBar = (SeekBar)findViewById(R.id.distanceRemaining);
        remainingDistanceSeekBar.setProgress(100);

        
        Drawable d = currentSeekBar.getProgressDrawable();
        Rect bounds = d.getBounds();
        d.setVisible(false, true);
        
        currentSeekBar.setProgressDrawable(d);
        currentSeekBar.getProgressDrawable().setBounds(bounds);
        
        currentSpeedText = (TextView)findViewById(R.id.currentSpeedValue);
        extraSpeedText = (TextView)findViewById(R.id.extraSpeedValue);
        remainingDistance =  (TextView)findViewById(R.id.distanceRemainingText);
        
        savedTimeText = (TextView)findViewById(R.id.savedTime);
        savedTimeText2 = (TextView)findViewById(R.id.savedTime2);

        changeExtraSpeed(25);
        changeRemainingDistance(100);
        changeSpeed(15);

        recalculate();
        
        currentSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				changeSpeed(seekBar.getProgress());
		        recalculate();
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

				changeSpeed(seekBar.getProgress());
		        recalculate();
				//changeSpeed(seekBar.getProgress());
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				changeSpeed(seekBar.getProgress());
				recalculate();
				// TODO Auto-generated method stub
				
			}
		});
        
        
        extraSpeedSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				changeExtraSpeed(seekBar.getProgress());
		        recalculate();
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

				changeExtraSpeed(seekBar.getProgress());
		        recalculate();
				//changeExtraSpeed(seekBar.getProgress());
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				changeExtraSpeed(seekBar.getProgress());
		        recalculate();
				// TODO Auto-generated method stub
				
			}
		});
        
        
        remainingDistanceSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				changeRemainingDistance(seekBar.getProgress());
		        recalculate();
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

				//changeRemainingDistance(seekBar.getProgress());
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				changeRemainingDistance(seekBar.getProgress());
		        recalculate();
				// TODO Auto-generated method stub
				
			}
		});
        
        
    }
    
    public void changeSpeed(int change)
    {
    	speed=minSpeedOffset+change;
    	
    }
    

    
    public void changeExtraSpeed(int change)
    {
    	extraSpeed=minSpeedOffset+change;
    }
    
    
    
    public void changeRemainingDistance(int change)
    {
    	CharSequence speedC = change+" miles";
    	remainingDistance.setText(speedC);
    	distance=change;
    }
    
    
    public void recalculate()
    {
    	if (speed==0)
    		return;
    	
    	double timeOrig = (double)distance/(double)speed;
    	timeOrig= timeOrig*60;
    	int timeOrigH = (int)timeOrig/60;
    	int timeOrigM = (int)timeOrig%60;
    	
    	
    	Date d = new Date();
    	d.setHours(timeOrigH);
    	d.setMinutes(timeOrigM);
    	
    	double timeNew = (double)distance/((double)(extraSpeed));
    	//double timeNew2 = (double)distance/((double)(extraSpeed));
    	timeNew = timeNew*60;
    	int timeNewH = (int)timeNew/60;
    	int timeNewM = (int)timeNew%60;
    	
    	Date d2 = new Date();
    	d2.setHours(timeNewH);
    	d2.setMinutes(timeNewM);
    	
    	
    	int diff= (int)(timeOrig-timeNew);
    	
    	currentSpeedText.setText(df.format(d)+" at "+speed+" mph");
    	
    	//extraSpeedText.setText(df.format(d2)+" at "+(speed+extraSpeed)+" mph");
    	extraSpeedText.setText(df.format(d2)+" at "+extraSpeed +" mph");
    	
    	String result = "Driving at "+extraSpeed+" mph vs "+speed+" mph over "+distance +" miles saves "+diff+" minutes";
    	//savedTimeText.setText((int)timeOrigH+":"+timeOrigM+" at "+speed+" mph");
    	savedTimeText2.setText(result);
    	
    	//saySomething(result);
    	
    }

	@Override
	public void onInit(int arg0) {
		
		savedTimeText2.setText("on init");
		
		//Intent checkIntent = new Intent();
    	//checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
    	//startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		
	}
    
}