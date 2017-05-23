package com.dongwt.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description:
 * @author: dongwt
 * @create: 2017-05-22 15:46
 **/
public class FileChannelTest {

    public static void main(String[] args) throws Exception {
        String path = "c:" + File.separator + "11.txt";
        RandomAccessFile file = new RandomAccessFile(path,"rw");
        FileChannel channel = file.getChannel();
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        int length = channel.read(readBuffer);
        while (length != -1){
            readBuffer.flip();
            while (readBuffer.hasRemaining()){
                System.out.print((char) readBuffer.get());
            }
            readBuffer.clear();
            length = channel.read(readBuffer);
        }

        file.close();
    }

}
