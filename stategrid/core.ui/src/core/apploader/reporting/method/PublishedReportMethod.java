package core.apploader.reporting.method;

/**
 * TODO ˵��
 * @author zhangfan
 * @date 2012/1/13
 * @Description ͨ��HttpClient�Է��������в��� �ϴ�,����,ɾ���ļ�
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
 * @Description ͨ��HttpClient�Է��������в��� �ϴ�,����,ɾ���ļ�
 */

public class PublishedReportMethod {
	public static void main(String[] args) throws IOException{
		String ServerPath = "http://192.168.9.36:8080/birt/";
		String fileName = "Reports/���ⱨ��.rptdesign";
		String LocalPath = "d:\\itsm\\test\\";
		DownLoadFileFromServerToLocal(ServerPath,fileName,LocalPath);
		
		fileName = "����������.rpt";
		LocalPath = "D:/itsm/Reports";
		UpLoadFileFromLocalToServer(ServerPath,fileName,LocalPath);
		
		fileName = "Ա������.rpt";
		LocalPath = "d:/itsm/Reports/";
		DeleteFileFromServer(ServerPath,fileName,LocalPath);
	}

	/**
	 * @category �ӷ����������ļ�������
	 * TODO �ӷ����������ļ�������
	 * @param ServerPath	�����������ַ
	 * @param fileName		���ϴ��ı����ļ���
	 * @param LocalPath		�������ڱ���·��
	 * @return outString	״̬ע��~״̬��Ϣ
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
            //�ж��ļ��Ƿ����
            if (!storeFile.exists()) {
            	storeFile.mkdirs();
            }
            FileOutputStream output = new FileOutputStream(fileName,true);
            output.write(responseBody);
            output.close();
            
            outString = "true~�ļ����سɹ�";
        } catch (HttpException e) {
			outString = "false~����ͨ��ʧ��";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			outString = "false~����δ�������ļ���ȡʧ��";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}finally{
			getMethod.releaseConnection();
		}
        return outString;
    } 
	
	/**
	 * @category �ӱ����ϴ��ļ���������
	 * TODO �ӱ����ϴ��ļ���������
	 * @param ServerPath	�����������ַ
	 * @param fileName		���ϴ��ı����ļ���
	 * @param LocalPath		�������ڱ���·��
	 * @return outString	״̬ע��~״̬��Ϣ
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
					outString = "true~�����ɹ�";
					System.out.println("statusLine>>>" + outString);
				}else if(strResponse.equalsIgnoreCase("Upload Failed!!")){
					outString = "false~����ʧ��";
					System.out.println("statusLine>>>" + outString);
				}
			}
		} catch (HttpException e) {
			outString = "false~����ͨ��ʧ��";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			outString = "false~����δ�������ļ���ȡʧ��";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}finally{
			mPost.releaseConnection();
	    }
        return outString;
	}
	/**
	 * @category ɾ�����������ļ�
	 * TODO ɾ�����������ļ�
	 * @param ServerPath	�����������ַ
	 * @param fileName		���ϴ��ı����ļ���
	 * @param LocalPath		�������ڱ���·��
	 * @return outString	״̬ע��~״̬��Ϣ
	 */
	public static String DeleteFileFromServer(String ServerPath,String fileName,String LocalPath){
		String outString = "";
		HttpClient httpClient = new HttpClient();
		String url = ServerPath + "/FileDelete.jsp";
		httpClient.setConnectionTimeout(8000);
		PostMethod postMethod = new PostMethod();
		try {
			// ������������ֵ
			// String fileName = "Ա������.rpt";
			// String filePath = "d:/itsm/Reports/";
			NameValuePair[] data = { new NameValuePair("fileName", fileName),new NameValuePair("filePath", LocalPath) };
			System.out.println(LocalPath+fileName);
			
			// ������ֵ����postMethod��
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
					outString = "true~ɾ���ɹ�";
				}else if(strResponse.equalsIgnoreCase("Delete Failed!!")){
					outString = "false~ɾ��ʧ��";
				}
			}
		} catch (HttpException e) {
			outString = "false~ͨ��ʧ��";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			outString = "false~����δ�������ļ���ȡʧ��";
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}finally{
			postMethod.releaseConnection();
		}
		return outString;
	}
}
