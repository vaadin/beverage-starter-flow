/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.polymertemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.Config;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SimpleErrorReporter;
import com.google.javascript.rhino.StaticSourceFile;

public final class BundleParser {

    private static Config config = ParserRunner.createConfig(Config.LanguageMode.ECMASCRIPT6, null,
            Config.StrictMode.STRICT);

    private BundleParser() {
    }

    public static Optional<Element> getIdElement(String id, String fileName, String statistics) {
        Element template = parseTemplateElement(statistics, fileName, id);
        return Optional.ofNullable(template.getElementById(id));
    }

    public static Element parseTemplateElement(InputStream content, String url, String tag) throws IOException {
        return parseTemplateElement(IOUtils.toString(content, StandardCharsets.UTF_8), url, tag);
    }

    public static Element parseTemplateElement(String source, String url, String tag) {
        ErrorReporter errorReporter = new SimpleErrorReporter();

        // parse a source file into an ast.
        SourceFile sourceFile = new SourceFile(url, StaticSourceFile.SourceKind.STRONG);
        ParserRunner.ParseResult parseResult = ParserRunner.parse(sourceFile, source, config, errorReporter);

        // run the visitor on the ast to extract the needed values.
        DependencyVisitor visitor = new DependencyVisitor();
        NodeUtil.visitPreOrder(parseResult.ast, visitor, Predicates.alwaysTrue());

        Document template = Jsoup.parse(
                "<dom-module id=\"" + tag + "\"><template>" + visitor.templateContent + "</template></dom-module>");

        // Turn ast back to javascript.
        return template.body().child(0);
    }

    private static class DependencyVisitor implements NodeUtil.Visitor {
        public String templateContent;

        @Override
        public void visit(Node node) {
            switch (node.getToken()) {
            case GETTER_DEF:
                addGetter(node);
                break;
            default:
                break;
            }
        }

        private void addGetter(Node node) {
            if ("template".equals(node.getString()) && templateContent == null) {
                templateContent = getTextNode(node).getRawString();
            }
        }

        private Node getTextNode(Node node) {
            Node child = node.getFirstChild();
            while (child.getFirstChild() != null || child.getNext() != null) {
                if (child.getNext() == null) {
                    child = child.getFirstChild();
                } else {
                    child = child.getNext();
                }
            }
            return child;
        }
    }

}
