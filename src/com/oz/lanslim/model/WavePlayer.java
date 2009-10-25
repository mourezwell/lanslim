package com.oz.lanslim.model;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.oz.lanslim.SlimLogger;


public class WavePlayer {

	private Clip sound;

	public WavePlayer(File pSoundFile) {
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(pSoundFile);
			DataLine.Info clipInfo = new DataLine.Info(Clip.class, stream.getFormat());
			
			if (AudioSystem.isLineSupported(clipInfo)) {
				sound = (Clip) AudioSystem.getLine(clipInfo);
				sound.open(stream);
			}
			else {
				SlimLogger.log("Unable to play sound due to DataLine not supported");
			}
		} 
		catch (UnsupportedAudioFileException e) {
			SlimLogger.logException("Unable to play sound due to ", e);
		}
		catch (LineUnavailableException e) {
			SlimLogger.logException("Unable to play sound due to ", e);
		}
		catch (IOException e) {
			SlimLogger.logException("Unable to play sound due to ", e);
		}
	}

	
	public synchronized void play() {
		if (sound != null) {
			if (sound.isActive()) {
				sound.stop();
			}
			sound.setFramePosition(0);
			sound.loop(0);
		}
	}

}