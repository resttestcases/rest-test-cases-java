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
package io.github.resttestcases.core.junit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import io.github.resttestcases.core.Context;
import io.github.resttestcases.core.Factory;
import io.github.resttestcases.core.Utils;
import io.github.resttestcases.core.statements.NodeStatement;
import io.github.resttestcases.core.statements.RootStatement;
import io.github.resttestcases.statements.TestStatement;

public class DynamicTestFactory {

	public static Iterable<? extends DynamicNode> createDynamicTestNodes(File dir, Properties properties)
			throws IOException {
		return createDynamicTestNodes(dir, properties, Factory.createContext());
	}

	private static Iterable<? extends DynamicNode> createDynamicTestNodes(File dir, Properties properties,
			Context context) throws IOException {
		List<DynamicNode> nodes = new ArrayList<DynamicNode>();

		for (File it : dir.listFiles()) {
			if (it.isDirectory()) {
				nodes.add(DynamicContainer.dynamicContainer(it.getName(),
						createDynamicTestNodes(it, properties, context)));
			} else {
				if (it.getName().endsWith(".yaml")) {
					RootStatement rootNode = Factory.createRootStatament(getFileWithParameters(it, properties));
					nodes.add(DynamicContainer.dynamicContainer(it.getName(),
							createTests(rootNode, properties, context)));
				}
			}
		}
		return nodes;
	}

	private static Iterable<? extends DynamicNode> createTests(RootStatement rootNode, Properties properties,
			Context context) {
		List<DynamicNode> nodes = new ArrayList<DynamicNode>();
		for (NodeStatement it : rootNode.getChildren()) {

			if (it instanceof TestStatement) {
				TestStatement testNode = (TestStatement) it;
				nodes.add(
						DynamicTest.dynamicTest(testNode.getPath().getLast(), () -> {
							testNode.execute(context);
						}));
			}
		}
		return nodes;
	}

	private static String getFileWithParameters(File file, Properties properties) throws IOException {

		String readTxtResource;
		try {
			readTxtResource = Utils.readTxtResource(file);
		} catch (Exception e) {
			throw new IOException("Error reading file " + file.getAbsolutePath(), e);
		}

        List<String> parameters = getParameters(readTxtResource);

		for (String par : parameters) {
			readTxtResource = readTxtResource.replace("$" + "{" + par + "}", properties.getProperty(par));
		}
		return readTxtResource;
	}

	private static List<String> getParameters(String jsonTxt) {
		List<String> list = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(jsonTxt);
		while (matcher.find()) {
			list.add(matcher.group(1));
		}
		return list;
	}

}
