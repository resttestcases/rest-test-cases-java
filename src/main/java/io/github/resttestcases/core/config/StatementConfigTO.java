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
package io.github.resttestcases.core.config;

import java.util.List;

public class StatementConfigTO {
    
    private String name;
    private String className;    
    private List<String> parents;
    
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getParents() {
        return parents;
    }
    public void setParents(List<String> parents) {
        this.parents = parents;
    }
}
