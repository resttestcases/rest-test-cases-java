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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.resttestcases.core.Context;
import io.github.resttestcases.core.Factory;
import io.github.resttestcases.core.NodePath;
import io.github.resttestcases.core.RestTestCasesException;

public abstract class ParentNodeStatement extends LeafNodeStatement{


    protected List<NodeStatement> statements = new ArrayList<>();


    @Override
    public void valide(NodePath path, JsonNode node) {
        if (!node.isObject()) {
            throw new RestTestCasesException("Invalid statement in " + path.toString());
        }
    }

    @Override
    public void config(NodePath pathInit, NodeStatement parentInit, JsonNode node) {
        super.config(pathInit, parentInit, node);
        
        Iterator<String> fieldNames = node.fieldNames();
        while (fieldNames.hasNext()) {
            String itName = fieldNames.next();
            NodePath itPath = path.expand(itName);
            NodeStatement it = Factory.createStatement(itPath, this);
            if (it == null)
            	throw new RestTestCasesException("Invalid statement in " + path.toString());
            it.config(itPath, this, node.get(itName));
            statements.add(it);
        }
    }

    public void execute(Context context) {
        for (NodeStatement statement : statements) {
            statement.execute(context);
        }
    }

    protected <T> T removeStatement(Class<T> statementClass) {
        T statement = findStatement(statementClass);
        if (statement != null)
            statements.remove(statement);
        return statement;
    }

    @SuppressWarnings("unchecked")
    protected <T> T findStatement(Class<T> statementClass) {
        for (NodeStatement it : statements) {
            if (it.getClass().equals(statementClass))
                return (T) it;
        }
        return null;
    }
    
    public List<NodeStatement> getChildren(){
    	return statements;
    }
}
