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
package io.github.resttestcases.statements.request;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.resttestcases.core.Context;
import io.github.resttestcases.core.NodePath;
import io.github.resttestcases.core.RestTestCasesException;
import io.github.resttestcases.core.statements.NodeStatement;
import io.github.resttestcases.core.statements.ParentNodeStatement;
import io.github.resttestcases.statements.response.ResponseStatement;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RequestStatement extends ParentNodeStatement {

	protected Method httpMethodReq;
	private String uri;
	private ResponseStatement responseStatement;

	@Override
	public void config(NodePath path, NodeStatement parent, JsonNode node) {
		if (node.isObject()) {

			super.config(path, parent, node);

			RequestMethodPathStatement requestMethodPath = removeStatement(RequestMethodPathStatement.class);
			if (requestMethodPath == null || httpMethodReq==null) {
				throw new RestTestCasesException("Invalid statement in " + path.toString()+ "! Not found METHOD: path ");
			}
			this.uri = requestMethodPath.getValue();

			// Remove the response because it should only execute after the request.
			responseStatement = removeStatement(ResponseStatement.class);

		} else {
			throw new RestTestCasesException("Invalid statement in " + path.toString());
		}
	}

	@Override
	public void execute(Context context) {

		logger.debug("Executing request {} {}", httpMethodReq, uri);

		RequestSpecification req = RestAssured.given();
		context.setParam(RequestSpecification.class.getName(), req);

		// Set parameters
		super.execute(context);

		// Executes the request and saves the result in the context.
		context.setParam(Response.class.getName(), executeRequest(req));
		context.removeParam(RequestSpecification.class.getName());
		context.logRequest();
		context.logResponse();

		// Executes the response and remove from context
		if (responseStatement != null) {
			responseStatement.execute(context);
		}
		context.removeParam(Response.class.getName());
	}

	private Response executeRequest(RequestSpecification reqSpec) {
		switch (httpMethodReq) {
		case GET: {
			return reqSpec.get(uri);
		}
		case POST: {
			return reqSpec.post(uri);
		}
		case PUT: {
			return reqSpec.put(uri);
		}
		case DELETE: {
			return reqSpec.delete(uri);
		}
		default:
			throw new AssertionError("Request Method " + httpMethodReq + " invalid in " + path.toString());
		}
	}

}
