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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.ComparisonFailure;

import com.fasterxml.jackson.databind.JsonNode;

public class ValidateJson {

    public static void valide(JsonNode expected, JsonNode actual) {

        Error error = validJson(new Path(), expected, actual);
        if (error != null) {
            String msg = "";
            if (error.expected == null) {
                msg = "not expected field " + error.actual;
            }
            throw new ComparisonFailure("Comparison failure in <" + error.path.toString() + "> " + msg,
                    expected.toString(), actual.toString());

        }
    }

    private static Error validJson(Path path, JsonNode expected, JsonNode actual) {
        if (expected.isValueNode()) {
            return validJsonPrimitive(new Path(), expected, actual);
        } else if (expected.isObject()) {
            return validJsonObject(new Path(), expected, actual);
        } else if (expected.isArray()) {
            return validJsonArray(new Path(), expected, actual);
        }
        return null;
    }

    private static Error validJsonPrimitive(Path path, JsonNode expected, JsonNode actual) {

        if ("?".equals(expected.textValue())
                || ("\\?".equals(expected.textValue()) && "?".equals(actual.textValue()))) {// scape
            return null;
        }

        if (actual != null && actual.isValueNode() && expected.equals(actual)) {
            return null;
        }
        return new Error(path, expected, actual);
    }

    private static Error validJsonObject(Path path, JsonNode expected, JsonNode actual) {

        if (!actual.isObject()) {
            return new Error(path, expected, actual);
        }

        Iterator<Entry<String, JsonNode>> set = expected.fields();

        Set<String> actualFields = new HashSet<>();
        actual.fieldNames().forEachRemaining(it -> actualFields.add(it));
        boolean acceptMoreFields = false;

        while (set.hasNext()) {
            Entry<String, JsonNode> it = set.next();
            String key = it.getKey();
            JsonNode value = it.getValue();
            actualFields.remove(key);

            if ("...".equals(key) && "?".equals(value.textValue())) {
                acceptMoreFields = true;
            } else if (value.isNull() && actual.get(key) != null && !actual.get(key).isNull()) {
                return new Error(path.child(key), expected, actual.get(key));

            } else if (value.isValueNode()) {
                Error ret = validJsonPrimitive(path.child(key), value, actual.get(key));
                if (ret != null) {
                    return ret;
                }

            } else if (value.isObject()) {
                Error ret = validJsonObject(path.child(key), value, actual.get(key));
                if (ret != null) {
                    return ret;
                }

            } else if (it.getValue().isArray()) {
                Error ret = validJsonArray(path.child(key), value, actual.get(key));
                if (ret != null) {
                    return ret;
                }
            } else {
                return new Error(path.child(key), expected, actual.get(key));
            }
        }

        if (!acceptMoreFields && !actualFields.isEmpty()) {
            String fieldname = actualFields.iterator().next();
            return new Error(path, fieldname + ":" + actual.get(fieldname).toString());
        }

        return null;
    }

    private static Error validJsonArray(Path path, JsonNode expectedElement, JsonNode actualElement) {

        if (!actualElement.isArray()) {
            return new Error(path, expectedElement, actualElement);
        }

        List<JsonNode> expected = new ArrayList<>();
        expectedElement.forEach(e -> expected.add(e));

        List<JsonNode> actual = new ArrayList<>();
        actualElement.forEach(e -> actual.add(e));

        boolean inOrder = false;
        if (!expected.isEmpty()) {
            JsonNode node = expected.get(0);
            if (node.isValueNode() && !node.isNull() && node.textValue() != null && node.textValue().startsWith("!")) {
                expected.remove(0);
                String[] cmds = node.textValue().substring(1).split(";");

                for (String cmd : cmds) {
                    cmd = cmd.trim();
                    if ("inOrder".equals(cmd)) {
                        inOrder = true;
                    } else if (cmd.matches("length:\\d+")) {
                        int expectedSize = Integer.parseInt(cmd.substring(7));
                        if (expectedSize != actual.size()) {
                            throw new ComparisonFailure("Comparison failure in <" + path + "> length",
                                    Integer.toString(expectedSize),
                                    Integer.toString(actual.size()));
                        }

                    } else {
                        throw new RestTestCasesException(
                                "Invalid statement in json expected <" + path.toString() + "> " + node.textValue());
                    }
                }

            }
        }

        Error e = inOrder ? validJsonArrayInOrder(path, expected, actual)
                : validJsonArrayAnyOrder(path, expected, actual);
        return e == null ? null : new Error(path, expectedElement.toString(), actualElement.toString());
    }

    private static Error validJsonArrayAnyOrder(Path path, List<JsonNode> expected, List<JsonNode> actual) {
        boolean acceptAny = false;
        int i = 0;
        for (JsonNode it : expected) {

            if (it.isValueNode() && "...".equals(it.textValue())) {
                acceptAny = true;
            } else {
                Error aux = existeInArray(path.child("[" + i + "]"), it, actual);
                if (aux != null) {
//                    aux.actual = "in " + Arrays.toString(actual.toArray());
                    return new Error(path, expected, actual);
                }
            }
        }

        if (!acceptAny && !actual.isEmpty()) {
            return new Error(path, expected, actual);
        }

        return null;

    }

    private static Error validJsonArrayInOrder(Path path, List<JsonNode> expected, List<JsonNode> actual) {
        boolean acceptMore = false;
        int indexActual = 0;
        for (JsonNode it : expected) {

            if (it.isValueNode() && "...".equals(it.textValue())) {
                acceptMore = true;
            } else {

                if (indexActual >= actual.size()) {
                    return new Error(path, expected, actual);
                }

                Error aux = null;
                if (acceptMore) {
                    do {
                        aux = validJson(path.child("[" + indexActual + "]"), it, actual.get(indexActual));
                        indexActual++;
                    } while (aux != null && indexActual < actual.size());
                    acceptMore = false;
                } else {
                    aux = validJson(path.child("[" + indexActual + "]"), it, actual.get(indexActual));
                    indexActual++;
                }

                if (aux != null) {
                    return new Error(path, expected, actual);
                }
            }
        }

        if (!acceptMore && indexActual < actual.size()) {
            return new Error(path, expected, actual);
        }

        return null;

    }

    private static Error existeInArray(Path path, JsonNode expected, List<JsonNode> list) {

        for (JsonNode it : list) {
            if (expected.isValueNode()) {
                Error aux = validJsonPrimitive(path, expected, it);
                if (aux == null) {
                    list.remove(it);
                    return null;
                }
            } else if (expected.isObject()) {
                Error aux = validJsonObject(path, expected, it);
                if (aux == null) {
                    list.remove(it);
                    return null;
                }
            } else if (expected.isArray()) {
                Error aux = validJsonArray(path, expected, it);
                if (aux == null) {
                    list.remove(it);
                    return null;
                }
            }
        }

        return new Error(path, expected, "in [...]");
    }

    private static class Error {

        private Path path;
        private Object expected;
        private Object actual;

        public Error(Path path, Object expected, Object actual) {
            this.path = path;
            this.expected = expected;
            this.actual = actual;
        }

        public Error(Path path, Object actual) {
            this.path = path;
            this.actual = actual;
        }

    }

    private static class Path {

        private String[] path;

        public Path() {
            path = new String[0];
        }

        public Path child(String name) {
            Path newPath = new Path();
            newPath.path = Arrays.copyOf(path, path.length + 1);
            newPath.path[path.length] = name;
            return newPath;
        }

//        public String last() {
//            return path.length == 0 ? null : path[path.length - 1];
//        }

        @Override
        public String toString() {
            return String.join(".", path);
        }

    }

}
