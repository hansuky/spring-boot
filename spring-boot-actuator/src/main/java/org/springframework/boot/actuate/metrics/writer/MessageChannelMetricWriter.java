/*
 * Copyright 2012-2013 the original author or authors.
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

package org.springframework.boot.actuate.metrics.writer;

import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

/**
 * A {@link MetricWriter} that publishes the metric updates on a {@link MessageChannel}.
 * The messages have the writer input ({@link Delta} or {@link Metric}) as payload, and
 * carry an additional header "metricName" with the name of the metric in it.
 *
 * @author Dave Syer
 */
public class MessageChannelMetricWriter implements MetricWriter {

	private static final String METRIC_NAME = "metricName";

	private final String DELETE = "delete";

	private final MessageChannel channel;

	public MessageChannelMetricWriter(MessageChannel channel) {
		this.channel = channel;
	}

	@Override
	public void increment(Delta<?> delta) {
		this.channel.send(MessageBuilder.withPayload(delta)
				.setHeader(METRIC_NAME, delta.getName()).build());
	}

	@Override
	public void set(Metric<?> value) {
		this.channel.send(MessageBuilder.withPayload(value)
				.setHeader(METRIC_NAME, value.getName()).build());
	}

	@Override
	public void reset(String metricName) {
		this.channel.send(MessageBuilder.withPayload(this.DELETE)
				.setHeader(METRIC_NAME, metricName).build());
	}

}
