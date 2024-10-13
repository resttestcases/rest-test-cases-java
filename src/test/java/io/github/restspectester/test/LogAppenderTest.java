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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class LogAppenderTest extends AppenderSkeleton {

	private static List<LoggingEvent> logs = new ArrayList<>();

	public static void clear() {
		logs.clear();
	}

	public static String toStringLog() {
		StringBuilder bf = new StringBuilder();
		for (LoggingEvent it : logs) {
			bf.append(it.getMessage() + "\n");
		}
		return bf.toString();
	}

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {
		logs.add(event);
	}

}
//public /*static*/ class MyAppender 
//    ArrayList<LoggingEvent> eventsList = new ArrayList();
//
//    @Override
//    protected void append(LoggingEvent event) {
//        eventsList.add(event);
//    }
//
//    public void close() {
//    }
//
//    public boolean requiresLayout() {
//        return false;
//    }
//
//}