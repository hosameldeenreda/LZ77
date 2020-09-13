package com.codebind;
import java.io.IOException;
import java.io.RandomAccessFile;
public class Tag {
    public int pos;
    public int len;
    public char nex;
    Tag(int pos,int len,char nex){
        this.pos=pos;
        this.len=len;
        this.nex=nex;
    }
    Tag(char nex){
        this.pos=0;
        this.len=0;
        this.nex=nex;
    }
    Tag(){
        this.pos=0;
        this.len=0;
        this.nex=' ';
    }
    void read(RandomAccessFile f) throws IOException {
        byte temp ;
        temp = f.readByte();
        pos =temp;
        temp = f.readByte();
        len =temp;
        char tempch = 0;
        tempch = f.readChar();
        nex =tempch;
    }
    void write(RandomAccessFile f) throws IOException {
        f.writeByte(pos);
        f.writeByte(len);
        f.writeChar(nex);
    }

}
