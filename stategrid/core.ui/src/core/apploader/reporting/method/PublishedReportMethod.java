package core.apploader.reporting.method;

/**
 * TODO 说明
 * @author zhangfan
 * @date 2012/1/13
 * @Description 通过HttpClient对服务器进行操作 上传,下载,删除文件
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import mainsoft.apache.commons.httpclient.HttpClient;
import mainsoft.apache.commons.httpclient.HttpException;
import mainsoft.apache.commons.httpclient.HttpStatus;
import mainsoft.apache.commons.httpclient.NameValuePair;
import mainsoft.apache.commons.httpclient.URI;
import mainsoft.apache.commons.httpclient.methods.GetMethod;
import mainsoft.apache.commons.httpclient.methods.MultipartPostMethod;
import mainsoft.apache.commons.httpclient.methods.PostMethod;
import mainsoft.apache.commons.httpclient.methods.multipart.FilePart;


/**
 * @author zhangfan
 * @date 2012/1/13
 * @Description 通过HttpClient对服务器进行操作 上传,下载,删除文件
 */

public class PublishedReportMethod {
	public static void main(String[] args) throws IOException{
		String ServerPath = "http://192.168.9.36:8080/birt/";
		String fileName = "Reports/问题报告.rptdesign";
		String LocalPath = "d:\\itsm\\test\\";
		DownLoadFileFromServerToLocal(ServerPath,fileName,LocalPath);
		
		fileName = "发布管理报告.rpt";
		LocalPath = "D:/itsm/Reports";
		UpLoadFileFromLocalToServer(ServerPath,fileName,LocalPath);
		
		fileName = "员工名单.rpt";
		LocalPath = "d:/itsm/Reports/";
		DeleteFileFromServer(ServerPath,fileName,LocalPath);
	}

	/**
	 * @category 从服务器下载文件到本地
	 * TODO 从服务器下载文件到本地
	 * @param ServerPath	报表服务器地址
	 * @param fileName		需上传的报表文件名
	 * @param LocalPath		报表所在本地路径
	 * @return outString	状态注记~状态信息
	 */
	public static String DownLoadFileFromServerToLocal(String ServerPath,String fileName,String LocalPath){
		String outString = "";
		HttpClient httpClient = new HttpClient();
	    GetMethod getMethod = new GetMethod();
        try{
        	ServerPath = ServerPath + "/";
        	LocalPath = LocalPath + "/";
        	System.out.println(fileName);
        	fileName = fileName.replaceAll("\\\\", "/").replaceAll("//","/");
			fileName = fileName.replaceAll("\\\\", "/").replaceAll("//","/");
        	if(fileName.indexOf("/")!=-1){
        		String[] a = fileName.split("/");
        		ServerPath = ServerPath + a[0];
        		LocalPath = LocalPath + a[0];
        		fileName = a[1];
        	}
        	String url = ServerPath +"/"+fileName;
        	System.out.println(url);
        	
        	getMethod.getParams().setContentCharset("UTF-8");
	    	getMethod.setURI(new URI(url,false,"UTF-8"));
	        int statusCode = httpClient.executeMethod(getMethod);
	        byte[] responseBody = null;
	        if (statusCode == HttpStatus.SC_OK){
	            System.err.println("Method failed: " + getMethod.getStatusLine());
	            responseBody = getMethod.getResponseBody();
	        }
            fileName = LocalPath +"/" + fileName;
            fileName = fileName.replaceAll("\\\\", "/").replaceAll("//","/");
            System.out.println("the download file full name=" + fileName);
            File storeFile = new File(LocalPath);
            //判断文件是否存在
            if (!storeFile.exists()) {
            	storeFile.mkdirs();
            }
            FileOutputStream output = new FileOutputStream(fileName,true);
            output.write(responseBody);
            output.close();
            
            outString = "true~文件下载成功";
        } catch (HttpException e) {
			outString = "false~网络通信失败";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			outString = "false~服务未开启或文件读取失败";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}finally{
			getMethod.releaseConnection();
		}
        return outString;
    } 
	
	/**
	 * @category 从本地上传文件到服务器
	 * TODO 从本地上传文件到服务器
	 * @param ServerPath	报表服务器地址
	 * @param fileName		需上传的报表文件名
	 * @param LocalPath		报表所在本地路径
	 * @return outString	状态注记~状态信息
	 */
	public static String UpLoadFileFromLocalToServer(String ServerPath,String fileName,String LocalPath){
		String outString = "";
		HttpClient client = new HttpClient();
        MultipartPostMethod mPost = new MultipartPostMethod();
        client.setConnectionTimeout(8000);
        String url = ServerPath + "/ProcessFileUpload1.jsp";
        try {
        	// Send any XML file as the body of the POST request
        	fileName = LocalPath + "/" + fileName;
            File file = new File(fileName);
            
            mPost.addParameter(file.getName(), file.getName(), file);

	        FilePart filePart = new FilePart("file",file);
			
			mPost.addPart(filePart);
			
			mPost.getParams().setContentCharset("UTF-8");
            mPost.setURI(new URI(url,false,"UTF-8"));
            
	        int statusCode = client.executeMethod(mPost);
	        if (statusCode == HttpStatus.SC_OK) {
				String strResponse = mPost.getResponseBodyAsString();
				System.out.println("statusLine>>>" + strResponse.trim());
				if(strResponse.equalsIgnoreCase("Upload Success!!")){
					outString = "true~发布成功";
					System.out.println("statusLine>>>" + outString);
				}else if(strResponse.equalsIgnoreCase("Upload Failed!!")){
					outString = "false~发布失败";
					System.out.println("statusLine>>>" + outString);
				}
			}
		} catch (HttpException e) {
			outString = "false~网络通信失败";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			outString = "false~服务未开启或文件读取失败";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}finally{
			mPost.releaseConnection();
	    }
        return outString;
	}
	/**
	 * @category 删除服务器端文件
	 * TODO 删除服务器端文件
	 * @param ServerPath	报表服务器地址
	 * @param fileName		需上传的报表文件名
	 * @param LocalPath		报表所在本地路径
	 * @return outString	状态注记~状态信息
	 */
	public static String DeleteFileFromServer(String ServerPath,String fileName,String LocalPath){
		String outString = "";
		HttpClient httpClient = new HttpClient();
		String url = ServerPath + "/FileDelete.jsp";
		httpClient.setConnectionTimeout(8000);
		PostMethod postMethod = new PostMethod();
		try {
			// 填入各个表单域的值
			// String fileName = "员工名单.rpt";
			// String filePath = "d:/itsm/Reports/";
			NameValuePair[] data = { new NameValuePair("fileName", fileName),new NameValuePair("filePath", LocalPath) };
			System.out.println(LocalPath+fileName);
			
			// 将表单的值放入postMethod中
			postMethod.setRequestBody(data);
			postMethod.getParams().setContentCharset("UTF-8");
			postMethod.setURI(new URI(url,false,"UTF-8"));
			
			httpClient.getParams().setContentCharset("UTF-8");
			int statusCode = httpClient.executeMethod(postMethod);
			System.out.println("statusLine>>>" + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				String strResponse = postMethod.getResponseBodyAsString();
				System.out.println("statusLine>>>" + strResponse.trim());
				if(strResponse.equalsIgnoreCase("Delete Success!!")){
					outString = "true~删除成功";
				}else if(strResponse.equalsIgnoreCase("Delete Failed!!")){
					outString = "false~删除失败";
				}
			}
		} catch (HttpException e) {
			outString = "false~通信失败";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			outString = "false~服务未开启或文件读取失败";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}finally{
			postMethod.releaseConnection();
		}
		return outString;
	}
}
