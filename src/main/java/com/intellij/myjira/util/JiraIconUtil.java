package com.intellij.myjira.util;

import static java.util.Objects.isNull;

import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.ImageUtil;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.imgscalr.Scalr;
import org.jetbrains.annotations.Nullable;

public class JiraIconUtil {

    private static final int SMALL_ICON = 16;


    public static Icon getIcon(@Nullable String iconUrl){
        if(StringUtil.isEmpty(iconUrl)){
            return null;
        }

        try {
            return IconLoader.findIcon(new URL(iconUrl));
        } catch (MalformedURLException e) {
            return null;
        }
    }


    public static Icon getSmallIcon(@Nullable String iconUrl){
        Icon icon = getIcon(iconUrl);
        if(isNull(icon)){
            return null;
        }

        Image image = IconLoader.toImage(icon);
        BufferedImage bufferedImage = ImageUtil.toBufferedImage(image);
        BufferedImage resizeImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, SMALL_ICON);

        return new ImageIcon(resizeImage);
    }

}
