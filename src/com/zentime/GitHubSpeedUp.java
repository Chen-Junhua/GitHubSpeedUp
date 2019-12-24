package com.zentime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitHubSpeedUp {

    public static void main(String[] args) {
        //读取host文件
        ProjectFile hostFile = new ProjectFile(Constants.hostsFilePath + "hosts", "GBK");
        //备份hosts文件
        try {
            hostFile.copyTo(Constants.hostsFilePath + "hosts.bak");
            System.out.println("hosts文件备份成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("hosts文件备份失败，请以管理员身份运行");
        }
        //获取IP地址
        System.out.println(">>>>>开始查询IP地址");
        IPInitializer ipInitializer = new IPInitializer(Constants.domainArray);
        List<String> hostsFileLineList = ipInitializer.generateHostFileLineList();
        //清理hosts中的旧记录
        System.out.println("清理hosts中的旧记录");
        List<String> newContentList = cleanOldContent(hostFile.getContentList());
        newContentList.addAll(hostsFileLineList);
        //写入hosts
        System.out.println("写入hosts文件");
        hostFile.setContentList(newContentList);
        hostFile.saveFile();
        //写入完成，刷新DNS
        System.out.println("写入完成，刷新DNS");
        try {
            Runtime.getRuntime().exec("ipconfig /flushdns");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> cleanOldContent(List<String> oldContentList) {
        List<String> newContentList = new ArrayList<String>();
        for (String oldLineString : oldContentList) {
            if(oldLineString.indexOf("#")==-1&&oldLineString.indexOf(" ")>-1)
            {
                String[] oldLineSplitArray=oldLineString.split(" ");
                if(oldLineSplitArray.length>1)
                {
                    String oldDomain=oldLineSplitArray[1];
                    if(!isStringInArray(oldDomain,Constants.domainArray))
                    {
                        newContentList.add(oldLineString);
                    }
                }
            }
            else
            {
                newContentList.add(oldLineString);
            }
        }
        return newContentList;
    }

    private static boolean isStringInArray(String targetString,String[] sourceArray)
    {
        boolean flag=false;
        for(int i=0;i<sourceArray.length;i++)
        {
            if(targetString.equals(sourceArray[i]))
            {
                flag=true;
            }
        }
        return flag;
    }
}
