package io.syndesis.extension;

import io.syndesis.integration.runtime.api.SyndesisExtensionAction;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SyndesisExtensionAction(
    id = "damage-reported",
    name = "Damage Reporter",
    description = "Reports contact list for damaged good",
    inputDataShape = "java:java.lang.String",
    outputDataShape = "java:io.syndesis.extension.TodoReport"
)
public class TodoDamageReporter {

    @Handler
    public io.syndesis.extension.TodoReport convert(@Body java.lang.String xmlInput) throws IOException {
        ObjectMapper mapper = new XmlMapper();

        InventoryList inventoryList = mapper.readValue(xmlInput, InventoryList.class);

        TodoReport report = new TodoReport();

        List<String> damagedItems = inventoryList.getItems().stream()
                .filter(Item::isDamaged)
                .map(Item::getId)
                .collect(Collectors.toList());

        report.setContactName("Joe Daemon");
        report.setContactNumber("123");
        StringBuilder sb = new StringBuilder();
        damagedItems.forEach(s -> sb.append(s).append(" "));
        report.setDamagedItems(sb.toString());
        
        return report;
    }
}
