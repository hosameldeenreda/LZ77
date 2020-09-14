package com.codebind;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
class LZ77 {
    static final int windowSize=8;
    private static void checkIfTextIsEmpty(String text){
        if(text.length()==0) {
            System.out.println("hosaasasasa");
            throw new RuntimeException("text cannot be empty String or null");
        }
    }
    private static void checkIfFileIsExist(File file){
        if(!file.exists())
            throw new RuntimeException("file not found");
    }
    private static  ArrayList<Tag> compress(String  text){
        int textLength=text.length();
        checkIfTextIsEmpty(text);
        ArrayList<Tag> outputTags = new ArrayList<>();
        outputTags.add(new Tag(0,0,text.charAt(0)));
        for(int i=1;i<textLength;i++){
            int maxLength=0,maxPos=0;
            for(int j=i-1;j>=0&&j>=i-windowSize;j--){
                if(text.charAt(i)==text.charAt(j)) {
                    int pos = i - j, len = 0, ii = i;
                    for (int k = j; ii < textLength - 1; k++) {
                        if (text.charAt(k) == text.charAt(ii)) {
                            ii++;
                            len++;
                        }
                        else
                            break;
                    }
                    if (len > maxLength) {
                        maxLength = len;
                        maxPos = pos;
                    }
                }
            }
            i+=maxLength;
            Tag tag=new Tag(maxPos,maxLength,text.charAt(i));
            outputTags.add(tag);
        }
        return outputTags;
    }
    private static String decompress(ArrayList<Tag> tags){
        String text="";
        for(int i=0;i<tags.size();i++){
            Tag temp=tags.get(i);
            for(int j=text.length()-temp.pos,k=0;k<temp.len;j++,k++)
                text+=text.charAt(j);
            text+=temp.nex;
        }
        return text;
    }
    private static String readTextFile(File file) throws IOException {
        checkIfFileIsExist(file);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        return (sc.next());
    }
    private static void writeTags(File file,ArrayList<Tag> tags)throws IOException {
        file.createNewFile(); // if file already exists will do nothing
        FileWriter write = new FileWriter(file);
        for (Tag currentTag : tags) {
            currentTag.write(write);
        }
        write.flush();
        write.close();
    }
    private static ArrayList<Tag> readTagsFromFile(File file) throws IOException {
        checkIfFileIsExist(file);
        Scanner sc = new Scanner(file);
        ArrayList<Tag> tags=new ArrayList<>();
        while (sc.hasNextInt()){
            tags.add(new Tag(sc.nextInt(),sc.nextInt(), (char) sc.nextInt()));
        }
        return tags;
    }
    private static void writeText(File file,String data) throws IOException {
        file.createNewFile(); // if file already exists will do nothing
        FileWriter write = new FileWriter(file);
        write.append(data);
        write.flush();
        write.close();
    }
    public static  void compress(File  original,File compressed) throws IOException {
        try {
            ArrayList<Tag> tags = compress(readTextFile(original));
            writeTags(compressed, tags);
        }  catch (Exception ex) {
            System.out.print(ex.toString());
        }
    }
    public static void decompress(File  compressed,File uncompressed) throws IOException {
        try{
            writeText(uncompressed,decompress(readTagsFromFile(compressed)));
        }  catch (Exception ex) {
            System.out.print(ex.toString());
        }

    }
}
