package com.vote.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    //文件的复制
    public static File copyFile(File srcfile) throws IOException{
        //文件的复制
        FileInputStream fis=new FileInputStream(srcfile);


        String path = System.getProperty("user.dir");
        System.out.print(path);
        File desfile=new File(path+srcfile.getName());
        FileOutputStream fos=new FileOutputStream(desfile);
        int len;
        byte[] bys=new byte[1024];
        while((len=fis.read(bys))!=-1){
            fos.write(bys, 0, len);
            fos.flush();
        }
        fos.close();
        fis.close();
        srcfile.delete();
        return desfile;
    }


}
