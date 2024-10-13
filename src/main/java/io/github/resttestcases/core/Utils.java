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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;

public class Utils {

	private Utils() {
		throw new UnsupportedOperationException();
	}

	public static String readTxtResource(String path) throws IOException {
		try (InputStream in = RestTestCases.class.getClassLoader().getResourceAsStream(path)) {
			if (in == null) {
				throw new IOException("File " + path + " not found!");
			}
			try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
				return scanner.useDelimiter("\\A").next();
			}
		}
	}

	public static String readTxtResource(File path) throws IOException {
		try (InputStream in = new FileInputStream(path)) {
			try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
				return scanner.useDelimiter("\\A").next();
			}
		}
	}

	public static String getText(NodePath path, JsonNode node) {
		if (!node.isValueNode())
			throw new RestTestCasesException("Invalid value in " + path.toString() + ", expected a single value.");
		return node.textValue();
	}

	public static Map<String, String> toMapKeyTextValue(NodePath path, JsonNode node) {
		validMap(node, path);
		Map<String, String> map = new HashMap<>();

		Iterator<String> fieldNames = node.fieldNames();
		while (fieldNames.hasNext()) {
			String itName = fieldNames.next();
			JsonNode itNode = node.get(itName);
			if (!itNode.isValueNode())
				throw new RestTestCasesException("Invalid statement in " + path.toString() + "." + itName
						+ ", a key-value entry is expected.");
			map.put(itName, itNode.asText());
			;
		}
		return map;
	}

	public static Map<String, List<String>> toMapKeyListTextValue(NodePath path, JsonNode node) {
		Utils.validMap(node, path);
		Map<String, List<String>> map = new HashMap<>();

		Iterator<String> fieldNames = node.fieldNames();
		while (fieldNames.hasNext()) {
			String itName = fieldNames.next();
			JsonNode itNode = node.get(itName);
			if (itNode.isValueNode()) {
				map.put(itName, Arrays.asList(itNode.asText()));
			} else if (itNode.isArray()) {
				List<String> values = new ArrayList<>();
				for (JsonNode arrayIt : itNode) {
					if (!arrayIt.isValueNode())
						throw new RestTestCasesException("Invalid statement in " + path.toString() + "." + itName
								+ ", a key-value entry is expected.");
					values.add(arrayIt.asText());
				}
				map.put(itName, values);
			} else {
				throw new RestTestCasesException("Invalid statement in " + path.toString() + "." + itName
						+ ", a key-value entry is expected.");
			}
		}
		return map;
	}

	public static void validMap(JsonNode node, NodePath path) {
		if (!node.isObject()) {
			throw new RestTestCasesException("Invalid statement in " + path.toString()
					+ ", a key-value entry is expected.");
		}
	}

}
