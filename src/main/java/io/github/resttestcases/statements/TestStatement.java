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
package io.github.resttestcases.statements;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.resttestcases.core.Context;
import io.github.resttestcases.core.NodePath;
import io.github.resttestcases.core.statements.NodeStatement;
import io.github.resttestcases.core.statements.ParentNodeStatement;

public class TestStatement extends ParentNodeStatement {
	
	private String name =null;
	
	@Override
	public void config(NodePath pathInit, NodeStatement parentInit, JsonNode node) {
		super.config(pathInit, parentInit, node);
		
		TestNameStatement testName = findStatement(TestNameStatement.class);
		if(testName!=null) {
			name=testName.getValue();
		}
	}

    @Override
    public void execute(Context context) {
    	String aux = (name==null?"":name) + path;
        logger.info("Starting test {}", aux);
        super.execute(context);
        logger.info("Ending test {}", aux);
    }
    
    public String getName() {
    	return name;
    }

}
