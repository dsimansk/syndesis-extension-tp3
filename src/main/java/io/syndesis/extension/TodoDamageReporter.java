package io.syndesis.extension;

import io.syndesis.integration.runtime.api.SyndesisExtensionAction;
import io.syndesis.integration.runtime.api.SyndesisExtensionRoute;
import io.syndesis.integration.runtime.api.SyndesisStepExtension;
import io.syndesis.maven.annotation.processing.SyndesisExtensionActionProcessor;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctc.wstx.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SyndesisExtensionAction(
    id = "damage-reported",
    name = "Damage Reporter",
    description = "Reports contact list for damaged good",
    inputDataShape = "java:java.lang.String",
    outputDataShape = "java:io.syndesis.extension.TodoReport"
)
public class TodoDamageReporter implements SyndesisStepExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoDamageReporter.class);

    @Override
    public Optional<ProcessorDefinition> configure(CamelContext camelContext, ProcessorDefinition route, Map<String, Object> map) {
        return Optional.of(route.process(this::process));
    }

    private void process(Exchange exchange) {
        ObjectMapper mapper = new XmlMapper();
        InventoryList inventoryList;
        try {
            inventoryList = mapper.readValue(exchange.getIn().getBody(String.class).trim(), InventoryList.class);
        } catch (IOException e) {
            throw ObjectHelper.wrapRuntimeCamelException(e);
        }

        List<Item> damagedItems = inventoryList.getItems().stream()
                .filter(Item::isDamaged)
                .collect(Collectors.toList());

        if (damagedItems.isEmpty()) {
            LOGGER.info("No damaged item found from: \n{}", exchange.getIn().getBody(String.class).trim());
            exchange.setProperty(Exchange.ROUTE_STOP, true);
        } else {
            LOGGER.info("Processing damaged item from: \n{}", exchange.getIn().getBody(String.class).trim());
            TodoReport report = new TodoReport(damagedItems);
            exchange.getOut().setBody(report, TodoReport.class);
        }
    }
}
