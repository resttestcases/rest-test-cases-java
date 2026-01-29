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
package io.github.resttestcases.core.statements;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.resttestcases.core.Context;
import io.github.resttestcases.core.NodePath;

/**
 * Component based on JSON node representation to function associated with the statement in the test
 */
public interface NodeStatement {
	
	/**
	 * Configures the component based on JSON node representation 
	 * @param path - node path
	 * @param parent node
	 * @param jsonNode actual
	 */
	void config(NodePath path, NodeStatement parent, JsonNode jsonNode);
	
	/**
	 * Valide JSON node 
	 * @param path node
	 * @param jsonNode actual
	 */
    void valide(NodePath path, JsonNode jsonNode);
    
    
    /**
     * Execute the function associated with this statement
     * @param context executiom
     */
    void execute(Context context);
}