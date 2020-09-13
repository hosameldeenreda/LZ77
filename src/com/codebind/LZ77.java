package com.codebind;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
class LZ77 {
    static final int windowSize=8;
    private static  ArrayList<Tag> compress(String  text){
        int textLength=text.length();
        if(textLength==0)
            throw new RuntimeException("text cannot be empty String or null");
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
                        } else
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
    private static String decompres(ArrayList<Integer> tags){
        if(tags.size()==0)
            throw new RuntimeException("tags cannot be empty or null");
        String text="";
        Hashtable<Integer, String> dictionary = new Hashtable<>();
        String old= String.valueOf((char)tags.get(0).intValue());
        text+=old;
        int dictionaryCounter=256;//out of ascii
        for (int currentTag : tags.subList(1,tags.size())){
            if(currentTag<256){
                // tag is represent one character
                text+=(char)currentTag;
                dictionary.put(dictionaryCounter,old+(char)currentTag);
                dictionaryCounter++;
                old= String.valueOf((char)currentTag);
            }
            else{
                String temp=dictionary.getOrDefault(currentTag, "-1");
                if (!temp.equals("-1")) {
                    //found at dictionary
                    text+=temp;
                    dictionary.put(dictionaryCounter,old+temp.charAt(0));
                    old= temp;
                }
                else{
                    text+=(old+old.charAt(0));
                    dictionary.put(dictionaryCounter,old+old.charAt(0));
                    old+=old.charAt(0);
                }
                dictionaryCounter++;
            }
        }
        return text;
    }
    private static String readTextFile(File file) throws IOException {
        if(!file.exists())
            throw new RuntimeException("file not found");
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        return (sc.next());
    }
    private static void writeTags(File file,ArrayList<Integer> tags)throws IOException {
        file.createNewFile(); // if file already exists will do nothing
        FileWriter write = new FileWriter(file);
        for (int currentTag : tags) {
            write.append(currentTag+" ");
        }
        write.flush();
        write.close();
    }
    private static ArrayList<Integer> readTagsFromFile(File file) throws IOException {
        if(!file.exists())
            throw new RuntimeException("file not found");
        Scanner sc = new Scanner(file);
        ArrayList<Integer> tags=new ArrayList<>();
        while (sc.hasNextInt()){
            tags.add(sc.nextInt());
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
            for(int i=0;i<tags.size();i++){
                System.out.println(tags.get(i).pos+" "+tags.get(i).len+" "+tags.get(i).nex);
            }
            //writeTags(compressed, tags);
        }  catch (Exception ex) {
            System.out.print(ex.toString());
        }
    }
    public static void decompres(File  compressed,File uncompressed) throws IOException {
        try{
            writeText(uncompressed,decompres(readTagsFromFile(compressed)));
        }  catch (Exception ex) {
            System.out.print(ex.toString());
        }

    }
}
