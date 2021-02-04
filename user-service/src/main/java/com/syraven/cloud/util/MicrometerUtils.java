package com.syraven.cloud.util;

import java.text.DecimalFormat;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/11/25 11:41
 */
public class MicrometerUtils {

    public static String Micrometer(String text){
        DecimalFormat df = null;
        if (text.indexOf(".") > 0){
            if(text.length() - text.indexOf(".") - 1 == 0){
                df = new DecimalFormat("###,##0.");
            }else if (text.length() - text.indexOf(".") -1 == 1){
                df = new DecimalFormat("###,##0.0");
            }else {
                df = new DecimalFormat("###,##0.00");
            }
        }else {
            df = new DecimalFormat("###,##0");
        }
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.0;
        }
        return df.format(number);
    }

}