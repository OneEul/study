package com.api.study;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.fasterxml.jackson.core.JsonParser;

@Controller
public class APIController {
	
	@GetMapping("test2")
	@ResponseBody
	public String test2() {
		String baseURL = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getUnityAirEnvrnIdexSnstiveAboveMsrstnList?pageNo=1&numOfRows=100&returnType=json&serviceKey=SVKLFoyKi9THT4PBiicEtZpP7l6sSBnn3XTAPdh1vA5USSOfDPr8UqKE%2FsaNa19P0e2a%2FOhEgR9nRSlpQ3bNyg%3D%3D";
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseURL); //url을
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY); //인코딩해야함
		WebClient webClient = WebClient.builder().uriBuilderFactory(factory).baseUrl(baseURL).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
		String response = webClient.get().retrieve().bodyToMono(String.class).block();
		return response;
	}

	@GetMapping("test")
	@ResponseBody
	public String testPost() {
		String jsonString = "false";
		String baseURL = "https://openapi.naver.com/v1/datalab/search";
		WebClient client = WebClient.builder()
		.defaultHeader("X-Naver-Client-Id", "EDTlucIFM0hgJIWRwyke").defaultHeader("X-Naver-Client-Secret", "V2xAjYIMxN")
		.baseUrl(baseURL).build();
	
		String requestBody = "{\"startDate\":\"2022-10-01\"," +
		"\"endDate\":\"2023-02-16\"," +
		"\"timeUnit\":\"month\"," +
		"\"keywordGroups\":[{\"groupName\":\"한글\"," + "\"keywords\":[\"한글\",\"korean\"]}," +
		"{\"groupName\":\"영어\"," + "\"keywords\":[\"영어\",\"english\"]}]," +
		"\"device\":\"pc\"," +
		"\"ages\":[\"1\",\"2\"]," +
		"\"gender\":\"f\"}";
	
		// client.post().bodyValue(requestBody);
		// jsonString = client.post().header("X-Naver-Client-Id", "EDTlucIFM0hgJIWRwyke").header("X-Naver-Client-Secret", "V2xAjYIMxN").bodyValue(requestBody).retrieve().bodyToMono(String.class).block();
	
		jsonString = client.post().bodyValue(requestBody).retrieve().bodyToMono(String.class).block();
		JSONParser jsonParser = new JSONParser();
		try {
			Object obj = jsonParser.parse(jsonString);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println(jsonObject.get("startDate"));
			System.out.println(jsonObject.get("results"));
			System.out.println(jsonObject.get("title"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return jsonString;
	}
}
