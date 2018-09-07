import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Helloworld {
	
	public static void main(String[] atgs) throws LineUnavailableException, IOException{
		String str = "123,221; 243, pol";
		String[] arr = str.split(";");
		System.out.println(arr[1].split(",")[1]);
		
		// Source : http://www.java2s.com/Tutorial/Java/0120__Development/PlayingStreamingSampledAudio.htm
		String gongFile = "C:/Users/pulkit sharma/Documents/workspace/GameGameGame/res/background/hit.wav";
		File nf = new File(gongFile);
		AudioInputStream stream = null;
		try {
			stream = AudioSystem.getAudioInputStream(nf);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		    AudioFormat format = stream.getFormat();
		    if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
		      format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format
		          .getSampleRate(), format.getSampleSizeInBits() * 2, format
		          .getChannels(), format.getFrameSize() * 2, format.getFrameRate(),
		          true); // big endian
		      stream = AudioSystem.getAudioInputStream(format, stream);
		    }

		    SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, stream
		        .getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
		    SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
		    line.open(stream.getFormat());
		    line.start();

		    int numRead = 0;
		    byte[] buf = new byte[line.getBufferSize()];
		    while ((numRead = stream.read(buf, 0, buf.length)) >= 0) {
		      int offset = 0;
		      while (offset < numRead) {
		        offset += line.write(buf, offset, numRead - offset);
		      }
		    }
		    line.drain();
		    line.stop();
		  }
	}