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

import io.github.resttestcases.core.Context;
import io.github.resttestcases.core.statements.ParentNodeStatement;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class ResponseStatement extends ParentNodeStatement {

	@Override
	public void execute(Context context) {
		Response response = context.getParam(Response.class.getName());
		context.setParam(ValidatableResponse.class.getName(), response.then());
		super.execute(context);
		context.removeParam(ValidatableResponse.class.getName());
	}

}
