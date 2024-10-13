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
package io.github.restspectester.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import io.github.resttestcases.core.Context;
import io.github.resttestcases.core.RestTestCases;
import io.github.resttestcases.core.Utils;

//Create dynamic tests from files in src\test\resources\json-tests\

@WireMockTest(httpPort = 8080)
public class TestAllYamlFiles {

	@BeforeAll
	public static void configLog() {
//       Context.logger=logger;

//        LOGGER.info("Starting");
//        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
//        Configuration configuration = loggerContext.getConfiguration();
//
//        LOGGER.info("Filepath: {}", configuration.getConfigurationSource().getLocation());
//        // Log4j root logger has no name attribute -> name == ""
//        LoggerConfig rootLoggerConfig = configuration.getLoggerConfig("");
//
//        rootLoggerConfig.getAppenders().forEach((name, appender) -> {
//            LOGGER.info("Appender {}: {}", name, appender.getLayout().toString());
//            // rootLoggerConfig.removeAppender(a.getName());
//        });
//
//        rootLoggerConfig.getAppenderRefs().forEach(ar -> {
//            System.out.println("AppenderReference: " + ar.getRef());
//        });
//
//        // adding appenders
//        configuration.addAppender(null);
//        

	}

	@TestFactory
	public Collection<DynamicTest> dynamicTestsFromCollection() throws IOException {
//        System.setProperty("rest.url", "http://localhost:"+serverPort+"/api/");

		List<DynamicTest> testes = new ArrayList<DynamicTest>();
		for (File file : getTestFiles()) {
			String nome = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("yaml-tests"));
			if (nome.endsWith(".yaml")) {
				testes.add(dynamicTest(nome, () -> {
					testar(nome);
				}));
			}
		}
		return testes;
	}

	private List<File> getTestFiles() throws IOException {
		List<File> testFiles = new ArrayList<File>();
		Files.find(new File("src/test/resources/yaml-tests").toPath(), Integer.MAX_VALUE, (filePath, fileAttr) -> {
			return fileAttr.isRegularFile() && filePath.toString().endsWith(".yaml");
		}).forEach(it -> testFiles.add(it.toFile()));
		return testFiles;
	}

	private void testar(final String path) throws IOException {
		LogAppenderTest.clear();
		try {
			RestTestCases.testYamlResource(path);
		} catch (AssertionError e) {
			Context.getLogger().error("AssertionError:" + e.getMessage(), e);
		}
		assertEquals(Utils.readTxtResource(path.replace(".yaml", ".log")),
				LogAppenderTest.toStringLog().replaceAll("(Matched).*", ""));
	}

}