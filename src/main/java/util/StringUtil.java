package util;


import javax.servlet.http.Part;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
public  class StringUtil {
    private static Date date = new Date();

    public static String getUUIDRandomString(int length){
        return UUID.randomUUID().toString().replaceAll("-","")
                .substring(0,length);
    }

    public static String getNowTimeNumString(){
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss:SSSS");

        return ft.format(date).replaceAll("-","")
                .replaceAll(" ","")
                .replaceAll(":","");
    }

    public static String getNowTimeString(){
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

        return ft.format(date);
    }
    
    //yyyyMMddhhmmssSS
    public static String getSubTimeNumString(int startIndex,int length) {

    	String s = getNowTimeNumString();
    	return startIndex+length > s.length()?
    	 s.substring(startIndex):s.substring(startIndex, startIndex+length);
    }
    
    /**
     * 返回url中的参数
     * @param url
     * @return
     */
    public static String[] getUrlParameter(String url) {
    
    	String suffix =url.trim();
		if(url.indexOf(':') > 0)
    		 suffix = url.substring(url.indexOf(':')+1);
    	// 正则表达式中 // 表示/
    	return suffix.split("//");
    }

    /**
     * 获取文件名称
     */
    public static String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
