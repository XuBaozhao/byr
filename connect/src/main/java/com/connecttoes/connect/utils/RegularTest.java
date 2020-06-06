package com.connecttoes.connect.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于学习正则表达式，学习未果。。。
 */

public class RegularTest {
    public static void main(String[] args) {

        String returnXml = "<resultdescription>单据  16613dd7d9a00000000000000000000vouchergl0  开始处理...单据  16613dd7d9a00000000000000000000vouchergl0  处理完毕!"
                + "</resultdescription>"
                + "<content>2018.09-记账凭证-5</content>"
                + "<billpk></billpk><bdocid>16613dd7d9a00000000000000000000vouchergl0</bdocid>"
                + "<filename>vouchergld861102.xml</filename><resultcode>1</resultcode>"
                + "<resultdescription>单据  16613dd7d9a00000000000000000000vouchergl0  开始处理...单据  16613dd7d9a00000000000000000000vouchergl0  处理完毕!"
                + "</resultdescription>"
                + "<content>2018.09-记账凭证-6</content></sendresult>";
        String regex = "<content>(.*?)</content>";
        List<String> list = new ArrayList<String>();
        List<String> extvounoLists = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(returnXml);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }
        for (String str : list) {
            System.out.println(str);
            String[] strs = str.split("-");
            String strss = strs[strs.length-1];
            extvounoLists.add(strs[strs.length-1]);
        }

        for (String string : extvounoLists) {
            System.out.println(string);
        }
    }
}