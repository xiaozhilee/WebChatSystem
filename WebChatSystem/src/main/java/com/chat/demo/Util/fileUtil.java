package com.chat.demo.Util;


import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class fileUtil {
    public boolean upload_img(MultipartFile file,String Path,String filename){
        String save_Path=Path+filename;
        File dest=new File(save_Path);
        if(dest.exists()) dest.delete();
        if(!dest.getParentFile().exists())
            dest.getParentFile().mkdir();
        try{
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
