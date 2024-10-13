/*
 * Copyright [2024] [Rest Test Cases] [https://github.com/resttestcases/]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.resttestcases.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class Context {

	protected ObjectMapper mapper;
	protected ObjectMapper mapperJson;

	protected static Logger logger = Factory.getLogger();
	protected static Logger loggerRequets = Factory.getLoggerHTTP();
	// Logs RestAssured
	private static ByteArrayOutputStream requestLog;
	private static ByteArrayOutputStream responseLog;

//    protected Gson gson;
	private Map<String, Object> parans = new HashMap<String, Object>();

	public Context() {
		init();
	}

	private void init() {
		mapper = new ObjectMapper(new YAMLFactory());
		mapper.findAndRegisterModules();
		mapperJson = new ObjectMapper();
		mapperJson.findAndRegisterModules();
		configRestAssuredLogs();
	}

	public void setParam(String name, Object req) {
		parans.put(name, req);
	}

	@SuppressWarnings("unchecked")
	public <T> T getParam(String name) {
		return (T) parans.get(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T removeParam(String name) {
		return (T) parans.remove(name);
	}
//
//    public <T> T fromJson(String str, Class<T> clazz) {
//        return gson.fromJson(str, clazz);
//    }
//
//    public <T> T fromJson(JsonElement e, Class<T> clazz) {
//        return gson.fromJson(e, clazz);
//    }

//    public List<String> getArrayValues(JsonElement value) {
//        if (value.isJsonArray()) {
//            JsonArray array = value.getAsJsonArray();
//            List<String> list = new ArrayList<String>();
//            for (JsonElement it : array) {
//                list.add(it.getAsString());
//            }
//            return list;
//        }
//        return Arrays.asList(value.getAsString());
//    }

	public static Logger getLogger() {
		return logger;
	}

	private static void configRestAssuredLogs() {
		if (loggerRequets.isDebugEnabled() && requestLog == null) {
			requestLog = new ByteArrayOutputStream();
			responseLog = new ByteArrayOutputStream();
			PrintStream requestPrint = new PrintStream(requestLog);
			PrintStream responsePrint = new PrintStream(responseLog);
			RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter(LogDetail.ALL, true, requestPrint);
			;
			ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter(LogDetail.ALL, true, responsePrint);
			RestAssured.filters(requestLoggingFilter, responseLoggingFilter);
		}
	}

	public static void logHttpReset() {
		if (loggerRequets.isDebugEnabled()) {
			requestLog.reset();
			responseLog.reset();
		}
	}

	public void logRequest() {
		try {
			if (loggerRequets.isDebugEnabled()) {

				loggerRequets.debug("\r\n"
						+ "-----------------------------------Request Data---------------------------------" + "\r\n"
						+ requestLog.toString("UTF-8") + "\r\n"
						+ "-----------------------------------End Request Data-----------------------------" + "\r\n");

				requestLog.reset();
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void logResponse() {
		try {
			if (loggerRequets.isDebugEnabled()) {
				loggerRequets.debug("\r\n"
						+ "-----------------------------------Response Data--------------------------------" + "\r\n"
						+ responseLog.toString("UTF-8") + "\r\n"
						+ "-----------------------------------End Response Data----------------------------" + "\r\n");
				responseLog.reset();
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public JsonNode toJsonNode(String str) throws IOException {
		return mapperJson.readTree(str);
	}

}
