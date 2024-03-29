package remember;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.sound.sampled.AudioInputStream;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;
import marytts.signalproc.effects.AudioEffect;
import marytts.signalproc.effects.AudioEffects;

/**
 * @author GOXR3PLUS
 *
 */
public class TTS {

    static AudioPlayer ap;
    static MaryInterface marytts;

//  Constructor
    public TTS() {
        try {
            marytts = new LocalMaryInterface();
            marytts.setAudioEffects("Rate(durScale:8.0)");

        } catch (MaryConfigurationException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Convert text to speech
     *
     * @param text
     *            Text to be converted to speech.
     * @param daemon
     *            <b>True</b> The thread that will start the text to speech Player will be a daemon Thread <br>
     *            <b>False</b> The thread that will start the text to speech Player will be a normal non daemon Thread
     * @param join
     *            <br>
     *            <b>True</b> The current Thread calling this method will wait(blocked) until the Thread which is playing the Speech finish <br>
     *            <b>False</b> The current Thread calling this method will continue freely after calling this method
     */
    public void speak(String text , float gainValue , boolean daemon , boolean join) {
        try (AudioInputStream audio = marytts.generateAudio(text)) {
            // Create new AudioPlayer.
            ap = new AudioPlayer();
            ap.setAudio(audio);
            ap.setGain(gainValue);
            ap.setDaemon(daemon);
            ap.start();
            if (join)
                ap.join();

        } catch (IOException|InterruptedException|SynthesisException iis) {
            iis.printStackTrace();
        }
    }

    //----------------------GETTERS---------------------------------------------------//

    /**
     * Available voices in String representation
     *
     * @return The available voices for MaryTTS
     */
    public Collection<Voice> getAvailableVoices() {
        return Voice.getAvailableVoices();
    }

    /**
     * @return the marytts
     */
    public MaryInterface getMarytts() {
        return marytts;
    }

    /**
     * Return a list of available audio effects for MaryTTS
     *
     * @return
     */
    public List<AudioEffect> getAudioEffects() {
        return StreamSupport.stream(AudioEffects.getEffects().spliterator(), false).collect(Collectors.toList());
    }

    //----------------------SETTERS---------------------------------------------------//

    /**
     * Change the default voice of the MaryTTS
     *
     * @param voice
     */
    public void setVoice(String voice) {
        marytts.setVoice(voice);
    }

}