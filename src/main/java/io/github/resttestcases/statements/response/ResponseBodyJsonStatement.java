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
package io.github.resttestcases.statements.response;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.resttestcases.core.Context;
import io.github.resttestcases.core.NodePath;
import io.github.resttestcases.core.RestTestCasesException;
import io.github.resttestcases.core.ValidateJson;
import io.github.resttestcases.core.statements.LeafNodeStatement;
import io.github.resttestcases.core.statements.NodeStatement;
import io.restassured.response.Response;

public class ResponseBodyJsonStatement extends LeafNodeStatement {

	private String expected;

	@Override
	public void config(NodePath pathInit, NodeStatement parentInit, JsonNode node) {
		super.config(pathInit, parentInit, node);
		this.expected = node.asText();
		if (this.expected == null)
			this.expected = node.toString();
	}

	@Override
	public void execute(Context context) {
		logger.debug("Valid response {}", path.getLast());
		Response response = context.getParam(Response.class.getName());
		String actual = response.getBody().asString();
		try {
			ValidateJson.valide(context.toJsonNode(expected), context.toJsonNode(actual));
		} catch (IOException e) {
			throw new RestTestCasesException("Erro in " + path + " message:" + e.getMessage(), e);
		}
	}
}