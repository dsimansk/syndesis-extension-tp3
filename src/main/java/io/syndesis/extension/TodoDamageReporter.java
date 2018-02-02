package io.syndesis.extension;

import io.syndesis.integration.runtime.api.SyndesisExtensionAction;
import org.apache.camel.Body;
import org.apache.camel.Handler;

import java.util.List;
import java.util.stream.Collectors;

@SyndesisExtensionAction(
    id = "damage-reported",
    name = "Damage Reporter",
    description = "Reports contact list for damaged good",
    outputDataShape = "java:io.syndesis.extension.TodoReport"
)
public class TodoDamageReporter {


    @Handler
    public io.syndesis.extension.TodoReport convert(@Body io.syndesis.extension.InventoryList inventoryList) {
        TodoReport report = new TodoReport();

        List<String> damagedItems = inventoryList.getItems().stream()
                .filter(Item::isDamaged)
                .map(Item::getId)
                .collect(Collectors.toList());

        report.setContactName("Joe");
        report.setContactNumber("123");
        report.setDamagedItems(damagedItems);

        return report;
    }
}
