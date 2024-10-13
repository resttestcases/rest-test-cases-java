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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.github.resttestcases.core.config.RestTestCasesConfigTO;
import io.github.resttestcases.core.config.StatementConfigTO;
import io.github.resttestcases.core.statements.NodeStatement;
import io.github.resttestcases.core.statements.RootStatement;
import io.restassured.RestAssured;

public class Factory {

	protected static Logger logger = LoggerFactory.getLogger("io.github.resttestcases");
	protected static Logger loggerHttp = LoggerFactory.getLogger("io.github.resttestcases.HTTP");
	protected static List<StatementConfigTO> statementsConfig = new ArrayList<>();
	protected static String baseURI;
	private static ObjectMapper yamlMapper;
	private static boolean config =false;

	public static ObjectMapper getYamlMapper() {
		return yamlMapper;
	}

	public static Logger getLogger() {
		return logger;
	}
	
	public static Logger getLoggerHTTP() {
        return loggerHttp;
    }

	public static Context createContext() {
		return new Context();
	}

	private static void config() throws IOException {
	    
	    logger.info("Init Rest Test Cases Tool");
		// Init mapper
		yamlMapper = new ObjectMapper(new YAMLFactory());
		yamlMapper.findAndRegisterModules();
		logger.debug("Init Yaml Mapper");

		// load local config
		logger.debug("Reading config file:"+RestTestCases.PATH_CONFIG_YAML);
		String configYaml = Utils.readTxtResource(RestTestCases.PATH_CONFIG_YAML);
		RestTestCasesConfigTO confTO = yamlMapper.readValue(configYaml, RestTestCasesConfigTO.class);
		for (StatementConfigTO it : confTO.getStatements()) {
			statementsConfig.add(it);
			logger.debug("Add statement config:"+it.getClassName());
		}

		
		//Local
		logger.debug("Reading config file:"+RestTestCases.PATH_CONFIG_YAML);
		try {
		    configYaml = Utils.readTxtResource(RestTestCases.PATH_LOCAL_CONFIG_YAML);
	        confTO = yamlMapper.readValue(configYaml, RestTestCasesConfigTO.class);
		}catch (IOException e) {
		    logger.error("Erro reading config file:"+RestTestCases.PATH_CONFIG_YAML+ " - "+e.getMessage(), e);
        }
		
		for (StatementConfigTO it : confTO.getStatements()) {
		    statementsConfig.add(it);
		    logger.debug("Add statement config:"+it.getClassName());
		}
		baseURI = confTO.getBaseURI();
		RestAssured.baseURI = confTO.getBaseURI();
		logger.debug("Add Base URI:"+baseURI);
		config=true;

	}

	public static RootStatement createRootStatament(String yamlText) throws IOException {
	    if(!config) {
	        config();
	    }
		JsonNode node = yamlMapper.readTree(yamlText);
		return new RootStatement(node);
	}

	public static NodeStatement createStatement(NodePath path, NodeStatement parent) {

		StatementConfigTO typeNodeTester = createStatement(path, parent.getClass().getName());

		if (typeNodeTester == null)
			throw new RestTestCasesException("Invalid declaration in " + path.toString());

		try {
			return getInstance(typeNodeTester);
		} catch (ReflectiveOperationException e) {
			throw new RestTestCasesException("Invalid declaration in " + path.toString() + " - " + e.getMessage(), e);
		}
	}

	private static StatementConfigTO createStatement(NodePath path, String parentClass) {
		String name = path.getLast();
		for (StatementConfigTO it : statementsConfig) {
			if (name.matches(it.getName())) {
				for (String parentExpected : it.getParents()) {
					if (parentClass.equals(parentExpected))
						return it;
				}
			}
		}
		return null;
	}

	private static NodeStatement getInstance(StatementConfigTO typeNodeTester) throws ReflectiveOperationException {
		Class<?> loadClass = Factory.class.getClassLoader().loadClass(typeNodeTester.getClassName());
		return (NodeStatement) loadClass.newInstance();
	}

}
