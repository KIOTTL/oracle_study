package com.sist.data;
// xDH6LhYQcOAc2a2aaypS
// jrx0pIfYsH
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
public class NaverNewsMain {

	
	public static String newsData(String ss)
	{
		
		String clientId = "xDH6LhYQcOAc2a2aaypS"; //애플리케이션 클라이언트 아이디값"
        String clientSecret = "jrx0pIfYsH"; //애플리케이션 클라이언트 시크릿값"


        String text = null;
        try {
            text = URLEncoder.encode(ss, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }


        String apiURL = "https://openapi.naver.com/v1/search/news.json?query=" + text;    // json 결과
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과


        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);


        System.out.println(responseBody);
        return responseBody;
	}
	public static ArrayList<News> newsAllData() {
		ArrayList<News> list = new ArrayList<News>();
        String json=newsData("맛집");
        
        try
        {
        	JSONParser jp=new JSONParser();
        	JSONObject root=(JSONObject)jp.parse(json);
        	JSONArray arr=(JSONArray)root.get("items");
        	System.out.println(arr.size());
        	for(int i=0;i<arr.size();i++)
        	{
        		JSONObject obj=(JSONObject)arr.get(i);
        		System.out.println(obj.get("title"));
        		//System.out.println(obj.get("link"));
        		String desc=(String)obj.get("description");
        		desc=desc.replaceAll("[^가-힣 ]", "");
        		desc=desc.replace("<", "");
        		desc=desc.replace("</", "");
        		desc=desc.replace(">", "");
        		desc=desc.replace("&", "");
        		desc=desc.replace(";", "");
        		desc=desc.replaceAll("[A-Za-z]", "");
        		System.out.println(desc);
        		//String d=(String)obj.get("pubDate");
        		//Date date=new Date(d);
        		//System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(date));
        		News n = new News();
        		n.setTitle((String)obj.get("title"));
        		n.setDescription(desc);
        		list.add(n);
        	}
        			
        }catch(Exception ex){ex.printStackTrace();}
        return list;
    }


    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

}