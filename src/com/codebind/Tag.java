package com.codebind;
import java.io.FileWriter;
import java.io.IOException;
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
    void write(FileWriter write) throws IOException {
        write.append(pos+" ");
        write.append(len+" ");
        write.append(Integer.valueOf(nex)+" ");
    }

}
