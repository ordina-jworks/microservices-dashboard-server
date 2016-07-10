/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.ordina.msdashboard.graph;

import be.ordina.msdashboard.model.Node;
import be.ordina.msdashboard.model.NodeBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Tim Ysewyn
 */
public class NodeMergerTest {

    @Test
    public void shouldAddNodeToEmptyList() {
        Node node = new Node("service1");
        List<Node> nodes = NodeMerger.merge().call(new ArrayList<>(), node);

        assertThat(nodes).isNotEmpty();
        assertThat(nodes).hasSize(1);
        assertThat(nodes.get(0)).isEqualTo(node);
    }

    @Test
    public void shouldAddNodeToExistingList() {
        Node node1 = new Node("service1");
        Node node2 = new Node("service2");

        List<Node> nodes = NodeMerger.merge().call(new ArrayList<>(Collections.singletonList(node1)), node2);

        assertThat(nodes).isNotEmpty();
        assertThat(nodes).hasSize(2);
        assertThat(nodes).contains(node1, node2);
    }

    @Test
    public void shouldMergeNodes() {
        Node node1 = new NodeBuilder().withId("service1").withLane(0).build();
        Node node2 = new NodeBuilder().withId("service1").withDetail("type", "microservice").build();

        List<Node> nodes = NodeMerger.merge().call(new ArrayList<>(Collections.singletonList(node1)), node2);

        assertThat(nodes).isNotEmpty();
        assertThat(nodes).hasSize(1);

        Node node = nodes.get(0);
        assertThat(node.getId()).isEqualTo("service1");
        assertThat(node.getLane()).isEqualTo(0);
        assertThat(node.getDetails()).isNotEmpty();

        Map<String, Object> details = node.getDetails();
        assertThat(details.containsKey("type")).isTrue();
        assertThat(details.get("type")).isEqualTo("microservice");
    }

}
