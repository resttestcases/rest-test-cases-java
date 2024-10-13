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

import io.github.resttestcases.core.statements.RootStatement;

public class RestTestCases {

	public static final String PATH_LOCAL_CONFIG_YAML = "resttestcases.conf.yaml";
	public static final String PATH_CONFIG_YAML = "io/github/resttestcases/config.yaml";

	public static void testYamlResource(String path) throws IOException {
		testYaml(Utils.readTxtResource(path));
	}

	public static void testYaml(String yamlText) throws IOException {
		RootStatement rootNode = Factory.createRootStatament(yamlText);
		rootNode.execute(Factory.createContext());
	}

}
