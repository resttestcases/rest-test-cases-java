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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class NodePath {

	public List<String> paths = Collections.emptyList();

	public String getLast() {
		return paths.get(paths.size() - 1);
	}

	public String getParent() {
		return paths.get(paths.size() - 2);
	}

	public NodePath expand(String lastNode) {
		NodePath path = new NodePath();
		path.paths = new ArrayList<String>(paths.size() + 1);
		path.paths.addAll(paths);
		path.paths.add(lastNode);
		return path;
	}

	@Override
	public String toString() {
		return paths.isEmpty() ? "[]" : "[" + StringUtils.join(paths.subList(1, paths.size()), '.') + "]";
	}

}
