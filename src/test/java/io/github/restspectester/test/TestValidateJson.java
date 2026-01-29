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
package io.github.restspectester.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resttestcases.core.RestTestCasesException;
import io.github.resttestcases.core.Utils;
import io.github.resttestcases.core.ValidateJson;

public class TestValidateJson {

    private static ObjectMapper mapper = new ObjectMapper();

    public JsonNode readJson(String path) {
        try {
            return mapper.readTree(Utils.readTxtResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void valid(String expected, String actual) {
        ValidateJson.valide(readJson("json/" + expected), readJson("json/" + actual));
    }

    private void validError(String expected, String actual, String error) {
        try {
            valid(expected, actual);
            fail("Expected error: " + error);
        } catch (AssertionError e) {
            assertEquals(error, e.getMessage());
        }
    }

    private void validException(String expected, String actual, String msgErro) {
        try {
            valid(expected, actual);
            fail();
        } catch (RestTestCasesException e) {
            assertEquals(msgErro, e.getMessage());
        }
    }

//    @Test
//    public void test_equals() {
//        valid("test_primitives_expected_equals.json", "test_primitives_actual.json");
//    }

    @Test
    public void test_primitives_expected_any_values() {
        valid("test_primitives_expected_any_values.json", "test_primitives_actual.json");
    }

    @Test
    public void test_primitives_expected_fewer_values() {
        validError("test_primitives_expected_fewer_values.json", "test_primitives_actual.json",
                "Comparison failure in <> not expected field description:null expected:<...\"John\",\"active\":true[]}> but was:<...\"John\",\"active\":true[,\"age\":25,\"description\":null]}>");
    }

    @Test
    public void test_primitives_expected_fewer_values2() {
        validError("test_primitives_expected_fewer_values2.json", "test_primitives_actual.json",
                "Comparison failure in <> not expected field name:\"John\" expected:<{\"id\":1,\"[active\":true],\"description\":null}> but was:<{\"id\":1,\"[name\":\"John\",\"active\":true,\"age\":25],\"description\":null}>");
    }

    @Test
    public void test_primitives_expected_more_fields() {
        valid("test_primitives_expected_more_fields.json", "test_primitives_actual.json");
    }

    @Test
    public void test_primitives_expected_not_equal() {
        validError("test_primitives_expected_not_equal.json", "test_primitives_actual.json",
                "Comparison failure in <active>  expected:<...me\":\"John\",\"active\":[fals]e,\"age\":25,\"descript...> but was:<...me\":\"John\",\"active\":[tru]e,\"age\":25,\"descript...>");
    }

    @Test
    public void test_array_expected_equals() {
        valid("test_array_expected_equals.json", "test_array_actual.json");
    }

    @Test
    public void test_array_expected_more_elements() {
        valid("test_array_expected_more_elements.json", "test_array_actual.json");
    }

    @Test
    public void test_array_expected_more_elements_error() {
        validError("test_array_expected_more_elements_error.json", "test_array_actual.json",
                "Comparison failure in <>  expected:<...wilson@example.com\"}[,{\"id\":7,\"name\":\"Daniel Smith\",\"email\":\"daniel.smith@example.com\"},{\"id\":8,\"name\":\"Tody Smith\",\"email\":\"tody.smith@example.com\"}]]> but was:<...wilson@example.com\"}[]]>");
    }

    @Test
    public void test_array_expected_more_elements_error2() {
        validError("test_array_expected_more_elements_error2.json", "test_array_actual.json",
                "Comparison failure in <>  expected:<...n.doe@example.com\"},[\"...\",{\"id\":8,\"name\":\"Tody Smith\",\"email\":\"tody.smith]@example.com\"}]> but was:<...n.doe@example.com\"},[{\"id\":2,\"name\":\"Jane Smith\",\"email\":\"jane.smith@example.com\"},{\"id\":3,\"name\":\"Bob Johnson\",\"email\":\"bob.johnson@example.com\"},{\"id\":4,\"name\":\"Alice Brown\",\"email\":\"alice.brown@example.com\"},{\"id\":5,\"name\":\"Eve Davis\",\"email\":\"eve.davis@example.com\"},{\"id\":6,\"name\":\"Charlie Wilson\",\"email\":\"charlie.wilson]@example.com\"}]>");
    }

    @Test
    public void test_array_expected_more_elements_error3() {
        validError("test_array_expected_more_elements_error3.json", "test_array_actual.json",
                "Comparison failure in <>  expected:<[[\"!inOrder\",{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},{\"id\":2,\"name\":\"Jane Smith\",\"email\":\"jane.smith@example.com\"},{\"id\":3,\"name\":\"Bob Johnson\",\"email\":\"bob.johnson@example.com\"},{\"id\":4,\"name\":\"Alice Brown\",\"email\":\"alice.brown@example.com\"},{\"id\":5,\"name\":\"Eve Davis\",\"email\":\"eve.davis@example.com\"},{\"id\":6,\"name\":\"Charlie Wilson\",\"email\":\"charlie.wilson@example.com\"},{\"id\":7,\"name\":\"Daniel Smith\",\"email\":\"daniel.smith@example.com\"},{\"id\":8,\"name\":\"Tody Smith\",\"email\":\"tody.smith]@example.com\"}]> but was:<[[{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},{\"id\":2,\"name\":\"Jane Smith\",\"email\":\"jane.smith@example.com\"},{\"id\":3,\"name\":\"Bob Johnson\",\"email\":\"bob.johnson@example.com\"},{\"id\":4,\"name\":\"Alice Brown\",\"email\":\"alice.brown@example.com\"},{\"id\":5,\"name\":\"Eve Davis\",\"email\":\"eve.davis@example.com\"},{\"id\":6,\"name\":\"Charlie Wilson\",\"email\":\"charlie.wilson]@example.com\"}]>");
    }

    @Test
    public void test_array_expected_more_elements_error4() {
        validError("test_array_expected_more_elements_error4.json", "test_array_actual.json",
                "Comparison failure in <>  expected:<[[\"!inOrder\",{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},{\"id\":2,\"name\":\"Jane Smith\",\"email\":\"jane.smith@example.com\"},{\"id\":3,\"name\":\"Bob Johnson\",\"email\":\"bob.johnson@example.com\"},\"...\",{\"id\":7,\"name\":\"Daniel Smith\",\"email\":\"daniel.smith@example.com\"},{\"id\":8,\"name\":\"Tody Smith\",\"email\":\"tody.smith]@example.com\"}]> but was:<[[{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},{\"id\":2,\"name\":\"Jane Smith\",\"email\":\"jane.smith@example.com\"},{\"id\":3,\"name\":\"Bob Johnson\",\"email\":\"bob.johnson@example.com\"},{\"id\":4,\"name\":\"Alice Brown\",\"email\":\"alice.brown@example.com\"},{\"id\":5,\"name\":\"Eve Davis\",\"email\":\"eve.davis@example.com\"},{\"id\":6,\"name\":\"Charlie Wilson\",\"email\":\"charlie.wilson]@example.com\"}]>");
    }

    @Test
    public void test_array_expected_fewer_elements() {
        validError("test_array_expected_fewer_elements.json", "test_array_actual.json",
                "Comparison failure in <>  expected:<....davis@example.com\"}[]]> but was:<....davis@example.com\"}[,{\"id\":6,\"name\":\"Charlie Wilson\",\"email\":\"charlie.wilson@example.com\"}]]>");
    }

    @Test
    public void test_array_order_expected_first() {
        valid("test_array_order_expected_first.json", "test_array_actual.json");
    }

    @Test
    public void test_array_order_expected_last() {
        valid("test_array_order_expected_last.json", "test_array_actual.json");
    }

    @Test
    public void test_array_order_expected_first_last() {
        valid("test_array_order_expected_first_last.json", "test_array_actual.json");
    }

    @Test
    public void test_array_expected_midle() {
        valid("test_array_expected_midle.json", "test_array_actual.json");
    }

    @Test
    public void test_array_order_expected_more_elements_error() {
        validError("test_array_order_expected_more_elements_error.json", "test_array_actual.json",
                "Comparison failure in <>  expected:<[[\"!inOrder\",{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},{\"id\":2,\"name\":\"Jane Smith\",\"email\":\"jane.smith@example.com\"},{\"id\":3,\"name\":\"Bob Johnson\",\"email\":\"bob.johnson@example.com\"},{\"id\":4,\"name\":\"Alice Brown\",\"email\":\"alice.brown@example.com\"},{\"id\":5,\"name\":\"Eve Davis\",\"email\":\"eve.davis@example.com\"},{\"id\":6,\"name\":\"Charlie Wilson\",\"email\":\"charlie.wilson@example.com\"},{\"id\":7,\"name\":\"Daniel Smith\",\"email\":\"daniel.smith@example.com\"},{\"id\":8,\"name\":\"Tody Smith\",\"email\":\"tody.smith]@example.com\"}]> but was:<[[{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},{\"id\":2,\"name\":\"Jane Smith\",\"email\":\"jane.smith@example.com\"},{\"id\":3,\"name\":\"Bob Johnson\",\"email\":\"bob.johnson@example.com\"},{\"id\":4,\"name\":\"Alice Brown\",\"email\":\"alice.brown@example.com\"},{\"id\":5,\"name\":\"Eve Davis\",\"email\":\"eve.davis@example.com\"},{\"id\":6,\"name\":\"Charlie Wilson\",\"email\":\"charlie.wilson]@example.com\"}]>");
    }

    @Test()
    public void test_array_expected_invalide() {
        validException("test_array_expected_invalide.json", "test_array_actual.json",
                "Invalid statement in json expected <> !inOrders;");
        validException("test_array_expected_invalide2.json", "test_array_actual.json",
                "Invalid statement in json expected <> !inOrder;size:x");
    }

    @Test
    public void test_array_expected_length() {
        valid("test_array_expected_length.json", "test_array_actual.json");
    }

    @Test
    public void test_array_expected_length_fail() {
        validError("test_array_expected_length_fail.json", "test_array_actual.json",
                "Comparison failure in <> length expected:<[7]> but was:<[6]>");
    }

    @Test
    public void test_array_number() {
        valid("test_array_number_expected.json", "test_array_number_actual.json");
    }

//    .json .json

//
//	@Test
//	public void test_equals(){
//		valid("test_equals_expected.json", "test_equals_actual.json");
//	}
//	
//	
//	
//	@Test
//	public void test_primitives() {
//		valid("test_primitives_expected.json", "test_primitives_actual.json");
//	}
//	
//	@Test
//	public void test_primitives_fail()  {
//		validError("test_primitives_expected.json", "test_primitives_fail.json","name expected:<\"John\"> but was:<\"John1\">");
//	}
//
//	
//	@Test
//	public void test_primitives_more_fields() {
//		validError("test_primitives_expected.json", "test_primitives_more_fields.json", "expected:<null> but was:<score>");//fewer fields than
//	}
//	
//	@Test
//	public void test_primitives_fewer_fields() {
//		validError("test_primitives_more_fields.json", "test_primitives_actual.json","department expected:<\"sales\"> but was:<null>");//fewer fields than
//	}
//	
//	
//	@Test
//	public void test_primitives_expected_more_fields() {
//		valid("test_primitives_expected_more_fields.json", "test_primitives_more_fields.json");//fewer fields than
//	}
//	
//	
//	@Test
//	public void test_test_primitives_expected_any_values() {
//		valid("test_primitives_expected_any_values.json", "test_primitives_actual.json");//fewer fields than
//	}
//	
//	
//	@Test
//	public void test_equals_expected_any_values() {
//		valid("test_equals_expected_any_values.json", "test_equals_actual.json");//fewer fields than
//	}
//	
//	
//	
//	

}
