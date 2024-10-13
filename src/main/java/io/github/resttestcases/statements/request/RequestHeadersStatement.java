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

import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.resttestcases.core.Context;
import io.github.resttestcases.core.NodePath;
import io.github.resttestcases.core.Utils;
import io.github.resttestcases.core.statements.LeafNodeStatement;
import io.github.resttestcases.core.statements.NodeStatement;
import io.restassured.specification.RequestSpecification;

public class RequestHeadersStatement extends LeafNodeStatement {

    private Map<String, String> params;

    @Override
    public void config(NodePath path, NodeStatement parent, JsonNode node) {
        super.config(path, parent, node);
        this.params = Utils.toMapKeyTextValue(path, node);
    }

    @Override
    public void execute(Context context) {
        RequestSpecification req = context.getParam(RequestSpecification.class.getName());// Add in ParentNode
        for (Entry<String, String> it : this.params.entrySet()) {
            req.header(it.getKey(), it.getValue());
        }
    }
}
