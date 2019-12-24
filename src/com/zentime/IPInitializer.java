package com.zentime;

import java.util.ArrayList;
import java.util.List;

public class IPInitializer {
    public String[] getDomainArray() {
        return domainArray;
    }

    public void setDomainArray(String[] domainArray) {
        this.domainArray = domainArray;
    }

    private String[] domainArray;

    public IPInitializer(String[] inputArray) {
        this.domainArray = inputArray;
    }

    public List<String> generateHostFileLineList() {
        List<String> lineList = new ArrayList<String>();

        for (int i = 0; i < domainArray.length; i++) {
            String domainString = domainArray[i];
            //https://github.com.ipaddress.com/
            //构造查询URL
            String[] domainStringSplitArray = domainString.split("[.]");
            int domainStringSplitArrayLength = domainStringSplitArray.length;
            if (domainStringSplitArrayLength >= 2) {
                String queryURL = "https://" + domainStringSplitArray[domainStringSplitArrayLength - 2] + "."
                        + domainStringSplitArray[domainStringSplitArrayLength - 1] + ".ipaddress.com/"
                        + domainString;
                System.out.println(">>>>>查询域名："+domainString);
                List<String> htmlList = new ArrayList<String>();
                HtmlGetter htmlGetter = new HtmlGetter();
                try {
                    htmlList = htmlGetter.getHtml(queryURL);
                    System.out.println("查询成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("查询失败");
                }
                //获得html
                for (String htmlLine : htmlList) {
                    //结果为单行，解析ip地址标签
                    String infoString = getSubStringByFlag(htmlLine, "<th>IP Address</th>", "<th>Web Server Location</th>");
                    infoString = getSubStringByFlag(infoString, "<li>", "</li>");

                    if ("".equals(infoString)) {
                        infoString = getSubStringByFlag(htmlLine, "<h3>What IP addresses", "<td><h3>In what country");
                        infoString = getSubStringByFlag(infoString, "<li>", "</li>");
                        infoString = getSubStringByFlag(infoString, "\">", "</a>");
                        infoString = infoString.substring(2);
                    } else {
                        infoString = infoString.substring(4);
                    }
                    lineList.add(infoString+" "+domainString);
                }
            }
        }

        return lineList;
    }

    public String getSubStringByFlag(String sourceContent, String beginFlag, String endFlag) {
        String retString = "";
        int beginInt = sourceContent.indexOf(beginFlag);
        int endInt = sourceContent.indexOf(endFlag);
        if (beginInt > -1 && endInt > -1 && endInt > beginInt) {
            retString = sourceContent.substring(beginInt, endInt);
        }
        return retString;
    }

}
