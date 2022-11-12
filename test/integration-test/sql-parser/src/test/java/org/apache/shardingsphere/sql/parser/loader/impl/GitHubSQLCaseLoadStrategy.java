/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sql.parser.loader.impl;

import org.apache.shardingsphere.sql.parser.base.DynamicLoadingSQLParserParameterizedIT;
import org.apache.shardingsphere.sql.parser.loader.SQLCaseLoadStrategy;
import org.apache.shardingsphere.sql.parser.result.SQLParserCSVResultProcessor;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * SQL case loader with GitHub.
 */
public final class GitHubSQLCaseLoadStrategy extends DynamicLoadingSQLParserParameterizedIT implements SQLCaseLoadStrategy {
    
    public GitHubSQLCaseLoadStrategy() {
        super("", "", "", new SQLParserCSVResultProcessor(""));
    }
    
    @Override
    public Collection<Object[]> getTestParameters(final URI sqlCaseTestURI, final URI sqlCaseResultURI) {
        Collection<Object[]> result = new LinkedList<>();
        Map<String, String> resultResponse = getResultResponse("https://api.github.com/repos/", sqlCaseResultURI);
        for (Map<String, String> each : getResponse("https://api.github.com/repos/", sqlCaseTestURI)) {
            String sqlCaseFileName = each.get("name").split("\\.")[0];
            String sqlCaseTestFileContent = getContent(URI.create(each.get("download_url")));
            String sqlCaseResultFileContent = getContent(URI.create(resultResponse.get(each.get("name"))));
            result.addAll(getSQLCases(sqlCaseFileName, sqlCaseTestFileContent, sqlCaseResultFileContent));
        }
        if (result.isEmpty()) {
            result.add(new Object[]{"", ""});
        }
        return result;
    }
}
