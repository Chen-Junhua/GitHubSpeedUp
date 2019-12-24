package com.zentime;

import java.nio.channels.FileChannel;
import java.util.*;
import java.io.*;

public class ProjectFile
{
	// 项目文件类，集成文件创建，写入，等功能
	private String fileType = "";// 文件类型
	private String fileEncoding = "UTF-8";// 文件编码
	private List<String> contentList;// 文件内容

	private String filePath = "";// 文件路径，不包含文件名
	private String fileName = "";// 文件名，不包含后缀名和路径
	private String fullFileName = "";



    private File fileObj;

	public ProjectFile(String fullFileName, String fileEncoding)
	{
		contentList = new ArrayList<String>();
		// 初始化文件名，文件路径，文件编码等参数
		this.fullFileName = fullFileName;
		this.fileEncoding = fileEncoding;
		if(fullFileName.indexOf(".")>-1)
        {
            this.fileType = fullFileName.substring(fullFileName.lastIndexOf('.') + 1, fullFileName.length());
            this.fileName = fullFileName.substring(fullFileName.lastIndexOf('/') + 1, fullFileName.lastIndexOf('.'));
        }
		else
        {
            this.fileType = "";
            this.fileName = fullFileName.substring(fullFileName.lastIndexOf('/') + 1, fullFileName.length());
        }
		if(fullFileName.indexOf("/")>-1)
		{
			this.filePath = fullFileName.substring(0, fullFileName.lastIndexOf('/'));
		}
		else if(fullFileName.indexOf("\\")>-1)
		{
			this.filePath=fullFileName.substring(0, fullFileName.lastIndexOf('\\'));
		}
//		try
//		{
//			this.filePath = fullFileName.substring(0, fullFileName.lastIndexOf('/'));
//		}
//		catch(Exception e)
//		{
//			this.filePath=fullFileName.substring(0, fullFileName.lastIndexOf('\\'));
//		}
		try
		{
			//如果文件目录不存在，则创建文件目录
			File folderPath=new File(filePath);
			if(!folderPath.exists())
			{
				folderPath.mkdirs();
			}
			// 如果文件存在，则初始化文件内容
			File tempFile = new File(fullFileName);
			this.fileObj=tempFile;
			if (tempFile.exists())
			{
				BufferedReader tempFileBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile), fileEncoding));
				String lineString;
				while ((lineString = tempFileBufferedReader.readLine()) != null)
				{
					contentList.add(lineString);
				}
				tempFileBufferedReader.close();
			}
			else
			{
				tempFile.createNewFile();
			}
		}
		catch (Exception e)
		{
			System.out.println(fileName + " Read Error");
			e.printStackTrace();
		}
	}

	// 保存，生成新文件，删除旧文件
	public void saveFile()
	{
		File tempFile = new File(fullFileName);
		try
		{
			if (tempFile.exists())
			{
				tempFile.delete();
			}
			tempFile.createNewFile();
			//写入目标文件
			Writer tempFileWriter = new OutputStreamWriter(new FileOutputStream(tempFile), fileEncoding);
			for(int counter_1=0;counter_1<contentList.size();counter_1++)
			{
				tempFileWriter.write(contentList.get(counter_1)+"\n");
			}
			tempFileWriter.flush();
			tempFileWriter.close();
		}
		catch(Exception e)
		{
			System.out.println(fileName+" File Save Error");
			e.printStackTrace();
		}
	}

	//在flag位置之前插入文件片段，如果flag为空则在文件末尾插入
	public void insertContent(String insertFlag,List<String> insertList)
	{
		boolean flag=false;
		List<String> tempList_1=new ArrayList<String>();
		List<String> tempList_2=new ArrayList<String>();
		if(!insertFlag.equals(""))
		{
			for(int counter_1=0;counter_1<contentList.size();counter_1++)
			{
				String lineString=contentList.get(counter_1);
				if(lineString.indexOf(insertFlag)>=0||flag)
				{
					flag=true;
					tempList_2.add(lineString);
				}
				else
				{
					tempList_1.add(lineString);
				}
			}
		}
		else
		{
			for(int counter_1=0;counter_1<contentList.size();counter_1++)
			{
				String lineString=contentList.get(counter_1);
				tempList_1.add(lineString);
			}
		}
		contentList.clear();
		for(int counter=0;counter<tempList_1.size();counter++)
		{
			contentList.add(tempList_1.get(counter));
		}
		for(int counter=0;counter<insertList.size();counter++)
		{
			contentList.add(insertList.get(counter));
		}
		for(int counter=0;counter<tempList_2.size();counter++)
		{
			contentList.add(tempList_2.get(counter));
		}
	}

    public void copyTo(String destFilePathAndName) throws IOException {
        File sourceFile = this.fileObj;
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            File destFile = new File(destFilePathAndName);
            inputChannel = new FileInputStream(sourceFile).getChannel();
            outputChannel = new FileOutputStream(destFile).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }

    }

	//清空文件内容
	public void cleanContent()
	{
		contentList.clear();
	}

	public String getFileType()
	{
		return fileType;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	public String getFileEncoding()
	{
		return fileEncoding;
	}

	public void setFileEncoding(String fileEncoding)
	{
		this.fileEncoding = fileEncoding;
	}

	public List<String> getContentList()
	{
		return contentList;
	}

	public void setContentList(List<String> contentList)
	{
		this.contentList = contentList;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFullFileName()
	{
		return fullFileName;
	}

	public void setFullFileName(String fullFileName)
	{
		this.fullFileName = fullFileName;
	}

    public File getFileObj() {
        return fileObj;
    }

    public void setFileObj(File fileObj) {
        this.fileObj = fileObj;
    }
}
