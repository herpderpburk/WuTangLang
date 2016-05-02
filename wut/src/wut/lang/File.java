package wut.lang;

/**
 * Created by josephburkey on 02/05/2016.
 */

import java.io.IOException;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;

public class File extends WutObject{

        // Taken from http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
        static public String read(String path) throws WutException {
        try {
            FileInputStream stream = new FileInputStream(new java.io.File(path));
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        /* Instead of using default, pass in a decoder. */
                return java.nio.charset.Charset.defaultCharset().decode(bb).toString();
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            throw new WutException(e);
        }
    }

    static public boolean exists(String path) throws WutException {
        return new java.io.File(path).exists();
    }

}